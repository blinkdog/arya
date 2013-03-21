/*
 * ConcreteTypes.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConcreteTypes
{
    public ConcreteTypes() {
        intList = new ArrayList(); intList.add(2); intList.add(4); intList.add(6);
        intArrayList = new ArrayList(); intArrayList.add(2); intArrayList.add(4); intArrayList.add(6);
        intLinkedList = new LinkedList(); intLinkedList.add(2); intLinkedList.add(4); intLinkedList.add(6);

        intSet = new HashSet(); intSet.add(2); intSet.add(4); intSet.add(6);
        intSortedSet = new TreeSet(); intSortedSet.add(2); intSortedSet.add(4); intSortedSet.add(6);
        intHashSet = new HashSet(); intHashSet.add(2); intHashSet.add(4); intHashSet.add(6);
        intTreeSet = new TreeSet(); intTreeSet.add(2); intTreeSet.add(4); intTreeSet.add(6);

        intMap = new HashMap(); intMap.put("two",2); intMap.put("four",4); intMap.put("six",6);
        intNavigableMap = new TreeMap(); intNavigableMap.put("two",2); intNavigableMap.put("four",4); intNavigableMap.put("six",6);
        intSortedMap = new TreeMap(); intSortedMap.put("two",2); intSortedMap.put("four",4); intSortedMap.put("six",6);
        intHashMap = new HashMap(); intHashMap.put("two",2); intHashMap.put("four",4); intHashMap.put("six",6);
        intTreeMap = new TreeMap(); intTreeMap.put("two",2); intTreeMap.put("four",4); intTreeMap.put("six",6);
    }

    private List<Integer> intList;
    private ArrayList<Integer> intArrayList;
    private LinkedList<Integer> intLinkedList;

    private Set<Integer> intSet;
    private SortedSet<Integer> intSortedSet;
    private HashSet<Integer> intHashSet;
    private TreeSet<Integer> intTreeSet;

    private Map<String,Integer> intMap;
    private NavigableMap<String,Integer> intNavigableMap;
    private SortedMap<String,Integer> intSortedMap;
    private HashMap<String,Integer> intHashMap;
    private TreeMap<String,Integer> intTreeMap;
}
