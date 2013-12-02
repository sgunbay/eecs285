package com.eecs285.siegegame;

public class TileMountain extends Tile {

	// Mountain tile. Raises movement speed.
	TileMountain(Coord in_coord){
		super("mountain",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 1.3;
	}
	
	public Double getInfFactor(){
		return 1.0;
	}
	
}
