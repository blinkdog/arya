/*
 * CityInfo.java
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

public class CityInfo
{
    public CityInfo() {
        Map<String,String> austin = new HashMap();
        austin.put("population", "123456");
        austin.put("temperature", "45");

        Map<String,String> houston = new HashMap();
        houston.put("population", "234567");
        houston.put("temperature", "32");

        Map<String,String> lubbock = new HashMap();
        houston.put("population", "321234");
        houston.put("temperature", "23");
        
        Map<String, Map<String,String>> texas = new HashMap();
        texas.put("Austin", austin);
        texas.put("Houston", houston);
        texas.put("Lubbock", lubbock);

        Map<String,String> chicago = new HashMap();
        chicago.put("population", "123456");
        chicago.put("temperature", "45");

        Map<String,String> naperville = new HashMap();
        naperville.put("population", "234567");
        naperville.put("temperature", "32");

        Map<String,String> rockford = new HashMap();
        rockford.put("population", "321234");
        rockford.put("temperature", "23");
        
        Map<String, Map<String,String>> illinois = new HashMap();
        illinois.put("Chicago", chicago);
        illinois.put("Naperville", naperville);
        illinois.put("Rockford", rockford);

        Map<String,String> beloit = new HashMap();
        beloit.put("population", "123456");
        beloit.put("temperature", "45");

        Map<String,String> madison = new HashMap();
        madison.put("population", "234567");
        madison.put("temperature", "32");

        Map<String,String> milwaukee = new HashMap();
        milwaukee.put("population", "321234");
        milwaukee.put("temperature", "23");
        
        Map<String, Map<String,String>> wisconsin = new HashMap();
        wisconsin.put("Beloit", beloit);
        wisconsin.put("Madison", madison);
        wisconsin.put("Milwuakee", milwaukee);
        
        infodex.put("IL", illinois);
        infodex.put("TX", texas);
        infodex.put("WI", wisconsin);
    }

    public Map<String, Map<String, Map<String, String>>> getInfodex() {
        return infodex;
    }

    public void setInfodex(Map<String, Map<String, Map<String, String>>> infodex) {
        this.infodex = infodex;
    }
    
    private Map<String, Map<String, Map<String, String>>> infodex = new HashMap();
}
