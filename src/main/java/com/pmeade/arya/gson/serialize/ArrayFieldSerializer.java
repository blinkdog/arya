/*
 * ArrayFieldSerializer.java
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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pmeade.arya.Arya;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

/**
 * ArrayFieldSerializer is a FieldSerializer that accepts array-typed fields
 * and converts them into JSON arrays.
 * @author pmeade
 */
public class ArrayFieldSerializer implements FieldSerializer
{
    /**
     * Construct an ArrayFieldSerializer.
     * @param arya Arya reference for further serialization (if needed)
     * @param aryaSerializer AryaSerializer to determine if the array's base
     *                       type requires additional serialization, or if
     *                       it can be directly encoded in JSON
     */
    public ArrayFieldSerializer(Arya arya, AryaSerializer aryaSerializer) {
        this.arya = arya;
        this.aryaSerializer = aryaSerializer;
    }

    /**
     * Request that a provided field on a provided object be serialized into
     * a JsonArray and populated in the provided JsonObject.
     * @param jo JsonObject to populate with the serialized JsonArray
     * @param t Object in which the array exists
     * @param field Field that identifies where the array exists in the Object
     * @throws IllegalAccessException 
     */
    public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // determine the Class of the field (which should be an array)
        Class type = field.getType();
        // while type is still an array
        while(type.isArray()) {
            // determine the component type (this is an array OF what?)
            // example: int[][][] -> int[][] -> int[] -> int
            type = type.getComponentType();
        }
        // determine if we need to save whatever the array contains
        // or if we can just write the values directly
        boolean saveRequired = aryaSerializer.isSaveRequired(type);
        // create the JsonArray that we will populate with the array contents
        JsonArray jsonArray = new JsonArray();
        // obtain the Java array from the Java object
        Object array = field.get(t);
        // serialize the array (convert the Java to JSON)
        serializeArray(jsonArray, array, saveRequired);
        // populate the provided JsonObject with our JsonArray, mapping
        // it to the name of the field in the Java object
        jo.add(field.getName(), jsonArray);
    }

    /**
     * Serialize the contents of the provided Java array into the provided
     * JsonArray.
     * @param jsonArray JsonArray into which the contents should be serialized
     * @param array Java array containing data to be serialized
     * @param saveRequired true, if the component type of the array requires
     *                     additional serialization (i.e.: is a complex object
     *                     that itself requires serialization);
     *                     false, if the component type of the array is a simple
     *                     object the can be represented directly in JSON
     */
    private void serializeArray(JsonArray jsonArray, Object array, boolean saveRequired)
    {
        // obtain the component type of the array
        Class componentType = array.getClass().getComponentType();
        // if this component type is also an array (example: int[][] -> int[])
        if(componentType.isArray())
        {
            // for each element of the Java array
            for(int i=0; i<Array.getLength(array); i++) {
                // create an inner JsonArray object to hold the inner data
                JsonArray innerArray = new JsonArray();
                // recursively serialize into the inner array
                serializeArray(innerArray, Array.get(array, i), saveRequired);
                // add the (now populated) inner array to the outer array
                jsonArray.add(innerArray);
            }
        }
        // otherwise, this component type is the base type of the array
        else
        {
            // for each element of the Java array
            for(int i=0; i<Array.getLength(array); i++) {
                // if we are required to serialize the object, because it
                // is more complex than can be directly represented in JSON
                if(saveRequired) {
                    // ask Arya to save the object
                    arya.save(Array.get(array, i));
                    // obtain the UUID identity of the recently saved object
                    UUID id = arya.getIdentity(Array.get(array, i));
                    // add the UUID (in string form) to the JSON array
                    jsonArray.add(new JsonPrimitive(id.toString()));
                } else {
                    // TODO: Refactor this huge if-else block into something
                    //       a little nicer; perhaps FieldSerializer could
                    //       require a little more functionality and
                    //       EnumFieldSerializer could provide the correct
                    //       value by querying on the componentType
                    if(componentType.equals(boolean.class)) { jsonArray.add(new JsonPrimitive(Array.getBoolean(array, i))); }
                    else if(componentType.equals(Boolean.class)) { Boolean[] b = (Boolean[])array; jsonArray.add(new JsonPrimitive(b[i])); }
                    else if(componentType.equals(byte.class)) { jsonArray.add(new JsonPrimitive(Array.getByte(array, i))); }
                    else if(componentType.equals(Byte.class)) { Byte[] b = (Byte[])array; jsonArray.add(new JsonPrimitive(b[i])); }
                    else if(componentType.equals(char.class)) { jsonArray.add(new JsonPrimitive(Array.getChar(array, i))); }
                    else if(componentType.equals(Character.class)) { Character[] c = (Character[])array; jsonArray.add(new JsonPrimitive(c[i])); }
                    else if(componentType.equals(short.class)) { jsonArray.add(new JsonPrimitive(Array.getShort(array, i))); }
                    else if(componentType.equals(Short.class)) { Short[] s = (Short[])array; jsonArray.add(new JsonPrimitive(s[i])); }
                    else if(componentType.equals(int.class)) { jsonArray.add(new JsonPrimitive(Array.getInt(array, i))); }
                    else if(componentType.equals(Integer.class)) { Integer[] it = (Integer[])array; jsonArray.add(new JsonPrimitive(it[i])); }
                    else if(componentType.equals(long.class)) { jsonArray.add(new JsonPrimitive(Array.getLong(array, i))); }
                    else if(componentType.equals(Long.class)) { Long[] l = (Long[])array; jsonArray.add(new JsonPrimitive(l[i])); }
                    else if(componentType.equals(float.class)) { jsonArray.add(new JsonPrimitive(Array.getFloat(array, i))); }
                    else if(componentType.equals(Float.class)) { Float[] f = (Float[])array; jsonArray.add(new JsonPrimitive(f[i])); }
                    else if(componentType.equals(double.class)) { jsonArray.add(new JsonPrimitive(Array.getDouble(array, i))); }
                    else if(componentType.equals(Double.class)) { Double[] d = (Double[])array; jsonArray.add(new JsonPrimitive(d[i])); }
                    else if(componentType.equals(Date.class)) {
                        Date[] d = (Date[])array;
                        if(d[i] != null) {
                            jsonArray.add(new JsonPrimitive(d[i].getTime()));
                        }
                    }
                    else if(componentType.equals(String.class)) {
                        String[] s = (String[]) array;
                        if(s[i] != null) {
                            jsonArray.add(new JsonPrimitive(s[i]));
                        }
                    }
                    else if(componentType.equals(UUID.class)) {
                        UUID[] u = (UUID[]) array;
                        if(u[i] != null) {
                            jsonArray.add(new JsonPrimitive(u[i].toString()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Arya reference for further serialization (if needed).
     */
    private Arya arya;
    
    /**
     * AryaSerializer to determine if the array's base type requires additional
     * serialization, or if it can be directly encoded in JSON.
     */
    private AryaSerializer aryaSerializer;
}
