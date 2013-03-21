/*
 * FieldDeserializer.java
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
import java.util.Date;

/**
 * FieldDeserializer provides the interface for deserialization of a field.
 * It also contains several static singletons for handling the deserialization
 * of simple types (primitives, String, UUID, Date, etc.)
 * @author pmeade
 */
public interface FieldDeserializer
{
    /**
     * Use the provided JSON to deserialize the provided field into the
     * provided object.
     * @param jo the JSON, as wrapped into objects by Gson
     * @param t the Object in which the field is to be deserialized
     * @param field the Field that will be deserialized
     * @throws IllegalAccessException 
     */
    public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException;

    /**
     * Handler for simple types Boolean and boolean.
     */
    public static final FieldDeserializer BOOLEAN = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Boolean b = jo.getAsJsonPrimitive(field.getName()).getAsBoolean();
                field.setBoolean(t, b);
            }
        }
    };
    
    /**
     * Handler for simple types Byte and byte.
     */
    public static final FieldDeserializer BYTE = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Byte b = jo.getAsJsonPrimitive(field.getName()).getAsByte();
                field.setByte(t, b);
            }
        }
    };

    /**
     * Handler for simple types Character and char.
     */
    public static final FieldDeserializer CHAR = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Character c = jo.getAsJsonPrimitive(field.getName()).getAsCharacter();
                field.setChar(t, c);
            }
        }
    };

    /**
     * Handler for simple types Short and short.
     */
    public static final FieldDeserializer SHORT = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Short s = jo.getAsJsonPrimitive(field.getName()).getAsShort();
                field.setShort(t, s);
            }
        }
    };

    /**
     * Handler for simple types Integer and int.
     */
    public static final FieldDeserializer INT = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Integer i = jo.getAsJsonPrimitive(field.getName()).getAsInt();
                field.setInt(t, i);
            }
        }
    };

    /**
     * Handler for simple types Long and long.
     */
    public static final FieldDeserializer LONG = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Long l = jo.getAsJsonPrimitive(field.getName()).getAsLong();
                field.setLong(t, l);
            }
        }
    };

    /**
     * Handler for simple types Float and float.
     */
    public static final FieldDeserializer FLOAT = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Float f = jo.getAsJsonPrimitive(field.getName()).getAsFloat();
                field.setFloat(t, f);
            }
        }
    };

    /**
     * Handler for simple types Double and double.
     */
    public static final FieldDeserializer DOUBLE = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Double d = jo.getAsJsonPrimitive(field.getName()).getAsDouble();
                field.setDouble(t, d);
            }
        }
    };

    /**
     * Handler for simple type String.
     */
    public static final FieldDeserializer STRING = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                String s = jo.getAsJsonPrimitive(field.getName()).getAsString();
                field.set(t, s);
            }
        }
    };

    /**
     * Handler for simple type UUID.
     */
    public static final FieldDeserializer UUID = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                String s = jo.getAsJsonPrimitive(field.getName()).getAsString();
                java.util.UUID u = java.util.UUID.fromString(s);
                field.set(t, u);
            }
        }
    };

    /**
     * Handler for simple type Date.
     */
    public static final FieldDeserializer DATE = new FieldDeserializer() {
        public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException {
            if(jo.has(field.getName())) {
                Long l = jo.getAsJsonPrimitive(field.getName()).getAsLong();
                Date d = new Date(l);
                field.set(t, d);
            }
        }
    };
}
