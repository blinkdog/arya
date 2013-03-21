/*
 * ArrayFieldDeserializer.java
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
import com.google.gson.JsonObject;
import com.pmeade.arya.Arya;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * ArrayFieldDeserializer deserializes JSON arrays into Java arrays. The
 * content of a JsonArray object is converted into a Java array that populates
 * the specified field on the provided object.
 * @author pmeade
 */
public class ArrayFieldDeserializer implements FieldDeserializer
{
    /**
     * Construct an ArrayFieldDeserializer.
     * @param arya allows additional deserialization requests
     * @param aryaDeserializer to determine if the component type of the array
     *                         requires deserialization, or is a simple type
     *                         that can be interpreted directly
     */
    public ArrayFieldDeserializer(Arya arya, AryaDeserializer aryaDeserializer)
    {
        this.arya = arya;
        this.aryaDeserializer = aryaDeserializer;
    }

    /**
     * Request deserialization of a JSON array into the provided Object.
     * @param jo JsonObject containing the JsonArray to be deserialized
     * @param t Object to be populated with the array content
     * @param field Field of the object to be populated (also identifies the
     *              name of the JsonArray to be deserialized)
     * @throws IllegalAccessException never thrown (you don't actually believe
     *                                what I just told you, right?)
     */
    public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // if the provided JsonObject has a key that maps to the field name
        if(jo.has(field.getName())) {
            // get the type of the field (which should be an array)
            Class objType = field.getType();
            // while the type is still an array
            while(objType.isArray()) {
                // determine the component type (this is an array OF what?)
                // example: int[][][] -> int[][] -> int[] -> int
                objType = objType.getComponentType();
            }
            // ask the AryaDeserializer if this is something that needs
            // to be deserialized (i.e.: a complex object) or if it is
            // a simple type that can be interpreted directly
            boolean loadRequired = aryaDeserializer.isLoadRequired(objType);
            // obtain the JsonArray that contains the data
            JsonArray ja = jo.getAsJsonArray(field.getName());
            // populate the field on the provided object with the data
            // deserialized from the JSON representation of the object
            field.set(t, deserializeArray(ja, field.getType(), loadRequired));
        }
    }

    /**
     * Deserialize a JsonArray into a Java array object.
     * @param jsonArray JsonArray containing the JSON data to be deserialized
     * @param objType the type of the array to be deserialized
     * @param loadRequired true, if the base component type is a complex object
     *                     that requires its own deserialization; false, if
     *                     the base type is simple and can be interpreted
     *                     directly from the JSON itself
     * @return Java array containing the objects deserialized from the JSON
     */
    private Object deserializeArray(JsonArray jsonArray, Class objType, boolean loadRequired)
    {
        // get the component type of the array (this is an array OF what?)
        Class componentType = objType.getComponentType();
        // instantiate an instance of the array of the component type
        Object array = Array.newInstance(componentType, jsonArray.size());
        // for each item in the JsonArray data
        for(int i=0; i<jsonArray.size(); i++)
        {
            // define something to hold a reference to the thing we want to
            // put in the array; we'll populate it below
            Object value = null;
            // if the component type is ALSO an array (that is, we've got
            // a multi-dimensional array on our hands)
            if(componentType.isArray())
            {
                // then, recursively deserialize the inner array; the return
                // value (an array of one less dimension) is what goes in the
                // outer multi-dimensional array
                value = deserializeArray(jsonArray.get(i).getAsJsonArray(), componentType, loadRequired);
            }
            // otherwise, this is the base component type (i.e.: no more array
            // dimensions need to be processed, we're at the actual type)
            else
            {
                // if the actual type is a complex object that also requires
                // deserialization
                if(loadRequired)
                {
                    // then, get the UUID identity of the object from the JSON
                    String sUuid = jsonArray.get(i).getAsString();
                    java.util.UUID uuid = java.util.UUID.fromString(sUuid);
                    // ask Arya to load the object, so it can go in the array
                    value = arya.load(uuid, componentType);
                }
                // otherwise, this is a simple type that can be interpreted
                // directly; so we'll just read it from the JSON
                else
                {
                    // TODO: Refactor this huge if-else block into something
                    //       a little nicer; perhaps FieldDeserializer could
                    //       require a little more functionality and
                    //       EnumFieldDeserializer could provide the correct
                    //       value by querying on the componentType
                    if(componentType.equals(boolean.class)) { value = jsonArray.get(i).getAsBoolean(); }
                    else if(componentType.equals(Boolean.class)) { value = jsonArray.get(i).getAsBoolean(); }
                    else if(componentType.equals(byte.class)) { value = jsonArray.get(i).getAsByte(); }
                    else if(componentType.equals(Byte.class)) { value = jsonArray.get(i).getAsByte(); }
                    else if(componentType.equals(char.class)) { value = jsonArray.get(i).getAsCharacter(); }
                    else if(componentType.equals(Character.class)) { value = jsonArray.get(i).getAsCharacter(); }
                    else if(componentType.equals(short.class)) { value = jsonArray.get(i).getAsShort(); }
                    else if(componentType.equals(Short.class)) { value = jsonArray.get(i).getAsShort(); }
                    else if(componentType.equals(int.class)) { value = jsonArray.get(i).getAsInt(); }
                    else if(componentType.equals(Integer.class)) { value = jsonArray.get(i).getAsInt(); }
                    else if(componentType.equals(long.class)) { value = jsonArray.get(i).getAsLong(); }
                    else if(componentType.equals(Long.class)) { value = jsonArray.get(i).getAsLong(); }
                    else if(componentType.equals(float.class)) { value = jsonArray.get(i).getAsFloat(); }
                    else if(componentType.equals(Float.class)) { value = jsonArray.get(i).getAsFloat(); }
                    else if(componentType.equals(double.class)) { value = jsonArray.get(i).getAsDouble(); }
                    else if(componentType.equals(Double.class)) { value = jsonArray.get(i).getAsDouble(); }
                    else if(componentType.equals(Date.class)) { value = new Date(jsonArray.get(i).getAsLong()); }
                    else if(componentType.equals(String.class)) { value = jsonArray.get(i).getAsString(); }
                    else if(componentType.equals(java.util.UUID.class)) { value = java.util.UUID.fromString(jsonArray.get(i).getAsString()); }
                    else { throw new UnsupportedOperationException(); }
                }
            }
            // put the deserialized value object in the Java array
            Array.set(array, i, value);
        }
        // return the Java array to the caller
        return array;
    }

    /**
     * Arya reference allows ArrayFieldDeserializer to make additional
     * deserialization requests. It might do this when provided with an
     * array of complex objects that themselves need to be deserialized.
     */
    private Arya arya;
    
    /**
     * AryaDeserializer allows ArrayFieldDeserializer to determine if the
     * component type of the array is a complex object that requires
     * deserialization.
     */
    private AryaDeserializer aryaDeserializer;
}
