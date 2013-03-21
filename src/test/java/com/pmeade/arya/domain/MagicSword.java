/*
 * MagicSword.java
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

public class MagicSword extends Sword
{
    public int getDmgBonus() {
        return dmgBonus;
    }

    public void setDmgBonus(int dmgBonus) {
        this.dmgBonus = dmgBonus;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    public String getSpecialDesc() {
        return specialDesc;
    }

    public void setSpecialDesc(String specialDesc) {
        this.specialDesc = specialDesc;
    }

    public int getToHitBonus() {
        return toHitBonus;
    }

    public void setToHitBonus(int toHitBonus) {
        this.toHitBonus = toHitBonus;
    }
    
    private int toHitBonus;
    private int dmgBonus;
    private String specialDesc;
    private String epicName;
}
