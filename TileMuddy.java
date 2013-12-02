package com.eecs285.siegegame;


public class TileMuddy extends Tile {
		
	TileMuddy(Coord in_coord){
		super("muddy",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.5;
	}
	
	public Double getInfFactor(){
		return 1.0;
	}

}