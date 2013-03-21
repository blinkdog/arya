/*
 * TypeTest.java
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

import com.pmeade.arya.domain.Player;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeTest
{
    public TypeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAlwaysSucceed() {
        assertTrue(true);
    }
    
    @Test
    public void testListAssignableFromCollection() {
        assertFalse(List.class.isAssignableFrom(Collection.class));
    }

    @Test
    public void testCollectionAssignableFromList() {
        assertTrue(Collection.class.isAssignableFrom(List.class));
    }
    
    @Test
    public void testBooleanArray() {
        Boolean[] b2 = { true, false, true };
        Object o = b2;
        assertEquals(Boolean[].class, o.getClass());
        assertTrue(o.getClass().isArray());
        assertEquals(3, Array.getLength(o));
        try { assertTrue(Array.getBoolean(o, 0)); fail(); } catch(IllegalArgumentException e) { }
        try { assertFalse(Array.getBoolean(o, 1)); fail(); } catch(IllegalArgumentException e) { }
        try { assertTrue(Array.getBoolean(o, 2)); fail(); } catch(IllegalArgumentException e) { }
    }
    
    @Test
    public void testPlayerClassNames() {
        assertEquals("Player", Player.class.getSimpleName());
        assertEquals("com.pmeade.arya.domain.Player", Player.class.getName());
    }
}
