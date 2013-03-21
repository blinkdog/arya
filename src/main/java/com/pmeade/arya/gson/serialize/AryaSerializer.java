/*
 * AryaSerializer.java
 * Copyright 2013 Patrick Meade.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pmeade.arya.gson.serialize;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pmeade.arya.Arya;
import com.pmeade.arya.annotation.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AryaSerializer implements serialization for objects that derive from Object
 * (i.e.: all Java objects). It implements Gson's JsonSerializer so that Gson
 * will call it when attempting to serialize an object to JSON.
 * @author pmeade
 */
public class AryaSerializer implements JsonSerializer<Object>
{
    /** SLF4J Logging Service */
    private static final Logger log = LoggerFactory.getLogger(AryaSerializer.class);

    /**
     * Handy map of types to instances of FieldSerializer to handle
     * serialization of that type.
     */
    private static final Map<Type,FieldSerializer> typeMap =
        new ImmutableMap.Builder<Type,FieldSerializer>()
            .put(boolean.class,   FieldSerializer.BOOLEAN)
            .put(Boolean.class,   FieldSerializer.BOOLEAN)
            .put(byte.class,      FieldSerializer.BYTE)
            .put(Byte.class,      FieldSerializer.BYTE)
            .put(char.class,      FieldSerializer.CHAR)
            .put(Character.class, FieldSerializer.CHAR)
            .put(short.class,     FieldSerializer.SHORT)
            .put(Short.class,     FieldSerializer.SHORT)
            .put(int.class,       FieldSerializer.INT)
            .put(Integer.class,   FieldSerializer.INT)
            .put(long.class,      FieldSerializer.LONG)
            .put(Long.class,      FieldSerializer.LONG)
            .put(float.class,     FieldSerializer.FLOAT)
            .put(Float.class,     FieldSerializer.FLOAT)
            .put(double.class,    FieldSerializer.DOUBLE)
            .put(Double.class,    FieldSerializer.DOUBLE)
            .put(Date.class,      FieldSerializer.DATE)
            .put(String.class,    FieldSerializer.STRING)
            .put(UUID.class,      FieldSerializer.UUID)
            .build();
    
    /**
     * Construct an AryaSerializer.
     * @param arya Arya reference to populate with UUID identities as provided
     *             Java objects are serialized to JSON
     */
    public AryaSerializer(Arya arya) {
        this.arya = arya;
        this.arrayFieldSerializer = new ArrayFieldSerializer(arya, this);
        this.enumFieldSerializer = new EnumFieldSerializer();
        this.objectFieldSerializer = new ObjectFieldSerializer(arya);
        this.parameterizedFieldSerializer = new ParameterizedFieldSerializer(arya, this);
    }

    /**
     * Determine if the provided type requires serialization. If it is a simple
     * type that can be expressed directly in JSON, then this returns false. If
     * it is a complex type that requires Arya to serialize the object (and the
     * UUID identity is stored in the JSON), then this returns true.
     * @param clazz type for which to determine the necessity of serialization
     * @return true, if the type requires serialization; false, if the type is
     *         simple and can be directly represented in JSON
     */
    public boolean isSaveRequired(Class clazz) {
        return !typeMap.containsKey(clazz);
    }
    
    /**
     * Gson serialization handler method. This method is called when Gson wants
     * to convert a Java Object into JSON.
     * @param t Java Object to be converted into JSON
     * @param type type of the Java object to be serialized
     * @param jsc JsonSerializationContext -- unknown and unused
     * @return a JsonObject containing the serialized JSON representation of
     *         the provided Java Object
     */
    @Override // implements JsonSerializer<Object>
    public JsonElement serialize(Object t, Type type, JsonSerializationContext jsc)
    {
        // find the UUID identity of this object; this would be a UUID field
        // marked with the @Id annotation, if it exists on the provided object
        UUID uuid = findUuid(t);
        // if we couldn't locate a UUID identity for the provided object
        if(uuid == null) {
            // generate a random UUID identity for the provided object
            uuid = UUID.randomUUID();
        }
        // tell Arya to populate its serialization map with the UUID identity
        // of the object and the object itself
        arya.populate(uuid, t);
        // create the JsonObject into which we will serialize the provided
        // Java object
        JsonObject jo = new JsonObject();
        // encode the Java type of the object into the Arya.TYPE field
        // we do this so that we can recover the appropriate type during
        // deserialization; we typically need this when the object has a
        // field of the supertype but contains a reference to a subtype
        jo.addProperty(Arya.TYPE, t.getClass().getName());
        // start with the actual type of the provided object
        Class tClazz = t.getClass();
        // while we haven't reached the top the inheritance hierarchy
        while(tClazz != Object.class)
        {
            // obtain the fields on this class
            Field[] fields = tClazz.getDeclaredFields();
            // for each field in the class
            for(Field field : fields) {
                // skip anything that is final, static, transient, or volatile
                if(Modifier.isFinal(field.getModifiers())) continue;
                if(Modifier.isStatic(field.getModifiers())) continue;
                if(Modifier.isTransient(field.getModifiers())) continue;
                if(Modifier.isVolatile(field.getModifiers())) continue;
                // ensure that we can access the data contained in the field
                field.setAccessible(true);
                // request a FieldSerializer object that understands how to
                // serialize fields of the provided type
                FieldSerializer fieldSerializer = typeMap.get(field.getType());
                // if the type is a parameterized type (i.e.: Collection or Map)
                Type genericType = field.getGenericType();
                if(genericType instanceof ParameterizedType) {
                    // use a ParameterizedFieldSerializer to handle serialization
                    fieldSerializer = parameterizedFieldSerializer;
                }
                // if the type is an enumerated type
                if(field.getType().isEnum()) {
                    // use a EnumFieldSerializer to handle serialization
                    fieldSerializer = enumFieldSerializer;
                }
                // if the type is an array
                if(field.getType().isArray()) {
                    // use an ArrayFieldSerializer to handle serialization
                    fieldSerializer = arrayFieldSerializer;
                }
                // if we still haven't found an appropriate FieldSerializer
                if(fieldSerializer == null) {
                    // default to the ObjectFieldSerializer to handle
                    // serialization of complex types
                    fieldSerializer = objectFieldSerializer;
                }
                try {
                    // attempt to serialize the field to JSON
                    fieldSerializer.serialize(jo, t, field);
                } catch(IllegalAccessException e) {
                    // if we failed to serialize, log the error
                    log.error("Unable to access field '" + field.getName() + "':", e);
                }
            }
            // after handling all the fields on the class, we move up the
            // inheritance hierarchy and handle the fields of the superclass
            tClazz = tClazz.getSuperclass();
        }
        // return the JsonObject that contains the data serialized to JSON
        return jo;
    }

    /**
     * Find the UUID identity of the provided object.
     * @param t Object for which to find the UUID identity
     * @return UUID identity of the provided object, if the object has a UUID
     *         field with the @Id annotation that contains a non-null value;
     *         otherwise this method returns null
     */
    private UUID findUuid(Object t)
    {
        // reference to a UUID, we'll populate it if we happen to find one
        UUID uuid = null;
        // start with the actual type of the provided object
        Class tClazz = t.getClass();
        // while we haven't reached the top of the inheritance hierarchy
        while(tClazz != Object.class)
        {
            // obtain the fields in this class
            Field[] fields = tClazz.getDeclaredFields();
            // for each field in the class
            for(Field field : fields) {
                // ensure we can access the data of the field
                field.setAccessible(true);
                // see if the field has an @Id annotation
                Id id = field.getAnnotation(Id.class);
                // if it does have an @Id annotation
                if(id != null) {
                    // and this field has the type UUID
                    if(field.getType().equals(UUID.class)) {
                        // and if we haven't previously found a UUID identity
                        if(uuid == null) {
                            try {
                                // obtain the UUID identity of the object,
                                // if it has one in the field
                                uuid = (UUID) field.get(t);
                            } catch(IllegalAccessException e) {
                                // if we weren't able to access the field,
                                // then log the error
                                log.error("Unable to access field marked with @Id:", e);
                            }
                        }
                        // otherwise, if we have previously found a UUID
                        // identity for this object
                        else {
                            // log the error
                            log.error("Multiple fields marked with @Id");
                            // return null; there is no way to tell which UUID
                            // identity we should be using or not
                            return null;
                        }
                    }
                }
            }
            // after handling all the fields in this class, we move up a level
            // in the inheritance hierarchy and work on the superclass
            tClazz = tClazz.getSuperclass();
        }
        // if we found one (and only one) UUID identity, return it, otherwise
        // return the null value that we were unable to populate
        return uuid;
    }

    /**
     * Arya reference. We use this to inform Arya of the objects that are
     * being serialized by this AryaSerializer.
     */
    private Arya arya;
    
    /**
     * ArrayFieldSerializer is used to serialize array fields. This serializer
     * also handles multi-dimensional arrays like int[][][].
     */
    private ArrayFieldSerializer arrayFieldSerializer;
    
    /**
     * EnumFieldSerializer is used to serialize enum fields.
     */
    private EnumFieldSerializer enumFieldSerializer;
    
    /**
     * ObjectFieldSerializer is used to serialize reference fields. If we
     * aren't able to represent the field directly in JSON, and it isn't an
     * array, an enum, or a parameterized collection, then it's probably a
     * complex object that itself requires serialization.
     */
    private ObjectFieldSerializer objectFieldSerializer;
    
    /**
     * ParameterizedFieldSerializer is used to serialize parameterized
     * collections. This serializer handles serialization of Collection<V>,
     * Map<K,V>, and other parameterized types.
     */
    private ParameterizedFieldSerializer parameterizedFieldSerializer;
}
