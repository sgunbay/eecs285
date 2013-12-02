package com.eecs285.siegegame;

public class TileMountain extends Tile {

	TileMountain(Coord in_coord){
		super("mountain",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.5;
	}
	
	public Double getInfFactor(){
		return 1.5;
	}
	
}
