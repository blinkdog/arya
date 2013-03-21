/*
 * ObjectFieldSerializer.java
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
import com.pmeade.arya.Arya;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * ObjectFieldSerializer serializes provided Object fields. The object itself
 * is recursively serialized by Arya. The object's UUID identity is then stored
 * in the provided JsonObject.
 * @author pmeade
 */
public class ObjectFieldSerializer implements FieldSerializer
{
    /**
     * Construct an ObjectFieldSerializer.
     * @param arya Arya reference, so that the object in the field can be
     *             recursively serialized by Arya
     */
    public ObjectFieldSerializer(Arya arya) {
        this.arya = arya;
    }

    /**
     * Serialize the provided field in the provided Object to JSON, then
     * populate the JSON on the provided JsonObject.
     * @param jo JsonObject on which to populate the UUID identity of the object
     * @param t Object that contains the field to be serialized
     * @param field Field in the provided object to be serialized to JSON
     * @throws IllegalAccessException 
     */
    public void serialize(JsonObject jo, Object t, Field field) throws IllegalAccessException
    {
        // obtain the object to be serialized from the field of the provided
        // Java object
        Object o = field.get(t);
        // ask Arya to serialize the object for us
        arya.save(o);
        // obtain the UUID identity of the object we just serialized (it will
        // be non-null, because either we found an @Id annotated field with
        // an appropriate UUID, or Arya generated a random UUID identity)
        UUID uuid = arya.getIdentity(o);
        // add the UUID identity (in string form) to the JsonObject
        jo.addProperty(field.getName(), uuid.toString());
    }

    /**
     * Arya reference, so that ObjectFieldSerializer can recursively serialize
     * the object in the field.
     */
    private Arya arya;
}
