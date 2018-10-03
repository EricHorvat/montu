package ar.edu.itba.montu.war.kingdom;

import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;

public class KingdomCharacteristics{

	final Double attack;

	public KingdomCharacteristics(double speed, double lifespan, double attack) {
		/*CONTROL 0.0 <= attack <= 1.0*/
		this.attack = attack;
	}
	
	public static KingdomCharacteristics withSpeedLifespanAndAttack(final double speed, final double lifespan, final double attack) {
		return new KingdomCharacteristics(speed, lifespan, attack);
	}
}
