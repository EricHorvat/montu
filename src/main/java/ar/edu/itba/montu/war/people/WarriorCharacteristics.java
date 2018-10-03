package ar.edu.itba.montu.war.people;

import ar.edu.itba.montu.abstraction.MovingAgentCharacteristic;

public class WarriorCharacteristics extends MovingAgentCharacteristic {

	public WarriorCharacteristics(double viewDistance, double attackDistance, double healthPoint, double attack) {
		super(viewDistance, attackDistance, healthPoint, attack);
	}

	public static WarriorCharacteristics standardCharacteristics() {
		return new WarriorCharacteristics(30,20,100, 1);
	}
	
	public static WarriorCharacteristics defenseCharacteristics() {
		return new WarriorCharacteristics(30,20,100, 1);
	}
	
	public static WarriorCharacteristics withHpSpeedDelayAndDamageSkill(final double hp) {
		return new WarriorCharacteristics(30,20,hp, 1);
	}
	
	
}
