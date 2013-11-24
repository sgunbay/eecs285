package eecs285.proj4;

import java.lang.System.*;
import java.util.*;
import java.awt.*;

public abstract class Tile {
	
	Army a;
	// Plains.
	// Forest.
	// Mud.
	// Mountain.
	// Water.
	
	// City.
	Tile(){
		a = null;
	}
	
	public abstract Double getSpdFactor();
	
	public abstract Double getInfFactor();
	
	public abstract Color getColor();
	
	public Army getOccupant(){
		return a;
	}
	
	public void setOccupant(Army a_in){
		a = a_in;
	}
	
}
