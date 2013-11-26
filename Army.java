package com.eecs285.siegegame;

import java.awt.*;
import java.util.*;

public class Army{
	
	public static int MAX_ARMY_SIZE = 100;
	public static double RECOIL = 0.3;
	public static Random rnd = new Random(System.currentTimeMillis());
	
	public Integer owner;
	public Coord coord;
	public Double dmg;
	public Double spd;
	public Double inf;
	public Queue<Unit> units;
	public Vector<Coord> possibleMoves;
	public Integer steps;
	
	Army(Unit u, Coord in_coord){
		owner = new Integer(Siege.currentPlayer);
		units = new ArrayDeque<Unit>();
		units.add(u);
		dmg = u.dmg;
		spd = u.spd;
		inf = u.inf;
		steps = spd.intValue();
		coord = in_coord;
		possibleMoves = updatePossibleMoves();
	}
	
	public Color getColor(){
		return 	Siege.players[owner].color;	
	}
	
	private Boolean isEmpty(){
		return units.isEmpty();
	}
	
	private Boolean isFull(){
		return (units.size() == 100);		
	}
	
	public Boolean addUnit(Unit u){
		if (isFull())
			return false;
		units.add(u);
		dmg += u.dmg;
		spd = Math.min(spd,u.spd);
		inf = Math.max(inf,u.inf);
		steps = spd.intValue();
		return true;
	}
	
	private Unit removeUnit(){
		
		assert(!isEmpty());
		Unit u = units.remove();
		dmg -= u.dmg;

		if (!isEmpty()){
			Double min_spd = new Double(100);
			Double max_inf = new Double(0);
			for (Unit x : units){
				min_spd = Math.min(min_spd,x.spd);
				max_inf = Math.max(max_inf,x.inf);
			}
			spd = min_spd;
			inf = max_inf;
		}
		steps = spd.intValue();
		return u;		
	}
	
	private Unit removeUnit(double probability){
		assert(probability >= 0);
		if (rnd.nextDouble() <= probability)
			return removeUnit();
		return null;
	}
	
	private Army mergeTo(Army a){
		while (!isEmpty() && !a.isFull())
			a.addUnit(removeUnit());	
		a.possibleMoves = null;
		a.steps = 0;
		return a;
	}

	public int getStrength(){
		return (int) Math.sqrt(dmg*units.size());
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
		
		possibleMoves = null;
		if (target.isEmpty())
			return null;
		return target;		
	}

	public Vector<Coord> updatePossibleMoves(){
	// Returns a vector of all possible coords that this army can move to.
		
		if (steps == 0)
			return null;
		
		double range = steps * Siege.grid.getTile(coord).getSpdFactor();
		int r = (int) range;
		Vector<Coord> moves = new Vector<Coord>();
		
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
		
		while (!possible.isEmpty()){
			PCoord c = possible.remove();
			if (c.range == 0)
				continue;
			
			Coord [] next = new Coord[4];
			next[0] = new Coord(c.coord.row,c.coord.col-1);
			next[1] = new Coord(c.coord.row,c.coord.col+1);
			next[2] = new Coord(c.coord.col+1,c.coord.col);
			next[3] = new Coord(c.coord.col-1,c.coord.col);
			for (int i = 0; i < 4; ++i){
				if (Siege.grid.withinBounds(next[i]) && Siege.grid.getTile(next[i]).isPassable){
					if (!moves.contains(next[i])){
						moves.add(next[i]);
						possible.add(new PCoord(next[i],c.range-1));
					}
				}
			}
		}
		
		return moves;
	}
	
	private void move(Coord target){
		Tile mytile = Siege.grid.getTile(coord);
		Tile victimTile = Siege.grid.getTile(target);
		victimTile.setOccupant(this);
		if (victimTile.isCity())
			victimTile.owner = owner;
		mytile.setOccupant(null);
		Siege.grid.setTile(coord, mytile);
		Siege.grid.setTile(target, victimTile);	
	}
	
	public boolean attemptMove(Coord target){
	// Returns true on successful move.
	// Returns false on failure.
		
	// Attempts to move, attack or merge.
		if (possibleMoves == null || !possibleMoves.contains(target)){
			return false;
		}
		
		Tile mytile = Siege.grid.getTile(coord);
		Tile victimTile = Siege.grid.getTile(target);
		if (victimTile.getOccupant() != null){
			if (victimTile.getOccupant().owner != owner){
				victimTile.setOccupant(attack(victimTile.getOccupant()));
				if (victimTile.getOccupant() == null)
					move(target);
			}
			else {
				victimTile.setOccupant(mergeTo(victimTile.getOccupant()));
			}
			possibleMoves = null;
			Siege.grid.setTile(target,victimTile);
			mytile.setOccupant(this);
			Siege.grid.setTile(coord,mytile);
			return true;
		}		
		
		move(target);
	
		// Update yourself.
		coord = target;
		possibleMoves = updatePossibleMoves();
		return true;
	}
	
	public boolean withinInfRange(Coord target){
		int rowdist = target.row - coord.row;
		int coldist = target.col - coord.col;
		double range = inf * Siege.grid.getTile(coord).getInfFactor();
		if ((rowdist + coldist) > range)
			return false;
		return true;
	}

	public void refreshArmy(){
		steps = spd.intValue();
		possibleMoves = updatePossibleMoves();	
	}
	
}
