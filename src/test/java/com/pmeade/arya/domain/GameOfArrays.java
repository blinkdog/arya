/*
 * GameOfArrays.java
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

import java.util.Date;
import java.util.UUID;

public class GameOfArrays
{
    private boolean[] b1 = { true, false, true};
    private Boolean[] b2 = { true, false, true};
    private byte[] b3 = { 0x7f, (byte)0x80, 0x23, (byte)0xea };
    private Byte[] b4 = { 0x7f, (byte)0x80, 0x23, (byte)0xea };
    private char[] c1 = { 'a', 'b', 'c' };
    private Character[] c2 = { 'a', 'b', 'c' };
    private short[] s1 = { 100, 200, 300, -31000 };
    private Short[] s2 = { 100, 200, 300, -31000 };
    private int[] i1 = { 50, 150, 250, -350, -500000 };
    private Integer[] i2 = { 50, 150, 250, -350, -500000 };
    private long[] l1 = { 50L, 150L, 250, -350, -10000000000000L };
    private Long[] l2 = { 50L, 150L, 250L, -350L, -10000000000000L };
    private float[] f1 = { (float)0.25, (float)0.50, (float)5000.0, (float)235000.0 };
    private Float[] f2 = { (float)0.25, (float)0.50, (float)5000.0, (float)235000.0 };
    private double[] d1 = { 1000.25, 1000.50, 10005000.0, 1000235000.0 };
    private Double[] d2 = { 1000.25, 1000.50, 10005000.0, 1000235000.0 };
    private String[] s3 = { "Win", "Or", "Die" };
    private Date[] d3 = { new Date(), new Date(), new Date() };
    private UUID[] u1 = { UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID() };
}
