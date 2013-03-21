/*
 * ThroneProgram2.java
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
import java.util.List;

public class ThroneProgram2
{
    public static final String STARK = "Stark";
    public static final String GREYJOY = "Greyjoy";
    public static final String TYRELL = "Tyrell";
    public static final String LANNISTER = "Lannister";

    public ThroneProgram2() {
        List<String> stark = new ArrayList();
        stark.add("Eddard");
        stark.add("Catelyn");
        stark.add("Robb");
        stark.add("Sansa");
        stark.add("Arya");
        stark.add("Brandon");
        stark.add("Rickon");
        
        List<String> greyjoy = new ArrayList();
        greyjoy.add("Balon");
        greyjoy.add("Asha");
        greyjoy.add("Theon");

        List<String> tyrell = new ArrayList();
        tyrell.add("Mace");
        tyrell.add("Margaery");
        tyrell.add("Loras");
        
        List<String> lannister = new ArrayList();
        lannister.add("Tywin");
        lannister.add("Cersei");
        lannister.add("Jamie");
        lannister.add("Tyrion");
        lannister.add("Jeoffery");
        lannister.add("Tommen");
        lannister.add("Myrcella");
        
        houses.add(stark);
        houses.add(greyjoy);
        houses.add(tyrell);
        houses.add(lannister);
    }

    public List<List<String>> getHouses() {
        return houses;
    }

    public void setHouses(List<List<String>> houses) {
        this.houses = houses;
    }
    
    private List<List<String>> houses = new ArrayList();
}
