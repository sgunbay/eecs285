package com.eecs285.siegegame;

public class UnitRusher extends Unit{
	// High damage, speed and cost.
	UnitRusher() {
		name = new String("Rusher");
		dmg = new Double(5*ATTACK);
		spd = new Double(5);
		cost = new Integer(5);
	}
	
}
