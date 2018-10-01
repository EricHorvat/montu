package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.abstraction.MovingAgentCharacteristics;
import ar.edu.itba.montu.war.utils.DamageSkill;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class CastleCharacteristics extends LocatableAgentCharacteristics {

	private final double speedDelay;
	private final DamageSkill damageSkill;
	
	private final int spawnCapacity = RandomUtil.getRandom().nextInt(10);

	public CastleCharacteristics(double viewDistance, double attackDistance, double healthPoint, double speedDelay, DamageSkill damageSkill) {
		super(viewDistance, attackDistance, healthPoint);
		this.speedDelay = speedDelay;
		this.damageSkill = damageSkill;
	}
	
	public static CastleCharacteristics standardCharacteristics() {
		return new CastleCharacteristics(30,20,100000, 20, new DamageSkill(20, 30, 40, 50));
	}
	
	public static CastleCharacteristics defenseCharacteristics() {
		return new CastleCharacteristics(30,20,100000, 20, new DamageSkill(20, 30, 40, 50));
	}
	
	public static CastleCharacteristics withHpSpeedDelayAndDamageSkill(final double hp, final double speedDelay, final DamageSkill damageSkill) {
		return new CastleCharacteristics(30,20,hp, speedDelay, damageSkill);
	}

	public double speedDelay() {
		return speedDelay;
	}

	public DamageSkill damageSkill() {
		return damageSkill;
	}
	
	public int spawnCapacity() {
		return spawnCapacity;
	}
	
	
}
