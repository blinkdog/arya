/*
 * AryaOutput.java
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

package com.pmeade.arya;

import java.util.UUID;

/**
 * AryaOutput is the output listener service interface. Arya will call the
 * output() method on registered listeners. Classes wishing to receive
 * serialization events from Arya should implement this interface.
 * @author pmeade
 */
public interface AryaOutput
{
    /**
     * Notify about an Arya serialization event. This method is called when
     * Arya is asked to serialize a Java obejct into JSON format.
     * @param t Object that Arya serialized into JSON format
     * @param uuid UUID identity of the Object that was serialized
     * @param json JSON representation of the serialized Object
     */
    public <T> void output(T t, UUID uuid, String json);
}
