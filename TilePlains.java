package com.eecs285.siegegame;

import java.awt.Color;

public class TilePlains extends Tile {
	
	TilePlains(Coord in_coord){
		super("Plains",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 1.0;
	}
	
	public Double getInfFactor(){
		return 1.0;
	}
	
	public Color getColor(){
		return Color.GREEN.darker();
	}
}
