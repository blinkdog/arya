/*
 * GameOfMaps.java
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

package com.pmeade.arya.domain;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class GameOfMaps
{
    private Map<Boolean,String> b2 =
        ImmutableMap.<Boolean,String>builder()
            .put(false,"falsey")
            .put(true,"truey")
            .build();
    private Map<Byte,String> b4 =
        ImmutableMap.<Byte,String>builder()
            .put((byte)0x7f,"seven-eff")
            .put((byte)0x80,"eight-zero")
            .put((byte)0x23,"two-three")
            .put((byte)0xea,"eee-a")
            .build();
    private Map<Character,String> c2 =
        ImmutableMap.<Character,String>builder()
            .put('a',"a")
            .put('b',"bee")
            .put('c',"sea")
            .build();
    private Map<Short,String> s2 =
        ImmutableMap.<Short,String>builder()
            .put((short)100,"one-hundred")
            .put((short)200,"two-hundred")
            .put((short)300,"three-hundred")
            .put((short)-31000,"negative thirty-one thousand")
            .build();
    private Map<Integer,String> i2 =
        ImmutableMap.<Integer,String>builder()
            .put(50,"fifty")
            .put(150,"one-hundred and fifty")
            .put(250,"two-hundred and fifty")
            .put(-350,"negative three-hundred and fifty")
            .build();
    private Map<Long,String> l2 =
        ImmutableMap.<Long,String>builder()
            .put(50L,"fifty")
            .put(150L,"one-hundred and fifty")
            .put(250L,"two-hundred and fifty")
            .put(-350L,"negative three-hundred and fifty")
            .put(-10000000000000L,"a buttload")
            .build();
    private Map<Float,String> f2 =
        ImmutableMap.<Float,String>builder()
            .put((float)0.25,"point two five")
            .put((float)0.50,"point five oh")
            .put((float)5000.0,"five thousand point oh")
            .put((float)235000.0,"two-hundred and thirty-five thousand point oh")
            .build();
    private Map<Double,String> d2 =
        ImmutableMap.<Double,String>builder()
            .put(1000.25,"damn i'm bored")
            .put(1000.50,"really really bored")
            .put(10005000.0,"yep still bored")
            .put(1000235000.0,"only three left to go")
            .build();
    private Map<String,String> s3 =
        ImmutableMap.<String,String>builder()
            .put("Win","When you play the Game Of Thrones")
            .put("Or","You Win or You Die")
            .put("Die","There is no middle ground")
            .build();
    private Map<Date,String> d3 =
        ImmutableMap.<Date,String>builder()
            .put(new Date(), "before")
            .put(new Date(new Date().getTime() + 100L), "during")
            .put(new Date(new Date().getTime() + 500L), "after")
            .build();
    private Map<UUID,String> u1 =
        ImmutableMap.<UUID,String>builder()
            .put(UUID.randomUUID(), "reference #1")
            .put(UUID.randomUUID(), "reference #2")
            .put(UUID.randomUUID(), "reference #3")
            .build();
    
    // ------------------------------------------------------------------------
    
    private Map<String,Boolean> b1 =
        ImmutableMap.<String,Boolean>builder()
            .put("falsey", false)
            .put("truey", true)
            .build();
    private Map<String,Byte> b3 =
        ImmutableMap.<String,Byte>builder()
            .put("seven-eff",(byte)0x7f)
            .put("eight-zero",(byte)0x80)
            .put("two-three",(byte)0x23)
            .put("eee-a",(byte)0xea)
            .build();
    private Map<String,Character> c1 =
        ImmutableMap.<String,Character>builder()
            .put("a",'a')
            .put("bee",'b')
            .put("sea",'c')
            .build();
    private Map<String,Short> s1 =
        ImmutableMap.<String,Short>builder()
            .put("one-hundred",(short)100)
            .put("two-hundred",(short)200)
            .put("three-hundred",(short)300)
            .put("negative thirty-one thousand",(short)-31000)
            .build();
    private Map<String,Integer> i1 =
        ImmutableMap.<String,Integer>builder()
            .put("fifty",50)
            .put("one-hundred and fifty",150)
            .put("two-hundred and fifty",250)
            .put("negative three-hundred and fifty",-350)
            .build();
    private Map<String,Long> l1 =
        ImmutableMap.<String,Long>builder()
            .put("fifty",50L)
            .put("one-hundred and fifty",150L)
            .put("two-hundred and fifty",250L)
            .put("negative three-hundred and fifty",-350L)
            .put("a buttload",-10000000000000L)
            .build();
    private Map<String,Float> f1 =
        ImmutableMap.<String,Float>builder()
            .put("point two five",(float)0.25)
            .put("point five oh",(float)0.50)
            .put("five thousand point oh",(float)5000.0)
            .put("two-hundred and thirty-five thousand point oh",(float)235000.0)
            .build();
    private Map<String,Double> d1 =
        ImmutableMap.<String,Double>builder()
            .put("damn i'm bored",1000.25)
            .put("really really bored",1000.50)
            .put("yep still bored",10005000.0)
            .put("only three left to go",1000235000.0)
            .build();
    private Map<String,String> s4 =
        ImmutableMap.<String,String>builder()
            .put("Win","When you play the Game Of Thrones")
            .put("Or","You Win or You Die")
            .put("Die","There is no middle ground")
            .build();
    private Map<String,Date> d4 =
        ImmutableMap.<String,Date>builder()
            .put("before", new Date())
            .put("during", new Date(new Date().getTime() + 100L))
            .put("after", new Date(new Date().getTime() + 500L))
            .build();
    private Map<String,UUID> u2 =
        ImmutableMap.<String,UUID>builder()
            .put("reference #1", UUID.randomUUID())
            .put("reference #2", UUID.randomUUID())
            .put("reference #3", UUID.randomUUID())
            .build();
}
