package ar.edu.itba.montu.war.people;

import ar.edu.itba.montu.abstraction.AttackingAgentCharacteristics;
import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarriorCharacteristics extends AttackingAgentCharacteristics {

	private final Characteristic<Double> speed;
	
	private WarriorCharacteristics(Castle c) {
		super(
				LocatableAgentCharacteristics.withViewDistanceAndHealthPoints(
						c.characteristics().viewDistance() / 10,
						c.characteristics().healthPoints() / 10
				),
				c.characteristics().attackDistance() / 10,
				(int)RandomUtil.getNormalDistribution(100 - c.characteristics().attackHarm(), c.characteristics().attackHarm() / 10)
		);
		this.speed = Characteristic.withFixedValue(
				RandomUtil.getNormalDistribution(c.kingdom().characteristics().warriorSpeed(), c.kingdom().characteristics().warriorSpeed() / 10)
		);
	}
	
	public static WarriorCharacteristics fromCastle(Castle c) {
		return new WarriorCharacteristics(c);
	}
	
	public double speed() {
		return speed.value();
	}
	
}
