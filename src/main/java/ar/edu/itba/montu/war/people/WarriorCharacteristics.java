package ar.edu.itba.montu.war.people;

import ar.edu.itba.montu.abstraction.AttackingAgentCharacteristics;
import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarriorCharacteristics extends AttackingAgentCharacteristics {

	private final Characteristic<Double> speed;
	
	private WarriorCharacteristics(Castle c) {
		super(
				LocatableAgentCharacteristics.withViewDistanceAndHealthPoints(
						// Warriors get 20% of the castles view distance initially
						// Of course, a normal distribution applies when born
						(int)Math.max(RandomUtil.getNormalDistribution(c.characteristics().viewDistance() / 5, 0.1), 0),
						// Warriors get 10% of the castles health points initially
						// Of course, a normal distribution applies when born
						(int)RandomUtil.getNormalDistribution(c.characteristics().healthPoints() / 10, 50)
				),
				// Warriors get 10% of the castles attack distance initially
				// Of course, a normal distribution applies when born
				(int)Math.max(RandomUtil.getNormalDistribution(c.characteristics().viewDistance() / 10, 0.05), 0),
				// A castles attack harm is (100 - offense capacity) of the kingdom
				// This means the castles best attack is his defense = (100 - offense)
				// Therefore, we (100 - attack harm) and get the 10% of it
				// Warriors get 10% of (100 - castles attack harm) initially
				// Of course, a normal distribution applies when born
				(int)Math.max(RandomUtil.getNormalDistribution((100 - c.characteristics().attackHarm()) / 10, 2), 0)
		);
		// Speed is in km/min. These come near to 0.48km/min
		// We apply a noise of 10% that
		this.speed = Characteristic.withFixedValue(
				Math.max(RandomUtil.getNormalDistribution(c.kingdom().characteristics().warriorSpeed(), 0.05), 0)
		);
	}
	
	public static WarriorCharacteristics fromCastle(Castle c) {
		return new WarriorCharacteristics(c);
	}
	
	public double speed() {
		return speed.value();
	}
	
}
