/*
 * AryaInput.java
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
 * AryaInput is the input provider service interface. Classes wishing to
 * provide JSON to be deserialized into Java objects should implement
 * this interface.
 * @author pmeade
 */
public interface AryaInput
{
    /**
     * Obtain the JSON representation of the object identified by the provided
     * UUID identity.
     * @param uuid UUID identity of the object for which to obtain the JSON
     * @param type Class of the Java object to be deserialized
     * @return JSON representation of the object indicated by the provided UUID
     *         identity, if it can be found in this persistence store. Otherwise
     *         this function should return null.
     */
    public <T> String input(UUID uuid, Class<T> type);
}
