/*
 * AryaDeserializer.java
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

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.pmeade.arya.Arya;
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
 * AryaDeserializer implements deserialization for objects that derive
 * from Object (i.e.: all Java objects). It implements Gson's JsonDeserializer
 * so that Gson will call it when attempting to deserialize JSON to an object.
 * @author pmeade
 */
public class AryaDeserializer implements JsonDeserializer<Object>
{
    /** SLF4J Logging Service */
    private static final Logger log = LoggerFactory.getLogger(AryaDeserializer.class);

    /**
     * Handy map from a Java type to a deserialization object that can
     * deserialize that type.
     */
    private static final Map<Type,FieldDeserializer> typeMap =
        // Guava's ImmutableMap.Builder provides handy syntatic sugar
        new ImmutableMap.Builder<Type,FieldDeserializer>()
            .put(boolean.class,   FieldDeserializer.BOOLEAN)
            .put(Boolean.class,   FieldDeserializer.BOOLEAN)
            .put(byte.class,      FieldDeserializer.BYTE)
            .put(Byte.class,      FieldDeserializer.BYTE)
            .put(char.class,      FieldDeserializer.CHAR)
            .put(Character.class, FieldDeserializer.CHAR)
            .put(short.class,     FieldDeserializer.SHORT)
            .put(Short.class,     FieldDeserializer.SHORT)
            .put(int.class,       FieldDeserializer.INT)
            .put(Integer.class,   FieldDeserializer.INT)
            .put(long.class,      FieldDeserializer.LONG)
            .put(Long.class,      FieldDeserializer.LONG)
            .put(float.class,     FieldDeserializer.FLOAT)
            .put(Float.class,     FieldDeserializer.FLOAT)
            .put(double.class,    FieldDeserializer.DOUBLE)
            .put(Double.class,    FieldDeserializer.DOUBLE)
            .put(Date.class,      FieldDeserializer.DATE)
            .put(String.class,    FieldDeserializer.STRING)
            .put(UUID.class,      FieldDeserializer.UUID)
            .build();

    /**
     * Construct an AryaDeserializer for registration with Gson.
     * @param arya Arya reference to populate during deserialization
     */
    public AryaDeserializer(Arya arya) {
        this.arya = arya;
        this.arrayFieldDeserializer = new ArrayFieldDeserializer(arya, this);
        this.enumFieldDeserializer = new EnumFieldDeserializer();
        this.objectFieldDeserializer = new ObjectFieldDeserializer(arya);
        this.parameterizedFieldDeserializer = new ParameterizedFieldDeserializer(arya, this);
    }

    /**
     * Gson deserialization handler method. This method is called when Gson
     * wants to convert JSON into a Java Object.
     * @param je JsonObject corresponding to the JSON to deserialize
     * @param type Class of the object to be deserialized
     * @param jdc JsonDeserializationContext -- unknown and unused
     * @return a Java object that was deserialized from the provided JSON
     * @throws JsonParseException is thrown when something funky goes wrong
     *         with the expected JSON structure
     */
    @Override // implements JsonDeserializer<Object>
    public Object deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        // convert the provided JsonElement reference to a JsonObject reference
        JsonObject jo = je.getAsJsonObject();
        // set the default type for deserialization
        Class typeClass = (Class) type;
        // if the provided JSON has a type encoded by Arya
        if(jo.has(Arya.TYPE)) {
            // obtain the name of the class encoded by Arya
            String typeClassName = jo.get(Arya.TYPE).getAsString();
            try {
                // obtain the Class object for the provided name
                typeClass = Class.forName(typeClassName);
            } catch(ClassNotFoundException e) {
                // if we can't find that Class ... oops
                log.error("Unable to find class " + typeClassName + ":", e);
            }
        }
        // create an instance of the class to be deserialized
        Object o = null;
        try {
            // attempt to instantiate the object
            o = typeClass.newInstance();
        } catch (InstantiationException ex) {
            // if it doesn't have a no-arg constructor ... oops
            log.error("Unable to instantiate type " + type + ":", ex);
        } catch (IllegalAccessException ex) {
            // if the no-arg constructor isn't accessible
            log.error("Unable to instantiate type " + type + ":", ex);
        }
        // arya.pop() --> obtain the UUID identity of the object to be
        //                deserialized
        // arya.populate() --> tell Arya to add this object to its map
        //                     with the provided UUID identity
        arya.populate(arya.pop(), o);
        // while we haven't reached the top of the inheritance hierarchy 
        while(typeClass != Object.class)
        {
            // obtain all of the fields for the current class
            Field[] fields = typeClass.getDeclaredFields();
            // for each field of the current class
            for(Field field : fields) {
                // skip all fields that are final, static, transient, or volatile
                if(Modifier.isFinal(field.getModifiers())) continue;
                if(Modifier.isStatic(field.getModifiers())) continue;
                if(Modifier.isTransient(field.getModifiers())) continue;
                if(Modifier.isVolatile(field.getModifiers())) continue;
                // make sure the field is accessible to our deserializers
                field.setAccessible(true);
                // attempt to obtain an appropriate deserialization object
                // from our type map
                FieldDeserializer fieldDeserializer = typeMap.get(field.getType());
                // if the field is a parameterized type; that is, it
                // is like List<X>, Map<Y,Z>, etc.
                Type genericType = field.getGenericType();
                if(genericType instanceof ParameterizedType) {
                    // then we'll use a ParameterizedFieldDeserializer
                    fieldDeserializer = parameterizedFieldDeserializer;
                }
                // otherwise, if the field is an enumerated type
                if(field.getType().isEnum()) {
                    // then we'll use an EnumFieldDeserializer
                    fieldDeserializer = enumFieldDeserializer;
                }
                // otherwise, if the field is an array
                if(field.getType().isArray()) {
                    // then we'll use an ArrayFieldDeserializer
                    fieldDeserializer = arrayFieldDeserializer;
                }
                // if after all of this, we still haven't found an applicable
                // deserializer to deserialize this field
                if(fieldDeserializer == null) {
                    // then we'll use the default ObjectFieldDeserializer
                    fieldDeserializer = objectFieldDeserializer;
                }
                // attempt to deserialize from the JSON to the object's field
                try {
                    fieldDeserializer.deserialize(jo, o, field);
                } catch(IllegalAccessException e) {
                    log.error("Unable to access field '" + field.getName() + "':", e);
                }
            }
            // after we've handled every field in the class, we'll move up
            // inheritance hierarchy to the superclass, so we can get all
            // of those fields as well
            typeClass = typeClass.getSuperclass();
        }
        // return the object to the caller
        return o;
    }

    /**
     * Determine if the provided type must be loaded (deserialized) by Arya,
     * or if it is a simple type that can be handled directly.
     * @param clazz type for which to determine deserialization handling
     * @return true, if the type must be loaded (deserialized) by a call to
     *         Arya.load(); 
     *         false, if the type is a simple type that can be deserialized
     *         directly without further recourse to the Arya object
     */
    public boolean isLoadRequired(Class clazz) {
        return !typeMap.containsKey(clazz);
    }

    /**
     * Arya reference. Populated as objects are deserialized from the
     * JSON provided by Gson.
     */
    private Arya arya;
    
    /**
     * ArrayFieldDeserializer handles the deserialization details of
     * array fields. For example boolean[], Integer[], SomeObject[][], etc.
     */
    private ArrayFieldDeserializer arrayFieldDeserializer;
    
    /**
     * EnumFieldDeserializer handles the deserialization details of
     * enumerated type fields. For example, anything that is a Java "enum"
     * type.
     */
    private EnumFieldDeserializer enumFieldDeserializer;
    
    /**
     * ObjectFieldDeserializer handles the deserialization details of
     * object fields. This deserializer is used by default for any "complex"
     * type. This is a final catch-all for any type that cannot be handled
     * directly, and doesn't have a special structure (parameterized, array,
     * enum, etc.)
     */
    private ObjectFieldDeserializer objectFieldDeserializer;
    
    /**
     * ParameterizedFieldDeserializer handles the deserialization details
     * of parameterized fields. For example, List<V>, Map<K,V>, etc.
     */
    private ParameterizedFieldDeserializer parameterizedFieldDeserializer;
}
