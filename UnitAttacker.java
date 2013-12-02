package com.eecs285.siegegame;

public class UnitAttacker extends Unit{
	// High damage, low speed, moderate cost unit.
	
	UnitAttacker() {
		name = new String("Attacker");
		dmg = new Double(5*ATTACK);
		spd = new Double(2);
		cost = new Integer(3);		
	}
	
}
