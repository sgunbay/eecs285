package eecs285.proj4;

import java.awt.Color;

public class TileMuddy extends Tile {
		
	TileMuddy(){
		super();
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