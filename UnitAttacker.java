package eecs285.proj4;

public class UnitAttacker extends Unit{

	UnitAttacker() {
		name = new String("Attacker");
		dmg = new Double(5*ATTACK);
		spd = new Double(3);
		inf = new Double(3);
		cost = new Double(4);		
	}
	
}
