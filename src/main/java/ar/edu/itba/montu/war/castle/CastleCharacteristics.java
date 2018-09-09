package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.war.utils.DamageSkill;

public class CastleCharacteristics {
	
	private final double hp;
	private final double speedDelay;
	private final DamageSkill damageSkill;
	
	private CastleCharacteristics(final double hp, final double speedDelay, final DamageSkill damageSkill) {
		this.hp = hp;
		this.speedDelay = speedDelay;
		this.damageSkill = damageSkill;
	}
	
	public static CastleCharacteristics standardCharacteristics() {
		return new CastleCharacteristics(100, 20, new DamageSkill(20, 30, 40, 50));
	}
	
	public static CastleCharacteristics withHpSpeedDelayAndDamageSkill(final double hp, final double speedDelay, final DamageSkill damageSkill) {
		return new CastleCharacteristics(hp, speedDelay, damageSkill);
	}

	public double getHp() {
		return hp;
	}

	public double getSpeedDelay() {
		return speedDelay;
	}

	public DamageSkill getDamageSkill() {
		return damageSkill;
	}
	
	
	
	
}
