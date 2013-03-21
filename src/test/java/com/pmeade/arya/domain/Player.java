/*
 * Player.java
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

import java.util.*;

public class Player
{
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public Map<Player, List<Player>> getKnownAssociates() {
        return knownAssociates;
    }

    public void setKnownAssociates(Map<Player, List<Player>> knownAssociates) {
        this.knownAssociates = knownAssociates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }
    
    private Map<Player,List<Player>> knownAssociates = new HashMap();
    private String name;
    private Date dateCreated = new Date();
    private Gender gender = Gender.FEMALE;
    private List<Weapon> weapons = new ArrayList();
}
