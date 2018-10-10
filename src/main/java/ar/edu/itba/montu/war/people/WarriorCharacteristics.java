package ar.edu.itba.montu.war.people;

import ar.edu.itba.montu.abstraction.AttackingAgentCharacteristics;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarriorCharacteristics extends AttackingAgentCharacteristics {

	private WarriorCharacteristics(CastleCharacteristics c) {
		super(
				LocatableAgentCharacteristics.withViewDistanceAndHealthPoints(
						c.viewDistance() / 10,
						c.healthPoints() / 10
				),
				c.attackDistance() / 10,
				(int)RandomUtil.getNormalDistribution(100 - c.attackHarm(), c.attackHarm() / 10)
		);
	}
	
	public static WarriorCharacteristics fromCastleCharacteristics(CastleCharacteristics c) {
		return new WarriorCharacteristics(c);
	}
	
}
