package ar.edu.itba.montu.war.people;

import ar.edu.itba.montu.abstraction.MovingAgentCharacteristics;
import ar.edu.itba.montu.war.utils.DamageSkill;

public class WarriorCharacteristics extends MovingAgentCharacteristics {

	private final double speedDelay;
	private final DamageSkill damageSkill;

	/*private WarriorCharacteristics(final double hp, final double speedDelay, final DamageSkill damageSkill) {
		this.speedDelay = speedDelay;
		this.damageSkill = damageSkill;
	}*/

	public WarriorCharacteristics(double viewDistance, double attackDistance, double healthPoint, double speedDelay, DamageSkill damageSkill) {
		super(viewDistance, attackDistance, healthPoint);
		this.speedDelay = speedDelay;
		this.damageSkill = damageSkill;
	}

	public static WarriorCharacteristics standardCharacteristics() {
		return new WarriorCharacteristics(30,20,100, 20, new DamageSkill(20, 30, 40, 50));
	}
	
	public static WarriorCharacteristics defenseCharacteristics() {
		return new WarriorCharacteristics(30,20,100, 20, new DamageSkill(20, 30, 40, 50));
	}
	
	public static WarriorCharacteristics withHpSpeedDelayAndDamageSkill(final double hp, final double speedDelay, final DamageSkill damageSkill) {
		return new WarriorCharacteristics(30,20,hp, speedDelay, damageSkill);
	}

	public double getSpeedDelay() {
		return speedDelay;
	}

	public DamageSkill getDamageSkill() {
		return damageSkill;
	}
	
	
	
	
}
