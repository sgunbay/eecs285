package eecs285.proj4;

import java.util.*;

public class Army{
	
	public static int MAX_ARMY_SIZE = 100;
	public static double RECOIL = 0.3;
	public static Random rnd = new Random(System.currentTimeMillis());
	
	public Coord coord;
	public Double dmg;
	public Double spd;
	public Double inf;
	public Queue<Unit> units;
	
	Army(Unit u, int row, int col){
		units = new ArrayDeque<Unit>();
		units.add(u);
		dmg = u.dmg;
		spd = u.spd;
		inf = u.inf;
		coord = new Coord(row,col);
	}
	
	public Boolean isEmpty(){
		return units.isEmpty();
	}
	
	public Boolean isFull(){
		return (units.size() == 100);		
	}
	
	public Integer armySize(){
		return units.size();
	}
	
	public Boolean addUnit(Unit u){
		if (isFull())
			return false;
		units.add(u);
		dmg += u.dmg;
		spd = Math.min(spd,u.spd);
		inf = Math.max(inf,u.inf);
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
		
		return u;		
	}
	
	public Unit removeUnit(double probability){
		assert(probability >= 0);
		if (rnd.nextDouble() <= probability)
			return removeUnit();
		return null;
	}
	
	public void addArmy(Army a){
		while (!isFull() && !a.isEmpty())
			addUnit(a.removeUnit());		
	}

	public int getStrength(){
		return (int) Math.sqrt(dmg*units.size());
	}	
	
	public Army attack(Army target){
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
		
		return target;		
	}
}
