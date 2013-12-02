package com.eecs285.siegegame;

public class TileWater extends Tile {

	TileWater(Coord in_coord){
		super("water",false,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.0;
	}
	
	public Double getInfFactor(){
		return 0.0;
	}

}
