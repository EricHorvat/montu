package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class CastleCharacteristics extends LocatableAgentCharacteristics {
	
	private final int spawnCapacity = RandomUtil.getRandom().nextInt(10);

	public CastleCharacteristics(double viewDistance, double attackDistance, double healthPoint, double attack) {
		super(viewDistance, attackDistance, healthPoint, attack);
	}
	
	public static CastleCharacteristics standardCharacteristics() {
		return new CastleCharacteristics(30,20,100000, 1);
	}
	
	public static CastleCharacteristics defenseCharacteristics() {
		return new CastleCharacteristics(30,20,100000, 1);
	}
	
	public static CastleCharacteristics withHpSpeedDelayAndDamageSkill(final double hp) {
		return new CastleCharacteristics(30,20,hp, 1);
	}
	
	public int spawnCapacity() {
		return spawnCapacity;
	}
	
	
}
