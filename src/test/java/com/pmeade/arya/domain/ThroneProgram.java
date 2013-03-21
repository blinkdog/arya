/*
 * ThroneProgram.java
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

import java.util.HashMap;
import java.util.Map;

public class ThroneProgram
{
    public static final String STARK = "Stark";
    public static final String GREYJOY = "Greyjoy";
    public static final String TYRELL = "Tyrell";
    public static final String LANNISTER = "Lannister";

    public ThroneProgram() {
        names.put("Eddard", STARK);
        names.put("Catelyn", STARK);
        names.put("Robb", STARK);
        names.put("Sansa", STARK);
        names.put("Arya", STARK);
        names.put("Brandon", STARK);
        names.put("Rickon", STARK);
        
        names.put("Balon", GREYJOY);
        names.put("Asha", GREYJOY);
        names.put("Theon", GREYJOY);

        names.put("Mace", TYRELL);
        names.put("Margaery", TYRELL);
        names.put("Loras", TYRELL);
        
        names.put("Tywin", LANNISTER);
        names.put("Cersei", LANNISTER);
        names.put("Jamie", LANNISTER);
        names.put("Tyrion", LANNISTER);
        names.put("Jeoffery", LANNISTER);
        names.put("Tommen", LANNISTER);
        names.put("Myrcella", LANNISTER);
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }
    
    private Map<String, String> names = new HashMap();
}
