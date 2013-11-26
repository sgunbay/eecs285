package com.eecs285.siegegame;

import java.awt.Color;

public class TileCity extends Tile{
	
	public static int CITY_INCOME = 10;
	
	private Integer accGold;
	
	TileCity(int in_owner, Coord in_coord){
		super("City",true,in_coord);
		owner = new Integer(in_owner);
		income = new Integer(CITY_INCOME);
		infers = new Integer(0);
		accGold = new Integer(0);
	}
	
	public Double getSpdFactor(){
		return 1.0;
	}
	
	public Double getInfFactor(){
		return 1.0;
	}
	
	public Color getColor(){		
		if (owner == -1)
			return Color.WHITE;
		return Siege.players[owner].color;
	}
		
	public void updateAccGold(){
		assert(owner != -1);
		if (infers > 0){
			accGold += income;
			return;
		}
		Siege.players[owner].transferAccGold(accGold);
		accGold = 0;
		return;
	}

	public int getGold(){
		if (infers > 0)
			return accGold;
		else
			return Siege.players[owner].getGold();
	}
	
	private boolean trainUnit(Unit u){
		if (u.cost > getGold())
			return false;
		a.addUnit(u);
		Siege.players[owner].subtractGold(u.cost);
		
		// Update map.
		Tile t = Siege.grid.getTile(coord);
		t.setOccupant(a);
		Siege.grid.setTile(coord,t);
		return true;		
	}
	
	public boolean trainUnitBasic(){
		Unit u = new UnitBasic();
		return trainUnit(u);
	}
	
	public boolean trainUnitAttacker(){
		Unit u = new UnitAttacker();
		return trainUnit(u);
	}
	
	public boolean trainUnitRusher(){
		Unit u = new UnitRusher();
		return trainUnit(u);
	}
	
	public boolean trainUnitExplorer(){
		Unit u = new UnitExplorer();
		return trainUnit(u);
	}
	
	public void refreshTile(){
		super.refreshTile();	
		if (owner == Siege.currentPlayer)
			updateAccGold();
	}
	
}
