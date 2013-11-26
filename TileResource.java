package com.eecs285.siegegame;

import java.awt.Color;

public class TileResource extends Tile {

	static int SMALL = 10;
	static int MEDIUM = 15;
	static int LARGE = 20;
	
	TileResource(int in_amount, Coord in_coord){
		super("Resource",false,in_coord);
		owner = new Integer(-1);
		income = new Integer(in_amount);
	}
	
	public Double getSpdFactor(){
		return 0.0;
	}
	
	public Double getInfFactor(){
		return 0.0;
	}
	
	public Color getColor(){
		if (owner != -1)
			return Siege.players[owner].color;
		return Color.PINK;
	}

}
