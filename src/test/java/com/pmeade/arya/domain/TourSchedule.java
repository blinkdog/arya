/*
 * TourSchedule.java
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

public class TourSchedule
{
    public TourSchedule() {
        List<String> texas = new ArrayList();
        texas.add("Austin");
        texas.add("Houston");
        texas.add("Lubbock");
        List<String> illinois = new ArrayList();
        illinois.add("Chicago");
        illinois.add("Naperville");
        illinois.add("Rockford");
        List<String> wisconsin = new ArrayList();
        wisconsin.add("Beloit");
        wisconsin.add("Madison");
        wisconsin.add("Milwaukee");
        schedule.put("IL", illinois);
        schedule.put("TX", texas);
        schedule.put("WI", wisconsin);
    }

    public Map<String, List<String>> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, List<String>> schedule) {
        this.schedule = schedule;
    }
    
    private Map<String, List<String>> schedule = new HashMap();
}
