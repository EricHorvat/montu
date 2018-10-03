package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristic;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class CastleCharacteristics extends LocatableAgentCharacteristic {
	
	private final int spawnCapacity = RandomUtil.getRandom().nextInt(3);

	private final Characteristic<Double> offenseCapacity;
	
	public CastleCharacteristics(
			final KingdomCharacteristics characteristics,
			final double viewDistance,
			final double attackDistance,
			final double healthPoint, double attack) {
		super(viewDistance, attackDistance, healthPoint, attack);
		this.offenseCapacity = Characteristic.withFixedValue(
				RandomUtil.getNormalDistribution(characteristics.offenseCapacity(), 0.1 * characteristics.offenseCapacity())
		);
	}
	
	public static CastleCharacteristics standardCharacteristics(final KingdomCharacteristics characteristics) {
		return new CastleCharacteristics(characteristics, 30, 20, 100000, 1);
	}
	
	public static CastleCharacteristics defenseCharacteristics(final KingdomCharacteristics characteristics) {
		return new CastleCharacteristics(characteristics, 30, 20, 100000, 1);
	}
	
	public int spawnCapacity() {
		return spawnCapacity;
	}
	
	public double offenseCapacity() {
		return offenseCapacity.value();
	}
	
	public double defenseCapacity() {
		return 100 - offenseCapacity.value();
	}
}
