/*
 * EnumFieldSerializer.java
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

import com.google.gson.JsonObject;
import java.lang.reflect.Field;

/**
 * EnumFieldSerializer serializes enum fields into JSON.
 * @author pmeade
 */
public class EnumFieldSerializer implements FieldSerializer
{
    /**
     * Serialize the provided enum field in the provided object into JSON,
     * and populate it on the provided JsonObject.
     * @param jo JsonObject to be populated with serialized JSON data
     * @param t Object whose data is to be serialized into JSON
     * @param field Field on the object to be serialized into JSON
     * @throws IllegalAccessException never thrown (you don't actually believe
     *                                what I just told you, right?)
     */
    public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // obtain the enum value from the object's field
        Object o = field.get(t);
        // add the string representation of the enum value to the JSON
        jo.addProperty(field.getName(), o.toString());
    }
}
