package com.eecs285.siegegame;

import java.lang.System.*;
import java.util.*;
import java.awt.*;

public abstract class Tile {
	
	public final String name;
	public final boolean isPassable;
	public final Coord coord;
	public Integer owner;
	public Integer income;
	public int infers;
	public Army a;
	// Plains.
	// Forest.
	// Mud.
	// Mountain.
	// Water.
	
	// City.
	Tile(String in_name, boolean in_passable, Coord in_coord){
		income = new Integer(0);
		owner = new Integer(-1);
		infers = 0;
		a = null;
		name = in_name;
		isPassable = in_passable;
		coord = in_coord;
	}
	public String toString(){
		return name;
	}
	
	public abstract Double getSpdFactor();
	
	public abstract Double getInfFactor();
	
	public abstract Color getColor();
	
	public void refreshTile(){
		if (a != null && a.owner == Siege.currentPlayer)
			a.refreshArmy();
	}
	
	public Army getOccupant(){
		return a;
	}
	
	public void setOccupant(Army a_in){
		a = a_in;
	}
	
	public boolean isCity(){
		return name.equalsIgnoreCase("City");	
	}
	
	public boolean isResource(){
		return name.equalsIgnoreCase("Resource");	
	}
}
