package com.eecs285.siegegame;

import java.awt.Color;

public class TileWater extends Tile {

	TileWater(Coord in_coord){
		super("Water",false,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.0;
	}
	
	public Double getInfFactor(){
		return 0.0;
	}
	
	public Color getColor(){
		return Color.BLUE;
	}

}
