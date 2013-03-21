/*
 * GsonTest.java
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

import com.google.gson.Gson;
import com.pmeade.arya.domain.CircReferenceA;
import com.pmeade.arya.domain.CircReferenceB;
import com.pmeade.arya.domain.SelfReference;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author pmeade
 */
public class GsonTest
{
    private Gson gson;
    
    public GsonTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        gson = new Gson();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAlwaysSucceed() {
        assertTrue(true);
    }
    
    @Test
    public void testSerializeSelfReference() {
        SelfReference selfReference = new SelfReference();
        assertEquals(selfReference, selfReference.getSelf());

        String json = null;
        try {
            json = gson.toJson(selfReference);
            fail();
        } catch(StackOverflowError e) {
            // expected
        }
        assertNull(json);
    }
    
    @Test
    public void testSerializeCircularReference() {
        CircReferenceA a = new CircReferenceA();
        CircReferenceB b = new CircReferenceB();
        a.setCircReferenceB(b);
        b.setCircReferenceA(a);
        
        assertEquals(a, a.getCircReferenceB().getCircReferenceA());
        assertEquals(b, b.getCircReferenceA().getCircReferenceB());

        String json = null;
        try {
            json = gson.toJson(a);
            fail();
        } catch(StackOverflowError e) {
            // expected
        }
        assertNull(json);
    }
}
