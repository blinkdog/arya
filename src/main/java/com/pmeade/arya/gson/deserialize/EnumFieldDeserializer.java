/*
 * EnumFieldDeserializer.java
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

import com.google.gson.JsonObject;
import java.lang.reflect.Field;

/**
 * EnumFieldDeserializer translates a JSON field to the appropriate value
 * of an enumerated type in the provided field.
 * @author pmeade
 */
public class EnumFieldDeserializer implements FieldDeserializer
{
    /**
     * Request deserialization of a JSON string into an enumerated type in
     * the field in the provided Object.
     * @param jo JsonObject containing the value to be deserialized
     * @param t Object to be populated with the enumerated value
     * @param field Field of the object to be populated (also identifies the
     *              name of the JSON field to be deserialized)
     * @throws IllegalAccessException never thrown (you don't actually believe
     *                                what I just told you, right?)
     */
    public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // if this JsonObject has something to map into the object's field
        if(jo.has(field.getName())) {
            // obtain the string representation of the enumerated type from the JSON
            String enumKey = jo.getAsJsonPrimitive(field.getName()).getAsString();
            // convert the string representation into an enumerated value object
            Object o = Enum.valueOf((Class<Enum>)field.getType(), enumKey);
            // populate the provided object in the provided field with the
            // enumerated value that we obtained
            field.set(t, o);
        }
    }
}
