package com.eecs285.siegegame;

public class Test {
	
	Grid grid;
	
	Test(){
	grid = new Grid(9);
	
	Army a = new Army(0,new Coord(4,4));
	a.addUnit(new UnitAttacker());
	a.addUnit(new UnitAttacker());
	a.addUnit(new UnitAttacker());
	a.addUnit(new UnitAttacker());
	a.addUnit(new UnitAttacker());
	a.addUnit(new UnitAttacker());
	a.addUnit(new UnitAttacker());
	
	grid.setOccupantAt(new Coord(4,4), a);
	
	grid.setTile(new Coord(3,5),new TileWater(new Coord(3,5)));
	grid.setTile(new Coord(4,5),new TileWater(new Coord(4,5)));
	grid.setTile(new Coord(5,5),new TileWater(new Coord(5,5)));
	grid.setTile(new Coord(3,4),new TileWater(new Coord(3,4)));
	grid.setTile(new Coord(5,4),new TileWater(new Coord(5,4)));

	
	a.refreshArmy();
	
	Army b = new Army(1,new Coord(4,3));
	Army c = new Army(1,new Coord(4,2));
	b.addUnit(new UnitExplorer());
	
	grid.setOccupantAt(new Coord (4,3),b);
	
	if (grid.getOccupantAt(new Coord(4,4)).attemptMove(new Coord(4,6)))
		System.out.println("You moved to terrain out of your range...");
	else
		System.out.println("Success!");
	
	if (grid.getOccupantAt(new Coord(4,4)).attemptMove(new Coord(4,5)))
		System.out.println("You moved to impassable terrain...");
	else
		System.out.println("Success!");

	if (grid.getOccupantAt(new Coord(4,4)).attemptMove(new Coord(4,4)))
		System.out.println("You moved to yourself...");
	else
		System.out.println("Success!");

	if (grid.getOccupantAt(new Coord(4,4)).attemptMove(new Coord(4,3)))
		System.out.println("Attack!!");
	else
		System.out.println("Failed to attack...");

	if (grid.getOccupantAt(new Coord(4,3)).owner == 1)
		System.out.println("You didn't destroy enemy army...");
	else if (grid.getOccupantAt(new Coord(4,3)).owner == -1)
		System.out.println("You forgot to auto-move to the enemy army...");
	else if (grid.getOccupantAt(new Coord(4,4)) != null)
		System.out.println("You forgot to erase yourself from your previous position...");
	else
		System.out.println("Attack-Move successful!");
	
	a.refreshArmy();
	grid.setOccupantAt(new Coord(4,3),a);
	
	for (int i = 0; i < 95; ++i){
		b.addUnit(new UnitAttacker());
		c.addUnit(new UnitAttacker());
	}
	
	b.refreshArmy();
	c.refreshArmy();
	grid.setOccupantAt(new Coord(4,2),b);
	grid.setOccupantAt(new Coord(4,1),c);
	
	if (grid.getOccupantAt(new Coord(4,3)).attemptMove(new Coord(4,2)))
		System.out.println("Attack again!!");
	else
		System.out.println("Failed to attack...");
	
	if (grid.getOccupantAt(new Coord(4,2)).owner == 0)
		System.out.println("You somehow overthrew the enemy army...");
	else if (grid.getOccupantAt(new Coord(4,2)).owner == -1)
		System.out.println("You somehow deleted the enemy army...");
	else if (grid.getOccupantAt(new Coord(4,3)) != null){
		System.out.println("You somehow survived that encounter...");
		System.out.println("Surviving army size: " + grid.getOccupantAt(new Coord(4,3)).units.size());
	}
	else
		System.out.println("Army successfully lost!");
	
	Siege.currentPlayer = 1;
	b.refreshArmy();
	c.refreshArmy();
	grid.setOccupantAt(new Coord(4,2),b);
	grid.setOccupantAt(new Coord(4,1),c);
	
	if (grid.getOccupantAt(new Coord(4,2)).attemptMove(new Coord(4,1)))
		System.out.println("Moving to merge!!");
	else
		System.out.println("Failed to attempt merge...");
	
	if (grid.getOccupantAt(new Coord(4,2)).owner == 0)
		System.out.println("You over-merged...");
	else if (grid.getOccupantAt(new Coord(4,2)).units.size() > 91)
		System.out.println("You may have under-merged...");
	else if (grid.getOccupantAt(new Coord(4,1)).units.size() != 100){
		System.out.println("You definitely under-merged...");
	}
	else
		System.out.println("Armies successfully merged!");
	
	if (grid.getOccupantAt(new Coord(4,2)).attemptMove(new Coord(3,2)) || grid.getOccupantAt(new Coord(4,1)).attemptMove(new Coord(3,1)))
		System.out.println("One of the armies can double-move...");
	else
		System.out.println("Success!");
	
	Siege.currentPlayer = 2;
	Army d = new Army(2,new Coord(8,8));
	Army e = new Army(2,new Coord(7,7));
	for (int i = 0; i < 5; ++i){
		d.addUnit(new UnitBasic());
		e.addUnit(new UnitBasic());
	}
	d.refreshArmy();
	e.refreshArmy();
	
	grid.setOccupantAt(new Coord(8,8),d);
	grid.setOccupantAt(new Coord(7,7),e);
	
	if (grid.getOccupantAt(new Coord(8,8)).attemptMove(new Coord(7,7)))
		System.out.println("Another move for merge!!");
	else
		System.out.println("Failed to attempt merge...");
	
	if (grid.getOccupantAt(new Coord(8,8)) != null)
		System.out.println("You have left the merging army behind...");
	else if (grid.getOccupantAt(new Coord(7,7)).units.size() < 10)
		System.out.println("You have under-merged...");
	else
		System.out.println("Armies successfully merged!");
	
	if (grid.getOccupantAt(new Coord(7,7)).attemptMove(new Coord(6,7)))
		System.out.println("The combined army can double-move...");
	else
		System.out.println("Success!");
	}
}
