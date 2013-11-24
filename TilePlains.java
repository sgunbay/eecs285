package eecs285.proj4;

import java.awt.Color;

public class TilePlains extends Tile {
	
	TilePlains(){
		super();
	}
	
	public Double getSpdFactor(){
		return 1.0;
	}
	
	public Double getInfFactor(){
		return 1.0;
	}
	
	public Color getColor(){
		return Color.GREEN.brighter();
	}
}
