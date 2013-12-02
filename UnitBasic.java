package com.eecs285.siegegame;

public class UnitBasic extends Unit{
	// Moderate damage, speed and cost.
	UnitBasic() {
		name = new String("Basic");
		dmg = new Double(3*ATTACK);
		spd = new Double(3);
		cost = new Integer(2);
	}
	
}
