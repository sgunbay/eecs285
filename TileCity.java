package com.eecs285.siegegame;

public class TileCity extends Tile{
	
	public static int CITY_INCOME = 10;
	
	private Integer accGold;
	
	TileCity(int in_owner, Coord in_coord){
		super("city",true,in_coord);
		owner = new Integer(in_owner);
		income = new Integer(CITY_INCOME);
		infers = new Integer(0);
		accGold = new Integer(0);
		if (owner != -1){
			a = new Army(in_owner,coord);	
		}
	}
	
	public String getColor(){
		if (owner != -1)
			return "player" + owner + "color";	
		return name + "Default";
	}
	
	public Double getSpdFactor(){
		return 1.0;
	}
	
	public Double getInfFactor(){
		return 1.0;
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
	// Returns disposable income.
		if (infers > 0)
			return accGold;
		else
			return Siege.players[owner].getGold();
	}
	
	private boolean trainUnit(Unit u){
		if (u.cost > getGold() || a.isFull())
			return false;
		a.addUnit(u);
		if (infers == 0)
			Siege.players[owner].subtractGold(u.cost);
		else
			accGold -= u.cost;
		
		// Update map.
		Siege.grid.setOccupantAt(coord,a);
		Siege.mainFrame.printMyCityPanel(Siege.grid.getTile(coord));
		return true;		
	}
	
	public boolean trainUnitBasic(){
		return trainUnit(new UnitBasic());
	}
	
	public boolean trainUnitAttacker(){
		return trainUnit(new UnitAttacker());
	}
	
	public boolean trainUnitRusher(){
		return trainUnit(new UnitRusher());
	}
	
	public boolean trainUnitExplorer(){
		return trainUnit(new UnitExplorer());
	}
	
	public void refreshTile(){
		super.refreshTile();
		if (owner == Siege.currentPlayer)
			updateAccGold();
	}
	
}
