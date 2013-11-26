package com.eecs285.siegegame;

import java.awt.Color;

public class TileForest extends Tile {
	
	TileForest(Coord in_coord){
		super("Forest",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.8;
	}
	
	public Double getInfFactor(){
		return 0.5;
	}
	
	public Color getColor(){
		return Color.GREEN.darker().darker();
	}
}
