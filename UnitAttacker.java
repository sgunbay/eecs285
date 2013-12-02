package com.eecs285.siegegame;

public class UnitAttacker extends Unit{

	UnitAttacker() {
		name = new String("Attacker");
		dmg = new Double(5*ATTACK);
		spd = new Double(2);
		cost = new Integer(3);		
	}
	
}
