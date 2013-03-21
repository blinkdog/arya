/*
 * GameOfLists.java
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

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GameOfLists
{
    private List<Boolean> b1 = Lists.newArrayList(true, false, true);
    private List<Byte> b4 = Lists.newArrayList((byte)0x7f, (byte)0x80, (byte)0x23, (byte)0xea);
    private List<Character> c2 = Lists.newArrayList('a', 'b', 'c');
    private List<Short> s2 = Lists.newArrayList((short)100, (short)200, (short)300, (short)-31000);
    private List<Integer> i2 = Lists.newArrayList(50, 150, 250, -350, -500000);
    private List<Long> l2 = Lists.newArrayList(50L, 150L, 250L, -350L, -10000000000000L);
    private List<Float> f2 = Lists.newArrayList((float)0.25, (float)0.50, (float)5000.0, (float)235000.0);
    private List<Double> d2 = Lists.newArrayList(1000.25, 1000.50, 10005000.0, 1000235000.0);
    private List<String> s3 = Lists.newArrayList("Win", "Or", "Die");
    private List<Date> d3 = Lists.newArrayList(new Date(), new Date(), new Date());
    private List<UUID> u1 = Lists.newArrayList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
}
