/*
 * ObjectFieldDeserializer.java
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
import com.pmeade.arya.Arya;
import java.lang.reflect.Field;

/**
 * ObjectFieldDeserializer deserializes UUID identities into Objects. The
 * object returned after deserialization is populated in the provided field
 * on the provided object.
 * @author pmeade
 */
public class ObjectFieldDeserializer implements FieldDeserializer
{
    /**
     * Construct an ObjectFieldDeserializer.
     * @param arya Arya reference for deserialization of the object field
     */
    public ObjectFieldDeserializer(Arya arya) {
        this.arya = arya;
    }

    /**
     * Request deserialization of a UUID into the provided Object.
     * @param jo JsonObject containing the UUID identity of the object
     * @param t Object to be populated with the deserialized object
     * @param field Field of the object to be populated (also identifies the
     *              name of the UUID in the JsonObject to be deserialized)
     * @throws IllegalAccessException never thrown (you don't actually believe
     *                                what I just told you, right?)
     */
    public void deserialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // if the provided JsonObject has a UUID mapped to the field name
        if(jo.has(field.getName())) {
            // obtain the UUID identity of the obejct (in String form)
            String sUuid = jo.getAsJsonPrimitive(field.getName()).getAsString();
            // convert the String form into an actual UUID object
            java.util.UUID uuid = java.util.UUID.fromString(sUuid);
            // use the UUID to request that Arya deserialize the complex object
            Object o = arya.load(uuid, field.getType());
            // set the deserialized object in the provided field on the
            // provided object
            field.set(t, o);
        }
    }

    /**
     * Arya parent reference. Allows ObjectFieldDeserializer to make a call
     * to Arya.load() to request the object needed to populate the provided
     * object.
     */
    private Arya arya;
}
