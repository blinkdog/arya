/*
 * FieldSerializer.java
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
import java.util.Date;

/**
 * FieldSerializer provides the interface for serialization of a field.
 * It also contains several static singletons for handling the serialization
 * of simple types (primitives, String, UUID, Date, etc.)
 * @author pmeade
 */
public interface FieldSerializer
{
    /**
     * Serialize the provided field on the provided object into JSON data, then
     * populate the JSON data on the provided JsonObject.
     * @param jo JsonObject into which the JSON data should be populated
     * @param t Object that should be serialized into JSON
     * @param field Field on the provided object that should be serialized
     * @throws IllegalAccessException 
     */
    public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException;
    
    /**
     * Handler for simple types Boolean and boolean.
     */
    public static final FieldSerializer BOOLEAN = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Boolean b = (Boolean) field.get(t);
            jo.addProperty(field.getName(), b);
        }
    };
    
    /**
     * Handler for simple types Byte and byte.
     */
    public static final FieldSerializer BYTE = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Byte b = (Byte) field.get(t);
            jo.addProperty(field.getName(), b);
        }
    };

    /**
     * Handler for simple types Character and char.
     */
    public static final FieldSerializer CHAR = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Character c = (Character) field.get(t);
            jo.addProperty(field.getName(), c);
        }
    };

    /**
     * Handler for simple types Short and short.
     */
    public static final FieldSerializer SHORT = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Short s = (Short) field.get(t);
            jo.addProperty(field.getName(), s);
        }
    };

    /**
     * Handler for simple types Integer and int.
     */
    public static final FieldSerializer INT = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Integer i = (Integer) field.get(t);
            jo.addProperty(field.getName(), i);
        }
    };

    /**
     * Handler for simple types Long and long.
     */
    public static final FieldSerializer LONG = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Long l = (Long) field.get(t);
            jo.addProperty(field.getName(), l);
        }
    };

    /**
     * Handler for simple types Float and float.
     */
    public static final FieldSerializer FLOAT = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Float f = (Float) field.get(t);
            jo.addProperty(field.getName(), f);
        }
    };

    /**
     * Handler for simple types Double and double.
     */
    public static final FieldSerializer DOUBLE = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Double d = (Double) field.get(t);
            jo.addProperty(field.getName(), d);
        }
    };

    /**
     * Handler for simple type String.
     */
    public static final FieldSerializer STRING = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            String s = (String) field.get(t);
            jo.addProperty(field.getName(), s);
        }
    };

    /**
     * Handler for simple type UUID.
     */
    public static final FieldSerializer UUID = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            java.util.UUID u = (java.util.UUID) field.get(t);
            if(u != null) {
                jo.addProperty(field.getName(), u.toString());
            }
        }
    };

    /**
     * Handler for simple type Date.
     */
    public static final FieldSerializer DATE = new FieldSerializer() {
        public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            Date d = (Date) field.get(t);
            if(d != null) {
                jo.addProperty(field.getName(), d.getTime());
            }
        }
    };
}
