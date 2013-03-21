# arya

Object graph serialization library for Google Gson

## Motivation

When using Gson to serialize complex object graphs, it is easy to create a 
very large block of JSON with full representations of embedded objects. Even
worse, it is easy to create an object graph with cycles, which leads to a
StackOverflowError.

Fixing these problems requires custom JsonSerializer<T> and JsonDeserializer<T>
classes. The problem with the custom approach is that every domain class that
requires custom serialization now requires two additional classes. And, every
time the domain class changes, these custom classes must now be kept in sync.
The workload has tripled and become prone to runtime errors. Ouch!

## Arya's Solution

Arya breaks the object graph serialization problem into the smaller problem
of serializing a bunch of objects. The simple contents (primitives, String,
etc.) are serialized directly to JSON. References to other objects are
converted to UUIDs. Referenced objects are then serialized recursively.

### Object Graph with Cycles in Gson

    { "a": { "b": { "a": { "b": { "a": ... StackOverflowError!

### Object Graph with Cycles in Arya

    { "a": "211379b8-b9c0-4202-9c4b-a399aa18e11b" }
    { "b": "4cdc768b-1164-40d6-b2f4-4b319bc289d2" }

## Usage

This is how you serialize an object graph to Arya:

    Arya arya = new Arya();
    arya.save(myObject);

This is how you deserialize an object graph from Arya:

    UUID myObjectUuid = UUID.fromString("a8af7511-5dc4-40fb-bffa-a9e3b5d70a2a");
    Arya arya = new Arya();
    MyClass myObject = arya.load(myObjectUuid, MyClass.class);

The next question you might have: Where does all the JSON data go to/come from?

### Input/Output Registration

Arya defines two interfaces `AryaInput` and `AryaOutput`. These are the
interfaces that allow you to connect Arya to whatever you'd like. Connecting
Arya to these inputs and outputs is simple:

    Arya arya = new Arya();
    AryaInput aryaInput = getSomeInputSource();
    AryaOutput aryaOutput = getSomeOutputSink();
    arya.register(aryaInput);
    arya.register(aryaOutput);

#### AryaInput

AryaInput provides JSON data to Arya for deserialization into Java objects.
The interface defines a single method:

    public <T> String input(UUID uuid, Class<T> type);

Arya will provide the UUID identity of the object and it's Java type. The
implementing class should return a block of JSON, or null if it has no
object by that UUID identity.

#### AryaOutput

AryaOutput is used to listen to Arya's serialization events. When Arya
serializes an object to JSON, it will send the event to AryaOutput listeners.
The interface defines a single method:

    public <T> void output(T t, UUID uuid, String json);

Arya will provide a reference to the object being serialized, the UUID identity
of that object, and the block of JSON to which the object was serialized.

Here is a simple (anonymous) implementation of AryaOutput that will print the
JSON to Java's standard output:

    Arya arya = new Arya();
    arya.register(new AryaOutput() {
        public <T> void output(T t, UUID uuid, String json) {
            System.out.println(json);
        }
    });
    arya.save(myObject);

## Shortcomings

Arya is not perfect. It is stable and functional, albeit there are plenty
of bells and whistles that I would still like to implement. Below you'll
find a list of things Arya doesn't handle (or doesn't handle well) yet.
If these impact your use-case for Arya, you may want to reconsider using
the library at this time.

### Statefulness and Thread Safety

Unlike Gson, Arya is both stateful and not thread-safe. When you create an
Arya object, you should use it to serialize a graph or deserialize a graph
and then dispose of the reference. Each instance can only be used a single
time; after that, you probably won't get what you want.

### Full Graph Only

Arya works on full object graphs only. When serializing, it will attempt to
serialize every object to which it can get a reference. When deserializing,
it will attempt to fully restore the entire graph to working memory. There
is no direct support for lazy-loading.

For small domains, this usually isn't a problem. For very large domains,
this could represent a significant problem. You'll need to judge the size
of your dataset and plan accordingly.

### Serialization Limitations

The following cases are known not to work:

* `Map<K,V>` where K is parameterized type. JSON supports arrays and objects
  on the value (V) side, but only strings on the key (K) side.
    * Example: `Map<List<String>,Integer>` is not supported.
 
* `Map<K,V>` where K or V is an array type or enumerated type.
    * Example: `Map<String,Double[]>` is not supported.
    * Example: `Map<String,TrafficLightColor>` is not supported.
        * Note: `TrafficLightColor` is a Java enum containing RED, YELLOW, GREEN.

* `List<V>` where V is an array type or enumerated type.
    * Example: `List<Double[]>` is not supported.
    * Example: `List<TrafficLightColor>` is not supported.

The following cases are suspected not to work:

* Arrays with nulls embedded in them
* Objects with null references (instead of empty collections) for parameterized
  types (List, Map, etc.)
* Custom parameterized types, such as `MyCollectionType<K,V,X>` (for example)

### Control Limitations

Apart from an `@Id` annotation, which lets an object identify a field
containing its UUID identity, there is little external control over Arya's
serialization and deserialization process.

Here are some known control limits:

* Fields that are final, transient, static, or volatile are not serialized,
  and there is no way to override this.

* Fields that are not final, transient, static, or volatile are serialized,
  and there is no way to override this.

* Fields are always serialized with the Java name, and there is no way
  to override this.

* Arya always emits the Java type in the JSON and always maps it to the
  constant `__Arya_TYPE__`, and there is no way to override this.
    * Arya will always emit the actual object type during serialization, and
      there is no way to override this.

* Arya will make its best guess when deserializing collections interfaces. For
  example, an object with a field of type `List<V>` will be deserialized as
  an `ArrayList<V>`, and there is no way to override this.

* Arya does not consider or obey the Gson annotations like `@Expose`,
  `@SerializedName`, or `@Since`.

* Arya does not emit version metadata information to the JSON, and there is
  no way to override this.
    * Note: Version data that is a proper part of the domain (if it exists)
      IS serialized along with all the other domain data.

### Extension Limitations

Arya makes use of Gson's custom serialization and deserialization facilities.
However, it does not pass this extensibility on to its clients.

### Input/Output Support

Arya ships with no `AryaInput` / `AryaOutput` support out of the box. If you
want to connect Arya to a file system, database, network connection, etc. then
you will need to implement these for yourself.

## Why is it named 'Arya'?

Arya Stark is my favorite character from [A Song of Ice and Fire](http://en.wikipedia.org/wiki/A_Song_of_Ice_and_Fire),
so I decided to name the library after her.
