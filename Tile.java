package com.eecs285.siegegame;

import java.lang.System.*;
import java.util.*;
import java.awt.*;

public abstract class Tile {
	// Abstract base class for all tile types.
	public final String name;
	public final boolean isPassable;
	public final Coord coord;
	public int owner;
	public Integer income;
	public int infers;
	public Army a;

	Tile(String in_name, boolean in_passable, Coord in_coord){
		// Constructor.
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
	
	public int getGold(){
		return 0;
	}
	
	public abstract Double getSpdFactor();
	
	public abstract Double getInfFactor();
	
	public String getColor(){
		return name;		
	}
	
	public Army getOccupant(){
		return a;
	}
	
	public void setOccupant(Army a_in){
		a = a_in;
	}
	
	public boolean isCity(){
		return name.equalsIgnoreCase("city");	
	}
	
	public boolean isResource(){
		return name.equalsIgnoreCase("resource");	
	}
	
	public void refreshTile(){
		if (a != null)
			a.refreshArmy();
	}
	
	public boolean trainUnitBasic(){
		return false;
	}
	
	public boolean trainUnitAttacker(){
		return false;
	}
	
	public boolean trainUnitRusher(){
		return false;
	}
	
	public boolean trainUnitExplorer(){
		return false;
	}
	
}
