package com.eecs285.siegegame;

import java.awt.Color;

public class TileMuddy extends Tile {
		
	TileMuddy(Coord in_coord){
		super("Muddy",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.5;
	}
	
	public Double getInfFactor(){
		return 1.0;
	}
	
	public Color getColor(){
		return Color.YELLOW;
	}
}