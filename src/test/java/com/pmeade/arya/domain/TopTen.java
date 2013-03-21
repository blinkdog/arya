/*
 * TopTen.java
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

public class TopTen
{
    public TopTen() {
        SongInfo harlemShake = new SongInfo();
        harlemShake.setArtist("Baauer");
        harlemShake.setTitle("Harlem Shake");
        harlemShake.setWeeksOnChart(2);

        SongInfo thriftShop = new SongInfo();
        thriftShop.setArtist("Macklemore & Ryan Lewis");
        thriftShop.setTitle("Thrift Shop");
        thriftShop.setWeeksOnChart(21);

        SongInfo whenIWas = new SongInfo();
        whenIWas.setArtist("Bruno Mars");
        whenIWas.setTitle("When I Was Your Man");
        whenIWas.setWeeksOnChart(10);

        SongInfo iKnewYouWere = new SongInfo();
        iKnewYouWere.setArtist("Taylor Swift");
        iKnewYouWere.setTitle("I Knew You Were Trouble");
        iKnewYouWere.setWeeksOnChart(19);

        SongInfo screamAndShout = new SongInfo();
        screamAndShout.setArtist("will.i.am & Britney Spears");
        screamAndShout.setTitle("Scream & Shout");
        screamAndShout.setWeeksOnChart(13);

        SongInfo startedFrom = new SongInfo();
        startedFrom.setArtist("Drake");
        startedFrom.setTitle("Started From The Bottom");
        startedFrom.setWeeksOnChart(3);

        SongInfo stay = new SongInfo();
        stay.setArtist("Rihanna");
        stay.setTitle("Stay");
        stay.setWeeksOnChart(3);

        SongInfo suitAndTie = new SongInfo();
        suitAndTie.setArtist("Justin Timberlake");
        suitAndTie.setTitle("The 20/20 Experience");
        suitAndTie.setWeeksOnChart(7);
        
        SongInfo lockedOut = new SongInfo();
        lockedOut.setArtist("Bruno Mars");
        lockedOut.setTitle("Locked Out Of Heaven");
        lockedOut.setWeeksOnChart(21);

        SongInfo loveMe = new SongInfo();
        loveMe.setArtist("Lil Wayne");
        loveMe.setTitle("Love Me");
        loveMe.setWeeksOnChart(6);
        
        billboard.put(Integer.valueOf( 1), harlemShake);
        billboard.put(Integer.valueOf( 2), thriftShop);
        billboard.put(Integer.valueOf( 3), whenIWas);
        billboard.put(Integer.valueOf( 4), iKnewYouWere);
        billboard.put(Integer.valueOf( 5), screamAndShout);
        billboard.put(Integer.valueOf( 6), startedFrom);
        billboard.put(Integer.valueOf( 7), stay);
        billboard.put(Integer.valueOf( 8), suitAndTie);
        billboard.put(Integer.valueOf( 9), lockedOut);
        billboard.put(Integer.valueOf(10), loveMe);
    }

    public Map<Integer, SongInfo> getBillboard() {
        return billboard;
    }

    public void setBillboard(Map<Integer, SongInfo> billboard) {
        this.billboard = billboard;
    }
    
    private Map<Integer,SongInfo> billboard = new HashMap();
}
