package com.eecs285.siegegame;

public class TilePlains extends Tile {

	// Plains tile. Default movement speed.
	TilePlains(Coord in_coord){
		super("plains",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 1.0;
	}
	
	public Double getInfFactor(){
		return 1.0;
	}	
}
