package eecs285.proj4;

import java.awt.Color;

public class TileMountain extends Tile {

	TileMountain(){
		super();
	}
	
	public Double getSpdFactor(){
		return 0.5;
	}
	
	public Double getInfFactor(){
		return 1.5;
	}
	
	public Color getColor(){
		return Color.ORANGE;
	}
	
}
