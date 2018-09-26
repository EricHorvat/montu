package ar.edu.itba.montu.war.kingdom;

import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;

public class KingdomCharacteristics{
	
	final double speed;
	final double lifespan;
	final double attack;

	public KingdomCharacteristics(double speed, double lifespan, double attack) {
		this.speed = speed;
		this.lifespan = lifespan;
		this.attack = attack;
	}
	
	public static KingdomCharacteristics withSpeedLifespanAndAttack(final double speed, final double lifespan, final double attack) {
		return new KingdomCharacteristics(speed, lifespan, attack);
	}
}
