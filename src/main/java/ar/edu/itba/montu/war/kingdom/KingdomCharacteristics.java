package ar.edu.itba.montu.war.kingdom;

import ar.edu.itba.montu.abstraction.WarAgentCharacteristics;

public class KingdomCharacteristics extends WarAgentCharacteristics {
	
	final double speed;
	final double lifespan;
	final double attack;
	
	private KingdomCharacteristics(final double speed, final double lifespan, final double attack) {
		this.speed = speed;
		this.lifespan = lifespan;
		this.attack = attack;
	}
	
	public static KingdomCharacteristics withSpeedLifespanAndAttack(final double speed, final double lifespan, final double attack) {
		return new KingdomCharacteristics(speed, lifespan, attack);
	}
}
