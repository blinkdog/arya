/*
 * Arya.java
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

package com.pmeade.arya;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmeade.arya.gson.deserialize.AryaDeserializer;
import com.pmeade.arya.gson.serialize.AryaSerializer;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Arya provides an object (de)serialization service. Arya leverages Google's
 * gson library to do the grunt JSON work. In some sense, Arya might be thought
 * an "extension" to gson.
 * 
 * IMPORTANT: Google's Gson class is immutable and thread-safe; references may
 *            be passed around safely. Arya is STATEFUL and NOT thread-safe.
 *            Don't treat Arya references like Gson references; it probably
 *            won't work out very well.
 * 
 * @author pmeade
 */
public class Arya
{
    /** SLF4J Logging Service */
    private static final Logger log = LoggerFactory.getLogger(Arya.class);
    
    /**
     * Key used when mapping the Java type of an object in the output JSON.
     */
    public static final String TYPE = "_Arya_TYPE_";
    
    /**
     * Constant that indicates we want Arya to output JSON with as little
     * whitespace as possible.
     */
    public static final boolean COMPACT_PRINTING = false;
    
    /**
     * Constant that indicates we want Arya to output JSON with pretty-print
     * formatting. This setting generates extra formatting whitespace to make
     * the JSON more legible for human beings.
     */
    public static final boolean PRETTY_PRINTING = true;

    /**
     * Construct an Arya object to provide (de)serialization services. Arya
     * defaults to PRETTY_PRINTING when this constructor is called.
     */
    public Arya()
    {
        this(PRETTY_PRINTING);
    }

    /**
     * Construct an Arya object to provide (de)serialization services. Arya
     * will format the output JSON according to the arguments passed to the
     * constructor.
     * @param prettyPrinting true, if Arya should output JSON with
     *                       pretty-printing to make it legible for humans
     *                       false, if Arya should output JSON without
     *                       any additional whitespace to make it compact
     */
    public Arya(boolean prettyPrinting)
    {
        this.prettyPrinting = prettyPrinting;
    }

    /**
     * Obtain the UUID identity of an Object that has been previously
     * serialized with Arya.
     * @param t Object for which to look up the UUID identity
     * @return UUID of the provided Object, if and only if the Object has
     *         been previously serialized by Arya, otherwise null
     */
    public UUID getIdentity(Object t)
    {
        return objUuidMap.get(t);
    }

    /**
     * Load (deserialize) an Object from the Arya persistence store.
     * @param uuid UUID identity of the Object to be restored
     * @param type the Class of the Object to be restored
     * @return the requested Object, if and only if Arya has a specification
     *         for the object under the provided UUID identity, otherwise null
     */
    public <T> T load(UUID uuid, Class<T> type)
    {
        // if we don't already have this object in the identity->object map
        if(uuidObjMap.containsKey(uuid) == false) {
            // then, for each input provider registered with Arya
            for(AryaInput provider : providers) {
                // ask the provider if it has JSON for the provided identity
                String json = provider.input(uuid, type);
                // if we got some (not null) JSON back from the provider 
                if(json != null) {
                    // save the UUID identity for the deserializer
                    loadStack.push(uuid);
                    // deserialze the object from the JSON
                    // (this implicitly adds it to the identity->object map)
                    getGson().fromJson(json, type);
                    // and stop asking further providers for JSON
                    break;
                }
            }
        }
        // return the object (if any) from the identity map
        return (T) uuidObjMap.get(uuid);
    }
    
    /**
     * Save (serialize) an Object to the Arya persistence store.
     * @param t the Object to be saved (serialized) to the persistence store
     */
    public <T> void save(T t)
    {
        // if the object->identity map does not already contain this object
        if(objUuidMap.containsKey(t) == false) {
            // obtain the JSON representation of this Object
            // (this implicitly adds it to the object->identity map)
            String json = getGson().toJson(t);
            // for each output listener registered with Arya
            for(AryaOutput listener : listeners) {
                // inform the listener of the object that has been
                // serialized, its UUID identity, and the JSON to
                // which it was serialized
                listener.output(t, objUuidMap.get(t), json);
            }
        }
    }

    /**
     * Obtain the UUID identity saved by Arya during the call to load().
     * This is called by the AryaDeserializer object because there is no
     * good way to pass the UUID through Google's Gson library.
     * @return the UUID identity of the object, previously saved during
     *         a call to the load() method
     */
    public UUID pop()
    {
        return loadStack.pop();
    }

    /**
     * Add the provided Object with the provided UUID identity to the
     * idenity->object and object->identity maps.
     * @param uuid UUID identity for the provided Object
     * @param t Object to add to the identity<->object maps
     */
    public void populate(UUID uuid, Object t)
    {
        // log the low level detail of an object getting added to the map
        log.trace("{}:{}", uuid, t);
        // add the object and identity to the object->identity map
        objUuidMap.put(t, uuid);
        // add the identity and object to the identity->object map
        uuidObjMap.put(uuid, t);
    }

    /**
     * Register an input provider with Arya.
     * @param aryaInput reference to object implementing the AryaInput service
     *                  interface; it will provide JSON when queried with a
     *                  UUID identity for an object
     */
    public void register(AryaInput aryaInput)
    {
        providers.add(aryaInput);
    }

    /**
     * Register an output listener with Arya.
     * @param aryaOutput reference to object implementing the AryaOutput service
     *                   interface; Arya will call it with a reference to the
     *                   object, the object's UUID identity, and the JSON to
     *                   which that object was serialized
     */
    public void register(AryaOutput aryaOutput)
    {
        listeners.add(aryaOutput);
    }
    
    //-----------------------------------------------------------------------
    
    /**
     * Obtain the Google Gson singleton. If the singleton does not exist,
     * it will be created on the first call to this method.
     * @return Gson object, properly configured for JSON (de)serialization
     */
    private Gson getGson() {
        // if we don't have a Gson singleton yet
        if(gson == null) {
            // create a builder object to generate a customized Gson object
            GsonBuilder gsonBuilder = new GsonBuilder();
            // register Arya's serializer to handle serialization of every
            // object that derives from Object (i.e.: all objects)
            gsonBuilder.registerTypeHierarchyAdapter(Object.class, new AryaSerializer(this));
            // register Arya's deserializer to handle deserialization of every
            // object that derives from Object (i.e.: all objects)
            gsonBuilder.registerTypeHierarchyAdapter(Object.class, new AryaDeserializer(this));
            // if Arya was configured for pretty printing
            if(prettyPrinting) {
                // then, configure our Gson singleton for pretty printing
                gsonBuilder.setPrettyPrinting();
            }
            // create the Gson singleton
            gson = gsonBuilder.create();
        }
        // return the Gson singleton to the caller
        return gson;
    }

    /**
     * List of output listeners. This list is used to register output listeners
     * who are interested in receiving Arya serialization events.
     */
    private List<AryaOutput> listeners = new ArrayList();
    
    /**
     * List of input providers. This list is used to register input providers
     * who will provide JSON when queried with UUID identities of objects.
     */
    private List<AryaInput> providers = new ArrayList();
    
    /**
     * Object->Identity map. This map tracks the progress of serialization over
     * complex object graphs.
     */
    private Map<Object,UUID> objUuidMap = new HashMap();
    
    /**
     * Identity->Object map. This map tracks the progress of deserialization
     * over complex object graphs.
     */
    private Map<UUID,Object> uuidObjMap = new HashMap();
    
    /**
     * LinkedList implementing a storage stack of UUID identity objects.
     * Calls to load() store the requested UUID identities here so that
     * AryaDeserializer can retrieve it when the Google Gson library
     * instructs it to do so.
     * 
     * NOTE: This probably doesn't need to be a LinkedList object.
     *       At the time, I thought the load() calls would happen
     *       recursively and build up on the stack, necessitating
     *       a stack of UUID identities ready to be retrieved. I'm
     *       not so sure about that now. It is a TODO point to
     *       convince myself that this stack arrangement is
     *       unnecessary and change this to a simple UUID reference
     *       field.
     */
    private LinkedList<UUID> loadStack = new LinkedList();

    /**
     * Gson singleton. This reference to Google's Gson object does the grunt
     * work with the JSON. The method getGson() creates the singleton, properly
     * configured with an AryaSerializer and AryaDeserializer to do the
     * smart work.
     * 
     * NOTE: Gson references are immutable and thread-safe. Constructing a
     *       single reference and reusing it is safe and efficient. Arya is
     *       stateful and NOT thread-safe. Don't mistake one for the other.
     */
    private Gson gson;
    
    /**
     * Flag: Gson singleton is constructed to use pretty-printing.
     * This flag is set by Arya's constructor and then baked into the Gson
     * singleton when getGson() is first called.
     */
    private boolean prettyPrinting;
}
