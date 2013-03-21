/*
 * MultiModel.java
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

public class MultiModel
{
    public MultiModel()
    {
        polygons = new Polygon[2][2][2];
        for(int i=0; i<polygons.length; i++) {
            for(int j=0; j<polygons[i].length; j++) {
                for(int k=0; k<polygons[i][j].length; k++) {
                    polygons[i][j][k] = new Polygon();
                }
            }
        }
    }

    public Polygon[][][] getPolygons() {
        return polygons;
    }

    public void setPolygons(Polygon[][][] polygons) {
        this.polygons = polygons;
    }
    
    private Polygon[][][] polygons;
}
