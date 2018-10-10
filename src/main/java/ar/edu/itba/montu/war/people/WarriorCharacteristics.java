package ar.edu.itba.montu.war.people;

import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.MovingAgentCharacteristic;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;

public class WarriorCharacteristics extends MovingAgentCharacteristic {

	private final Characteristic<Integer> attackDistance;
	
	
	
	private WarriorCharacteristics(final CastleCharacteristics characteristics, double attack) {
		super(viewDistance, healthPoint, attack);
	}

	public static WarriorCharacteristics standardCharacteristics() {
		return new WarriorCharacteristics(30,20,5000, 1);
	}
	
	public static WarriorCharacteristics defenseCharacteristics() {
		return new WarriorCharacteristics(30,20,5000, 1);
	}
	
	public static WarriorCharacteristics withHpSpeedDelayAndDamageSkill(final double hp) {
		return new WarriorCharacteristics(30,20,hp, 1);
	}
	
	
}
