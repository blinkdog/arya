/*
 * ThroneProgram3.java
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
import java.util.List;
import java.util.Map;

public class ThroneProgram3
{
    public static final String STARK = "Stark";
    public static final String GREYJOY = "Greyjoy";
    public static final String TYRELL = "Tyrell";
    public static final String LANNISTER = "Lannister";

    public ThroneProgram3() {
        Map stark = new HashMap();
        stark.put("Eddard", STARK);
        stark.put("Catelyn", STARK);
        stark.put("Robb", STARK);
        stark.put("Sansa", STARK);
        stark.put("Arya", STARK);
        stark.put("Brandon", STARK);
        stark.put("Rickon", STARK);

        Map greyjoy = new HashMap();
        greyjoy.put("Balon", GREYJOY);
        greyjoy.put("Asha", GREYJOY);
        greyjoy.put("Theon", GREYJOY);

        Map tyrell = new HashMap();
        tyrell.put("Mace", TYRELL);
        tyrell.put("Margaery", TYRELL);
        tyrell.put("Loras", TYRELL);

        Map lannister = new HashMap();
        lannister.put("Tywin", LANNISTER);
        lannister.put("Cersei", LANNISTER);
        lannister.put("Jamie", LANNISTER);
        lannister.put("Tyrion", LANNISTER);
        lannister.put("Jeoffery", LANNISTER);
        lannister.put("Tommen", LANNISTER);
        lannister.put("Myrcella", LANNISTER);
        
        houses.add(stark);
        houses.add(greyjoy);
        houses.add(tyrell);
        houses.add(lannister);
    }

    public List<Map<String, String>> getHouses() {
        return houses;
    }

    public void setHouses(List<Map<String, String>> houses) {
        this.houses = houses;
    }
    
    private List<Map<String, String>> houses = new ArrayList();
}
