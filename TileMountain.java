package com.eecs285.siegegame;

import java.awt.Color;

public class TileMountain extends Tile {

	TileMountain(Coord in_coord){
		super("Mountain",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.5;
	}
	
	public Double getInfFactor(){
		return 1.5;
	}
	
	public Color getColor(){
		return Color.ORANGE;
	}
	
}
