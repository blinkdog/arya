/*
 * ParameterizedFieldSerializer.java
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pmeade.arya.Arya;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * ParameterizedFieldSerializer serializes parameterized fields (i.e.: Map,
 * Collection, List) into JSON.
 * @author pmeade
 */
public class ParameterizedFieldSerializer implements FieldSerializer
{
    /**
     * Construct a ParameterizedFieldSerializer.
     * @param arya Arya reference to do further serialization if needed
     * @param aryaSerializer AryaSerializer reference to determine if a type
     *                       is simple and can be represented directly in JSON
     *                       or if additional serialization is required
     */
    public ParameterizedFieldSerializer(Arya arya, AryaSerializer aryaSerializer) {
        this.arya = arya;
        this.aryaSerializer = aryaSerializer;
    }

    /**
     * Serialize the provided field in the provided object to JSON, and then
     * populate that JSON in the provided JsonObject.
     * @param jo JsonObject to be populated with the JSON data
     * @param t Object containing the data to be serialized to JSON
     * @param field Field to be serialized in the provided object
     * @throws IllegalAccessException 
     */
    public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // obtain the raw parameterized type
        ParameterizedType paraType = (ParameterizedType) field.getGenericType();
        Class rawType = (Class) paraType.getRawType();
        // reference to a JsonElement that we will populate below
        JsonElement je = null;
        // if the raw type is assignable to Collection
        if(Collection.class.isAssignableFrom(rawType)) {
            // create a JsonArray to hold the elements of the collection
            JsonArray ja = new JsonArray(); je = ja;
            // obtain the collection from the provided field in the object
            Object collection = field.get(t);
            // serialize the collection to our JsonArray
            serializeCollection(ja, collection);
        }
        // otherwise, if the raw type is assignable to Map
        else if(Map.class.isAssignableFrom(rawType)) {
            // create a JsonObject to hold the elements of the map
            JsonObject jo2 = new JsonObject(); je = jo2;
            // obtain the map from the provided field in the object
            Object map = field.get(t);
            // serialize the map to our JsonObject
            serializeMap(jo2, map);
        }
        // otherwise...
        else {
            // we're really not sure what kind of parameterized type
            // this object represents...
            throw new UnsupportedOperationException();
        }
        // populate the provided JsonObject with our newly populated JsonElement
        jo.add(field.getName(), je);
    }

    /**
     * Serialize the provided Collection into the provided JsonArray
     * @param jsonArray JsonArray to be populated with the contents of the
     *                  provided Collection, suitably serialized into JSON
     * @param collection Collection to be serialized into JSON and then
     *                   populated in the provided JsonArray
     */
    private void serializeCollection(JsonArray jsonArray, Object collection)
    {
        // get a Collection-typed reference to the provided collection
        Collection c = (Collection) collection;
        // for each element in the collection
        for(Iterator i = c.iterator(); i.hasNext();) {
            // obtain the element
            Object o = i.next();
            // obtain the raw type of the element
            Class rawType = o.getClass();
            // if the raw type is assignable to Collection, then we'll need
            // to recursively serialize this inner collection
            if(Collection.class.isAssignableFrom(rawType)) {
                // create a JsonArray to populate
                JsonArray ja = new JsonArray();
                // recursively serialize the inner collection into
                // the inner JsonArray that we just created
                serializeCollection(ja, o);
                // add the inner JsonArray to the outer JsonArray
                jsonArray.add(ja);
            }
            // otherwise, if the raw type is assignable to Map, then we'll
            // need to recursively serialize this inner map
            else if(Map.class.isAssignableFrom(rawType)) {
                // create a JsonObject to populate
                JsonObject jo = new JsonObject();
                // recursively serialize the inner map into the
                // the JsonObject that we just created
                serializeMap(jo, o);
                // add the inner JsonObject to the outer JsonArray
                jsonArray.add(jo);
            }
            // otherwise, if the raw type is a complex object that itself
            // requires serialization
            else if(aryaSerializer.isSaveRequired(o.getClass())) {
                // ask Arya to serialize the object
                arya.save(o);
                // obtain the UUID identity of the saved object
                UUID id = arya.getIdentity(o);
                // convert the UUID to JSON and populate the JsonArray
                jsonArray.add(new JsonPrimitive(id.toString()));
            }
            // otherwise, the raw type is a simple object that can be directly
            // represented in JSON
            else {
                // TODO: replace this "componentType" with "rawType" above;
                //       they should be exactly the same reference?
                Class componentType = o.getClass();
                // TODO: Refactor this huge if-else block into something
                //       a little nicer; perhaps FieldDeserializer could
                //       require a little more functionality and
                //       EnumFieldDeserializer could provide the correct
                //       value by querying on the componentType
                if(componentType.equals(boolean.class)) { jsonArray.add(new JsonPrimitive((Boolean)o)); }
                else if(componentType.equals(Boolean.class)) { jsonArray.add(new JsonPrimitive((Boolean)o)); }
                else if(componentType.equals(byte.class)) { jsonArray.add(new JsonPrimitive((Byte)o)); }
                else if(componentType.equals(Byte.class)) { jsonArray.add(new JsonPrimitive((Byte)o)); }
                else if(componentType.equals(char.class)) { jsonArray.add(new JsonPrimitive((Character)o)); }
                else if(componentType.equals(Character.class)) { jsonArray.add(new JsonPrimitive((Character)o)); }
                else if(componentType.equals(short.class)) { jsonArray.add(new JsonPrimitive((Short)o)); }
                else if(componentType.equals(Short.class)) { jsonArray.add(new JsonPrimitive((Short)o)); }
                else if(componentType.equals(int.class)) { jsonArray.add(new JsonPrimitive((Integer)o)); }
                else if(componentType.equals(Integer.class)) { jsonArray.add(new JsonPrimitive((Integer)o)); }
                else if(componentType.equals(long.class)) { jsonArray.add(new JsonPrimitive((Long)o)); }
                else if(componentType.equals(Long.class)) { jsonArray.add(new JsonPrimitive((Long)o)); }
                else if(componentType.equals(float.class)) { jsonArray.add(new JsonPrimitive((Float)o)); }
                else if(componentType.equals(Float.class)) { jsonArray.add(new JsonPrimitive((Float)o)); }
                else if(componentType.equals(double.class)) { jsonArray.add(new JsonPrimitive((Double)o)); }
                else if(componentType.equals(Double.class)) { jsonArray.add(new JsonPrimitive((Double)o)); }
                else if(componentType.equals(Date.class)) {
                    Date d = (Date) o;
                    if(d != null) {
                        jsonArray.add(new JsonPrimitive(d.getTime()));
                    }
                }
                else if(componentType.equals(String.class)) {
                    String s = (String) o;
                    if(s != null) {
                        jsonArray.add(new JsonPrimitive(s));
                    }
                }
                else if(componentType.equals(UUID.class)) {
                    UUID u = (UUID) o;
                    if(u != null) {
                        jsonArray.add(new JsonPrimitive(u.toString()));
                    }
                }
            }
        }
    }

    /**
     * Serialize the provided Map into the provided JsonObject
     * @param jo JsonObject to be populated with the contents of the
     *           provided Map serialized into JSON
     * @param map Map to be serialized into JSON and then populated
     *            into the provided JsonObject
     */
    private void serializeMap(JsonObject jo, Object map)
    {
        // obtain a Map-typed reference to the provided map
        Map m = (Map) map;
        // obtain the set of keys for the map
        Set s = m.keySet();
        // for each key in the map
        for(Object key : s) {
            // convert the map key into a String that can be used as a
            // map key in a JsonObject
            String mapKey = toMapKey(key);
            // obtain the value mapped to the key and convert it to a
            // JsonElement that can be mapped in the JsonObject
            JsonElement mapValue = toMapValue(m.get(key));
            // map the JSON key to the JsonElement in the JsonObject
            jo.add(mapKey, mapValue);
        }
    }
    
    /**
     * Convert the provided object into a String format suitable as a key
     * in a JSON object.
     * @param o Object to be converted into a JSON object key
     * @return Object as a JSON object key String
     */
    private String toMapKey(Object o)
    {
        // if this object is a complex type that also requires serialization
        if(aryaSerializer.isSaveRequired(o.getClass())) {
            // ask Arya to serialize the object
            arya.save(o);
            // obtain the UUID identity of the object
            UUID id = arya.getIdentity(o);
            // return the UUID (in String form) as the JSON object key
            return id.toString();
        }
        // otherwise, if this is a simple object that can be represented
        // directly in JSON format
        else {
            // obtain the type of the provided object
            Class componentType = o.getClass();
            // TODO: Refactor this huge if-else block into something
            //       a little nicer; perhaps FieldDeserializer could
            //       require a little more functionality and
            //       EnumFieldDeserializer could provide the correct
            //       value by querying on the componentType
            if(componentType.equals(boolean.class)) { return String.valueOf((Boolean)o); }
            else if(componentType.equals(Boolean.class)) { return String.valueOf((Boolean)o); }
            else if(componentType.equals(byte.class)) { return String.valueOf((Byte)o); }
            else if(componentType.equals(Byte.class)) { return String.valueOf((Byte)o); }
            else if(componentType.equals(char.class)) { return String.valueOf((Character)o); }
            else if(componentType.equals(Character.class)) { return String.valueOf((Character)o); }
            else if(componentType.equals(short.class)) { return String.valueOf((Short)o); }
            else if(componentType.equals(Short.class)) { return String.valueOf((Short)o); }
            else if(componentType.equals(int.class)) { return String.valueOf((Integer)o); }
            else if(componentType.equals(Integer.class)) { return String.valueOf((Integer)o); }
            else if(componentType.equals(long.class)) { return String.valueOf((Long)o); }
            else if(componentType.equals(Long.class)) { return String.valueOf((Long)o); }
            else if(componentType.equals(float.class)) { return String.valueOf((Float)o); }
            else if(componentType.equals(Float.class)) { return String.valueOf((Float)o); }
            else if(componentType.equals(double.class)) { return String.valueOf((Double)o); }
            else if(componentType.equals(Double.class)) { return String.valueOf((Double)o); }
            else if(componentType.equals(Date.class)) { return String.valueOf(((Date)o).getTime()); }
            else if(componentType.equals(String.class)) { return (String)o; }
            else if(componentType.equals(UUID.class)) { return ((UUID)o).toString(); }
        }
        // we weren't able to represent the provided object as a JSON object key
        throw new UnsupportedOperationException();
    }

    /**
     * Convert the provided object into a JsonElement suitable for a value
     * in a JSON object.
     * @param o Object to be serialized into a JsonElement
     * @return JsonElement that represents the provided Object
     */
    private JsonElement toMapValue(Object o)
    {
        // obtain the type of the provided object
        Class rawType = o.getClass();
        // if the type is assignable to Collection
        if(Collection.class.isAssignableFrom(rawType)) {
            // create an inner JsonArray to populate
            JsonArray ja = new JsonArray();
            // serialize the collection into the JsonArray
            serializeCollection(ja, o);
            // return the JsonArray as the map value
            return ja;
        }
        // otherwise, if the type is assignable to Map
        else if(Map.class.isAssignableFrom(rawType)) {
            // create an inner JsonObject to populate
            JsonObject jo = new JsonObject();
            // serialize the map into the JsonObject
            serializeMap(jo, o);
            // return the JsonObject as the map value
            return jo;
        }
        // otherwise, if the type is a complex object that requires
        // serialization of its own
        else if(aryaSerializer.isSaveRequired(o.getClass())) {
            // ask Arya to serialize the object
            arya.save(o);
            // obtain the UUID identity of the saved object
            UUID id = arya.getIdentity(o);
            // return the UUID wrapped in a JsonPrimitive as the map value
            return new JsonPrimitive(id.toString());
        } 
        // otherwise, the type is a simple type that can be represented
        // directly in the JSON
        else {
            // TODO: replace this "componentType" with "rawType" above;
            //       they should be exactly the same reference?
            Class componentType = o.getClass();
            // TODO: Refactor this huge if-else block into something
            //       a little nicer; perhaps FieldDeserializer could
            //       require a little more functionality and
            //       EnumFieldDeserializer could provide the correct
            //       value by querying on the componentType
            if(componentType.equals(boolean.class)) { return new JsonPrimitive((Boolean)o); }
            else if(componentType.equals(Boolean.class)) { return new JsonPrimitive((Boolean)o); }
            else if(componentType.equals(byte.class)) { return new JsonPrimitive((Byte)o); }
            else if(componentType.equals(Byte.class)) { return new JsonPrimitive((Byte)o); }
            else if(componentType.equals(char.class)) { return new JsonPrimitive((Character)o); }
            else if(componentType.equals(Character.class)) { return new JsonPrimitive((Character)o); }
            else if(componentType.equals(short.class)) { return new JsonPrimitive((Short)o); }
            else if(componentType.equals(Short.class)) { return new JsonPrimitive((Short)o); }
            else if(componentType.equals(int.class)) { return new JsonPrimitive((Integer)o); }
            else if(componentType.equals(Integer.class)) { return new JsonPrimitive((Integer)o); }
            else if(componentType.equals(long.class)) { return new JsonPrimitive((Long)o); }
            else if(componentType.equals(Long.class)) { return new JsonPrimitive((Long)o); }
            else if(componentType.equals(float.class)) { return new JsonPrimitive((Float)o); }
            else if(componentType.equals(Float.class)) { return new JsonPrimitive((Float)o); }
            else if(componentType.equals(double.class)) { return new JsonPrimitive((Double)o); }
            else if(componentType.equals(Double.class)) { return new JsonPrimitive((Double)o); }
            else if(componentType.equals(Date.class)) { return new JsonPrimitive(((Date)o).getTime()); }
            else if(componentType.equals(String.class)) { return new JsonPrimitive((String)o); }
            else if(componentType.equals(UUID.class)) { return new JsonPrimitive(((UUID)o).toString()); }
        }
        // we weren't able to represent the provided object as a JsonElement
        throw new UnsupportedOperationException();
    }

    /**
     * Arya reference to do further serialization if needed.
     */
    private Arya arya;
    
    /**
     * AryaSerializer reference to determine if a type is simple and can be
     * represented directly in JSON or if additional serialization is required
     */
    private AryaSerializer aryaSerializer;
}
