package eecs285.proj4;

import java.awt.Color;

public class TileForest extends Tile {
	
	TileForest(){
		super();
	}
	
	public Double getSpdFactor(){
		return 0.8;
	}
	
	public Double getInfFactor(){
		return 0.5;
	}
	
	public Color getColor(){
		return Color.GREEN.darker();
	}
}
