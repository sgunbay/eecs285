package com.eecs285.siegegame;

public class TileForest extends Tile {
	// Forest tile. Slows movement by a small amount.
	TileForest(Coord in_coord){
		super("forest",true,in_coord);
	}
	
	public Double getSpdFactor(){
		return 0.8;
	}
	
	public Double getInfFactor(){
		return 0.5;
	}

}
