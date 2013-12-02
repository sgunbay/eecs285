package com.eecs285.siegegame;

import java.util.*;

public class Army{
	
	public static int MAX_ARMY_SIZE = 100;
	public static double RECOIL = 0.5;
	
	public int owner; // Player that owns army.
	public Coord coord; // Coordinates of army.
	public Double dmg; // Total damage.
	public Double spd; // Army speed.
	public Queue<Unit> units; // Vector of units in the army.
	public Vector<Coord> possibleMoves; // Vector of all possible coordinates to which we can move.
	public Vector<Coord> possibleInfluences; // Vector of all possible coordinates we influence.
	
	
	Army(int in_owner, Coord in_coord){
	// Constructor.
		owner = in_owner;
		units = new ArrayDeque<Unit>();
		dmg = new Double(0);
		spd = new Double(70);
		coord = in_coord;
		possibleMoves = new Vector<Coord>();
		possibleInfluences = new Vector<Coord>();
	}

	public String getColor(){
	// Reutrns a string representing player color.
		return "player" + owner + "color";
	}
	
	public Boolean isEmpty(){
		return units.isEmpty();
	}
	
	public Boolean isFull(){
		return (units.size() == MAX_ARMY_SIZE);		
	}
	
	public Boolean addUnit(Unit u){
	// Adds a unit to the army.
		if (isFull())
			return false;
		units.add(u);
		dmg += u.dmg;
		spd = Math.min(spd,u.spd);
		return true;
	}
	
	private Unit removeUnit(){
	// Unconditionally kills off a unit.	
		assert(!isEmpty());
		Unit u = units.remove();
		dmg -= u.dmg;

		if (!isEmpty()){
			Double min_spd = new Double(100);
			for (Unit x : units)
				min_spd = Math.min(min_spd,x.spd);
			spd = min_spd;
		}
		return u;		
	}
	
	private Unit removeUnit(double probability){
	// Kills off a unit from the army with a certain probability.
		assert(probability >= 0);
		if (Siege.rnd.nextDouble() <= probability)
			return removeUnit();
		return null;
	}
	
	private Army mergeTo(Army a){
	// Merges two armies together.
		while (!isEmpty() && !a.isFull())
			a.addUnit(removeUnit());	
		a.possibleMoves = new Vector<Coord>();
		return a;
	}

	public int getStrength(){
		// Returns a value representing the strength of the army.
		return (int) Math.ceil(Math.sqrt(dmg*(units.size())));
	}	
	
	private Army attack(Army target){
	// Set target equal to the result of this function!!
		
		double damage = dmg;
		double recoil = RECOIL * target.dmg;
		
		for (; damage > 0; --damage){
			if (!target.isEmpty())
				target.removeUnit(damage);
		}
		
		for (; recoil > 0; --recoil){
			if (!isEmpty())
				removeUnit(recoil);
		}
		
		possibleMoves = new Vector<Coord>();
		if (target.isEmpty())
			return null;
		return target;		
	}

	private boolean movesContains(Vector<Coord> moves, Coord c){
		if (c.row == coord.row && c.col == coord.col)
			return true;
		
		for (Coord i : moves){
			if (i.row == c.row && i.col == c.col)
				return true;
		}
		return false;		
	}
	
	public Vector<Coord> updatePossibleMoves(){
	// Returns a vector of all possible coords that this army can move to.
		
		// Set up the range.
		double range = spd * Siege.grid.getTile(coord).getSpdFactor();
		int r = (int) range;
		Vector<Coord> moves = new Vector<Coord>();
		
		// Dummy armies can't move.
		if (units.size() == 0)
			return moves;
		
		// Inner class for the pathfinder's.
		class PCoord{
			Coord coord;
			public int range;
			PCoord(Coord coord_in, int range_in){
				coord = coord_in;
				range = range_in;
			}
			
		}
		
		PCoord init = new PCoord(coord,r);		
		Queue<PCoord> possible = new ArrayDeque<PCoord>();
		possible.add(init);
		
		// Pathfinder algorithm for detecting all possible movements within range.
		while (!possible.isEmpty()){
			PCoord c = possible.remove();
			if (c.range == 0)
				continue;
			
			Coord [] next = new Coord[4];
			next[0] = new Coord(c.coord.row,c.coord.col-1);
			next[1] = new Coord(c.coord.row,c.coord.col+1);
			next[2] = new Coord(c.coord.row+1,c.coord.col);
			next[3] = new Coord(c.coord.row-1,c.coord.col);
			for (int i = 0; i < 4; ++i){
				if (Siege.grid.withinBounds(next[i]) && Siege.grid.getTile(next[i]).isPassable){
					if (!movesContains(moves,next[i])){
						moves.add(next[i]);
						possible.add(new PCoord(next[i],c.range-1));
					}
				}
			}
		}
		return moves;
	}
	
	private void move(Coord target){
		
		// Force moves the army to target.
		// Updates the grid.
		Tile mytile = Siege.grid.getTile(coord);
		Tile victimTile = Siege.grid.getTile(target);
		
		Siege.grid.setOccupantAt(target, this);
		
		if (victimTile.isCity())
			victimTile.owner = owner;
		
		Siege.grid.setOccupantAt(coord, null);
		
		if (mytile.isCity())
			Siege.grid.setOccupantAt(coord, new Army(owner, coord));
		
		Siege.grid.setTile(coord, mytile);
	}
	
	
	public boolean attemptMove(Coord target){
	// Returns true on successful move.
	// Returns false on failure.
		
	// Attempts to move, attack or merge.
		if (possibleMoves.size() == 0 || (coord.row == target.row && coord.col == target.col) || !movesContains(possibleMoves, target)){
			return false;
		}
		
		Army victim = Siege.grid.getOccupantAt(target);
		if (victim != null){
			if (victim.owner != owner) // Attack if target has an enemy army.
				Siege.grid.setOccupantAt(target,attack(victim));
			else // Merge if target has an allied army.
				Siege.grid.setOccupantAt(target,mergeTo(victim));
		}
		
		if (isEmpty()){ // Delete yourself from grid if you got destroyed by merge / attack recoil.
			if (Siege.grid.getTile(coord).isCity())
				Siege.grid.setOccupantAt(coord, new Army(owner, coord));
			else
				Siege.grid.setOccupantAt(coord,null);
		}
		else if (Siege.grid.getOccupantAt(target) == null) // Force move otherwise.
			move(target);
		
		// Update yourself.
		coord = target;
		possibleMoves = new Vector<Coord>();
		updatePossibleInfluences();
		return true;
	}

	private int dist(Coord a, Coord b){
	// Calculate distance between two coordinates.
		int rowdist = Math.max(a.row - b.row, b.row - a.row);
		int coldist = Math.max(a.col - b.col, b.col - a.col);
		return rowdist + coldist;	
	}
	
	private void updatePossibleInfluences(){
		// Updates the vector of all squares influenced by this army.
		possibleInfluences = new Vector<Coord>();
		
		// Dummy armies don't influence anything.
		if (isEmpty())
			return;
		
		// Determine range.
		double range = spd * Siege.grid.getTile(coord).getSpdFactor();
		int r = (int) range;
		
		// Grab all valid coordinates within range.
		for (int i = coord.row-r; i <= coord.row+r; ++i){
			for (int j = coord.col-r; j <= coord.col+r; ++j){
				Coord c = new Coord(i,j);
				if (Siege.grid.withinBounds(c) && (dist(c,coord) <= r))
					possibleInfluences.add(c);
			}
		}
		
		return;
	}
	
	public void updateInfluences(){
		// Update the "infers" variables of all cities and resources influenced by this army.
		
		if (isEmpty())
			return;
		
		updatePossibleInfluences(); // Update your influence vector.
		for (Coord c : possibleInfluences){
			Tile t = Siege.grid.getTile(c);	
			
			if ((t.isCity()) && (t.owner != owner) && (t.owner != -1)){	
				t.infers++; // Update infers of all hostile cities influenced by you.
			}	
			else if (t.isResource() && (t.owner == -1)){
				if (t.infers == 0) // Capture neutral resources.
					t.owner = owner;
				else // Influence conflicted resources.
					t.infers++;
			}
			else if (t.isResource() && (t.owner != owner) && (t.owner != -1)){
			// Cause enemy resources to fall under conflict.
				t.owner = -1;
				t.infers++;
			}
			Siege.grid.setTile(c, t);
		}	
	}
	
	public void refreshArmy(){
	// Update influences and possible moves.
		updateInfluences();
		if (owner == Siege.currentPlayer)
			possibleMoves = updatePossibleMoves();
	}
	
}
