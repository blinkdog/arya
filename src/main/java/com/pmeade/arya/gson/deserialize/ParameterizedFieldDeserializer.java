/*
 * ParameterizedFieldDeserializer.java
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

package com.pmeade.arya.gson.deserialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pmeade.arya.Arya;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ParameterizedFieldDeserializer deserializes JSON arrays and JSON objects
 * into parameterized collections. Objects that derive from Collection<V> are
 * assumed to deserialize from JSON arrays. Objects that derive from Map<K,V>
 * are assumed to deserialize from a JSON object. After deserialization, the
 * Java object is populated in the specified field on the provided object.
 * @author pmeade
 */
public class ParameterizedFieldDeserializer implements FieldDeserializer
{
    /** SLF4J Logging Service */
    private static final Logger log = LoggerFactory.getLogger(ParameterizedFieldDeserializer.class);

    /**
     * Construct a ParameterizedFieldDeserializer.
     * @param arya Arya reference to further deserialize complex types
     * @param aryaDeserializer AryaDeserializer to query if a type is simple
     *                         and can be handled directly, or if it is complex
     *                         and requires further deserialization of its own
     */
    public ParameterizedFieldDeserializer(Arya arya, AryaDeserializer aryaDeserializer) {
        this.arya = arya;
        this.aryaDeserializer = aryaDeserializer;
    }

    /**
     * Request deserialization of a JSON array into a Collection<V> or
     * deserialization of a JSON object into a Map<K,V>. The deserialized
     * object will be populated in the provided field on the provided Object.
     * @param jo JsonObject containing the JSON to be deserialized
     * @param t Object to be populated with the parameterized content
     * @param field Field of the object to be populated (also identifies the
     *              name of the JSON to be deserialized)
     * @throws IllegalAccessException never thrown (you don't actually believe
     *                                what I just told you, right?)
     */
    public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // if the provided JsonObject has JSON data mapped for the field
        if(jo.has(field.getName())) {
            // obtain the raw parameterized type (Collection, Map, etc.)
            ParameterizedType paraType = (ParameterizedType) field.getGenericType();
            Class rawType = (Class) paraType.getRawType();
            // if the raw type is assignable to Collection
            if(Collection.class.isAssignableFrom(rawType)) {
                // assume the JSON data is an array, and obtain in that format
                JsonArray ja = jo.getAsJsonArray(field.getName());
                // deserialize the JSON array into a Collection object and
                // populate the field in the provided object
                field.set(t, deserializeCollection(ja, paraType));
            }
            // otherwise, if the raw type is assignable to Map
            else if(Map.class.isAssignableFrom(rawType)) {
                // assume the JSON data is a JSON object, and obtain that
                JsonObject jo2 = jo.getAsJsonObject(field.getName());
                // deserialize the JSON object into a Map object and
                // populate the field in the provided object
                field.set(t, deserializeMap(jo2, paraType));
            }
            // otherwise, these aren't the droids we're looking for
            else {
                // we can go about our business ... move along
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Deserialize a JSON array into a Collection object.
     * @param ja JSON data wrapped in Gson's JsonArray
     * @param type type of the Collection object
     * @return Collection object containing the deserialized JSON array
     */
    private Object deserializeCollection(JsonArray ja, Type type)
    {
        // obtain the raw parameterized type (i.e.: Collection, List,
        // Set, LinkedList, etc.)
        ParameterizedType paraType = (ParameterizedType) type;
        Class rawType = (Class) paraType.getRawType();
        // a reference to the collection that we will create below
        Collection c = null;
        // if the raw parameterized type is an interface
        if(rawType.isInterface()) {
            // then we need to come up with a matching concrete implementation
            // because an interface cannot be instantiated directly
            if(Collection.class.equals(rawType)) { c = new ArrayList(); }
            else if(List.class.equals(rawType)) { c = new ArrayList(); }
            else if(Set.class.equals(rawType)) { c = new HashSet(); }
            else if(SortedSet.class.equals(rawType)) { c = new TreeSet(); }
            else {
                // we don't know what kind of collection this is,
                // so we don't know what concrete implementation to provide
                throw new UnsupportedOperationException();
            }
        }
        // otherwise we just need one of whatever this thing is expecting
        else {
            try {
                // attempt to instantiate whatever type we were provided
                c = (Collection) rawType.newInstance();
            } catch(Exception e) {
                // if we couldn't instantiate that, fall back to an ArrayList
                log.error("Unable to create type " + rawType + ":", e);
                c = new ArrayList();
            }
        }
        // now, determine the actual type of the thing in the collection
        // that is: Collection<X> .. what type is X?
        Type[] actualTypes = paraType.getActualTypeArguments();
        // if the actual type is also parameterized, that is, we're dealing
        // with something like List<List<String>> or Set<Map<Integer,String>
        if(actualTypes[0] instanceof ParameterizedType)
        {
            // determine the raw parameterized type of the thing in the
            // collection (i.e.: is it a Collection or a Map in our collection?)
            ParameterizedType innerParaType = (ParameterizedType) actualTypes[0];
            Class innerRawType = (Class) innerParaType.getRawType();
            // if the inner type is assignable to Collection
            if(Collection.class.isAssignableFrom(innerRawType)) {
                // then for each element in the provided JsonArray
                for(int i=0; i<ja.size(); i++) {
                    // deserialze that element into its own collection, and
                    // then add that collection to our collection
                    c.add(deserializeCollection(ja.get(i).getAsJsonArray(), innerParaType));
                }
            }
            // otherwise if the inner type is assignable to Map
            else if(Map.class.isAssignableFrom(innerRawType)) {
                // then for each element in the provided JsonArray
                for(int i=0; i<ja.size(); i++) {
                    // deserialze that element into its own map, and then
                    // add that map to our collection
                    c.add(deserializeMap(ja.get(i).getAsJsonObject(), innerParaType));
                }
            }
            else {
                // we don't know what kind of parameterized type this is
                throw new UnsupportedOperationException();
            }
        }
        // TODO: Add else-if case here to handle inner array types. This would
        //       add support for cases like List<Boolean[]> or Set<double[][]>
        //
//        else if(actualTypes[0] is Array of some type) { blah }
        // otherwise, since the actual type is not parameterized, we'll
        // assume it is a simple type to be handled directly, or a complex
        // type that needs further deserialization of its own
        else
        {
            // obtain the Class object for the actual type (which we know
            // is not a parameterized type)
            Class actualType = (Class) actualTypes[0];
            // if the AryaDeserializer indicates that this is a complex type
            if(aryaDeserializer.isLoadRequired((Class)actualTypes[0])) {
                // then for each element in the JSON array
                for(int i=0; i<ja.size(); i++) {
                    // obtain the UUID identity of the object to be deserialized
                    String sUuid = ja.get(i).getAsString();
                    // convert it to an actual UUID object
                    java.util.UUID uuid = java.util.UUID.fromString(sUuid);
                    // ask Arya to deserialize the object, then add it to
                    // our collection
                    c.add(arya.load(uuid, (Class)actualTypes[0]));
                }
            }
            // otherwise, AryaDeserializer has indicated this is a simple type
            // that can be handled directly
            else {
                // for each element in the JSON array
                for(int i=0; i<ja.size(); i++) {
                    // TODO: Refactor this huge if-else block into something
                    //       a little nicer; perhaps FieldDeserializer could
                    //       require a little more functionality and
                    //       EnumFieldDeserializer could provide the correct
                    //       value by querying on the componentType
                    if(actualType.equals(boolean.class)) { c.add(ja.get(i).getAsBoolean()); }
                    else if(actualType.equals(Boolean.class)) { c.add(ja.get(i).getAsBoolean()); }
                    else if(actualType.equals(byte.class)) { c.add(ja.get(i).getAsByte()); }
                    else if(actualType.equals(Byte.class)) { c.add(ja.get(i).getAsByte()); }
                    else if(actualType.equals(char.class)) { c.add(ja.get(i).getAsCharacter()); }
                    else if(actualType.equals(Character.class)) { c.add(ja.get(i).getAsCharacter()); }
                    else if(actualType.equals(short.class)) { c.add(ja.get(i).getAsShort()); }
                    else if(actualType.equals(Short.class)) { c.add(ja.get(i).getAsShort()); }
                    else if(actualType.equals(int.class)) { c.add(ja.get(i).getAsInt()); }
                    else if(actualType.equals(Integer.class)) { c.add(ja.get(i).getAsInt()); }
                    else if(actualType.equals(long.class)) { c.add(ja.get(i).getAsLong()); }
                    else if(actualType.equals(Long.class)) { c.add(ja.get(i).getAsLong()); }
                    else if(actualType.equals(float.class)) { c.add(ja.get(i).getAsFloat()); }
                    else if(actualType.equals(Float.class)) { c.add(ja.get(i).getAsFloat()); }
                    else if(actualType.equals(double.class)) { c.add(ja.get(i).getAsDouble()); }
                    else if(actualType.equals(Double.class)) { c.add(ja.get(i).getAsDouble()); }
                    else if(actualType.equals(Date.class)) {
                        c.add(new Date(ja.get(i).getAsLong()));
                    }
                    else if(actualType.equals(String.class)) {
                        c.add(ja.get(i).getAsString());
                    }
                    else if(actualType.equals(UUID.class)) {
                        c.add(java.util.UUID.fromString(ja.get(i).getAsString()));
                    }
                    else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
        }
        // we're done populating the collection, so return it to the caller
        return c;
    }
    
    /**
     * Deserialize a JSON object into a Map object.
     * @param jo JSON data wrapped in Gson's JsonObject
     * @param type type of the Map object
     * @return Map containing the deserialized JSON object
     */
    private Object deserializeMap(JsonObject jo, Type type)
    {
        // obtain the raw parameterized type (i.e.: Map, SortedMap, etc.)
        ParameterizedType paraType = (ParameterizedType) type;
        Class rawType = (Class) paraType.getRawType();
        // reference to the map that we will create below
        Map map = null;
        // if the map type is an interface
        if(rawType.isInterface()) {
            //  then we need to come up with a matching concrete implementation
            if(Map.class.equals(rawType)) { map = new HashMap(); }
            else if(NavigableMap.class.equals(rawType)) { map = new TreeMap(); }
            else if(SortedMap.class.equals(rawType)) { map = new TreeMap(); }
            else {
                // we don't know what kind of map this is, so we don't know
                // what concrete implementation to provide
                throw new UnsupportedOperationException();
            }
        }
        // otherwise we just need one of whatever this thing is expecting
        else {
            try {
                // attempt to instantiate the provided concrete type 
                map = (Map) rawType.newInstance();
            } catch(Exception e) {
                // if we failed at that, fall back to a HashMap
                log.error("Unable to create type " + rawType + ":", e);
                map = new HashMap();
            }
        }
        // now, determine the actual type of the things in the map
        // that is: Map<K,V> .. what type are K and V?
        Type[] actualTypes = paraType.getActualTypeArguments();
        // if the value type (V) is also a parameterized type (i.e.: a Map
        // or Collection in it's own right; e.g.: Map<String,List<Integer>>)
        if(actualTypes[1] instanceof ParameterizedType)
        {
            // determine the raw parameterized type of the thing in the
            // value side of the map (i.e.: is it a Collection or a Map?)
            Class keyType = (Class) actualTypes[0];
            ParameterizedType innerParaType = (ParameterizedType) actualTypes[1];
            Class innerRawType = (Class) innerParaType.getRawType();
            // for each entry in the provided JsonObject data
            for(Entry<String,JsonElement> entry : jo.entrySet())
            {
                // obtain the object that corresponds to the key part of the map
                Object key = toMapKey(entry.getKey(), keyType);
                // a reference to the value part of the map that we'll create below
                Object value = null;
                // if the inner value type is assignable to Collection
                if(Collection.class.isAssignableFrom(innerRawType)) {
                    // deserialize the value side as a Collection object
                    value = deserializeCollection(entry.getValue().getAsJsonArray(), innerParaType);
                }
                // otheerwise, if the inner value type is assignable to Map
                else if(Map.class.isAssignableFrom(innerRawType)) {
                    // deserialize the value side as a Map object
                    value = deserializeMap(entry.getValue().getAsJsonObject(), innerParaType);
                }
                else {
                    // otherwise, we don't know what kind of parameterized
                    // type this represents...
                    throw new UnsupportedOperationException();
                }
                // once we've obtained both key and value, populate the map
                map.put(key, value);
            }
        }
        // TODO: Add else-if case here to handle inner array types. This would
        //       add support for cases like List<Boolean[]> or Set<double[][]>
        //
//        else if(actualTypes[0] is Array of some type) { blah }
        // otherwise, the value side is not a parameterized type
        else
        {
            // determine the actual types of the key and value of the map
            Class keyType = (Class) actualTypes[0];
            Class valueType = (Class) actualTypes[1];
            // for each entry in the provided JsonObject data
            for(Entry<String,JsonElement> entry : jo.entrySet())
            {
                // convert the key side (String) to an object of the key type
                Object key = toMapKey(entry.getKey(), keyType);
                // convert the value side (JsonElement) to an object of the
                // value type
                Object value = toMapValue(entry.getValue(), valueType);
                // once we've obtained both key and value, populate the map
                map.put(key, value);
            }
        }
        // return the completed map to the caller
        return map;
    }
    
    /**
     * Convert a string (from JSON) into an object of the provided type.
     * @param jsonKey string provided from the key side of a JsonObject
     * @param actualType type to which to convert the provided string
     * @return Object of the requested type, populated with the provided JSON
     */
    public Object toMapKey(String jsonKey, Class actualType)
    {
        // if AryaDeserializer indicates that this is a complex type
        if(aryaDeserializer.isLoadRequired(actualType)) {
            // then convert the string into a UUID identity that defines the
            // object to be deserialized
            java.util.UUID uuid = java.util.UUID.fromString(jsonKey);
            // ask Arya to deserialize the identified object and return it
            return arya.load(uuid, actualType);
        }
        else {
            // TODO: Refactor this huge if-else block into something
            //       a little nicer; perhaps FieldDeserializer could
            //       require a little more functionality and
            //       EnumFieldDeserializer could provide the correct
            //       value by querying on the componentType
            if(actualType.equals(boolean.class)) { return Boolean.valueOf(jsonKey); }
            else if(actualType.equals(Boolean.class)) { return Boolean.valueOf(jsonKey); }
            else if(actualType.equals(byte.class)) { return Byte.valueOf(jsonKey); }
            else if(actualType.equals(Byte.class)) { return Byte.valueOf(jsonKey); }
            else if(actualType.equals(char.class)) { return Character.valueOf(jsonKey.charAt(0)); }
            else if(actualType.equals(Character.class)) { return Character.valueOf(jsonKey.charAt(0)); }
            else if(actualType.equals(short.class)) { return Short.valueOf(jsonKey); }
            else if(actualType.equals(Short.class)) { return Short.valueOf(jsonKey); }
            else if(actualType.equals(int.class)) { return Integer.valueOf(jsonKey); }
            else if(actualType.equals(Integer.class)) { return Integer.valueOf(jsonKey); }
            else if(actualType.equals(long.class)) { return Long.valueOf(jsonKey); }
            else if(actualType.equals(Long.class)) { return Long.valueOf(jsonKey); }
            else if(actualType.equals(float.class)) { return Float.valueOf(jsonKey); }
            else if(actualType.equals(Float.class)) { return Float.valueOf(jsonKey); }
            else if(actualType.equals(double.class)) { return Double.valueOf(jsonKey); }
            else if(actualType.equals(Double.class)) { return Double.valueOf(jsonKey); }
            else if(actualType.equals(Date.class)) { return new Date(Long.valueOf(jsonKey)); }
            else if(actualType.equals(String.class)) { return jsonKey; }
            else if(actualType.equals(UUID.class)) { return java.util.UUID.fromString(jsonKey); }
        }
        // we don't know how to convert to an object of the requested type...
        throw new UnsupportedOperationException();
    }

    public Object toMapValue(JsonElement jsonElement, Class actualType)
    {
        // if AryaDeserializer indicates that this is a complex type
        if(aryaDeserializer.isLoadRequired(actualType)) {
            // obtain the JsonElement as a String form of the UUID identity
            // of the object to be deserialized
            String sUuid = jsonElement.getAsString();
            // then convert the string into a UUID identity that defines the
            // object to be deserialized
            java.util.UUID uuid = java.util.UUID.fromString(sUuid);
            // ask Arya to deserialize the identified object and return it
            return arya.load(uuid, actualType);
        } else {
            // obtain the JsonElement as a String form of the object
            String strJsonElement = jsonElement.getAsString();
            // TODO: Refactor this huge if-else block into something
            //       a little nicer; perhaps FieldDeserializer could
            //       require a little more functionality and
            //       EnumFieldDeserializer could provide the correct
            //       value by querying on the componentType
            if(actualType.equals(boolean.class)) { return Boolean.valueOf(strJsonElement); }
            else if(actualType.equals(Boolean.class)) { return Boolean.valueOf(strJsonElement); }
            else if(actualType.equals(byte.class)) { return Byte.valueOf(strJsonElement); }
            else if(actualType.equals(Byte.class)) { return Byte.valueOf(strJsonElement); }
            else if(actualType.equals(char.class)) { return Character.valueOf(strJsonElement.charAt(0)); }
            else if(actualType.equals(Character.class)) { return Character.valueOf(strJsonElement.charAt(0)); }
            else if(actualType.equals(short.class)) { return Short.valueOf(strJsonElement); }
            else if(actualType.equals(Short.class)) { return Short.valueOf(strJsonElement); }
            else if(actualType.equals(int.class)) { return Integer.valueOf(strJsonElement); }
            else if(actualType.equals(Integer.class)) { return Integer.valueOf(strJsonElement); }
            else if(actualType.equals(long.class)) { return Long.valueOf(strJsonElement); }
            else if(actualType.equals(Long.class)) { return Long.valueOf(strJsonElement); }
            else if(actualType.equals(float.class)) { return Float.valueOf(strJsonElement); }
            else if(actualType.equals(Float.class)) { return Float.valueOf(strJsonElement); }
            else if(actualType.equals(double.class)) { return Double.valueOf(strJsonElement); }
            else if(actualType.equals(Double.class)) { return Double.valueOf(strJsonElement); }
            else if(actualType.equals(Date.class)) { return new Date(Long.valueOf(strJsonElement)); }
            else if(actualType.equals(String.class)) { return strJsonElement; }
            else if(actualType.equals(UUID.class)) { return java.util.UUID.fromString(strJsonElement); }
        }
        // we don't know how to convert to an object of the requested type...
        throw new UnsupportedOperationException();
    }

    /**
     * Arya reference to further deserialize complex types.
     */
    private Arya arya;
    
    /**
     * AryaDeserializer to query if a type is simple and can be handled
     * directly, or if it is complex and requires further deserialization
     * of its own.
     */
    private AryaDeserializer aryaDeserializer;
}
