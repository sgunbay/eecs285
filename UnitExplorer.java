package com.eecs285.siegegame;

public class UnitExplorer extends Unit{
	// Low damage, high speed, low cost unit.
	UnitExplorer() {
		name = new String("Explorer");
		dmg = new Double(1*ATTACK);
		spd = new Double(5);
		cost = new Integer(2);
	}
	
}
