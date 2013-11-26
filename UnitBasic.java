package com.eecs285.siegegame;

public class UnitBasic extends Unit{
	
	UnitBasic() {
		name = new String("Basic");
		dmg = new Double(3*ATTACK);
		spd = new Double(5);
		inf = new Double(2);
		cost = new Integer(3);
	}
	
}
