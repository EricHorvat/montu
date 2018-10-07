package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristic;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class CastleCharacteristics extends LocatableAgentCharacteristic {

	private final Characteristic<Double> offenseCapacity;
	private final Characteristic<Integer> spawnCapacity;

	private final Characteristic<Integer> population;
	private final Characteristic<Integer> populationGas;
	
	public CastleCharacteristics(
			final KingdomCharacteristics characteristics,
			final double viewDistance,
			final double attackDistance,
			final double healthPoints,
			final double attack) {
		super(viewDistance, attackDistance, healthPoints, attack);
		this.offenseCapacity = Characteristic.withFixedValue(
				RandomUtil.getNormalDistribution(characteristics.offenseCapacity(), 0.1 * characteristics.offenseCapacity())
		);
		this.spawnCapacity = Characteristic.withFixedValue(
				RandomUtil.getRandom().nextInt(10)
		);
		this.population = Characteristic.withChangingValue(
				0,
				RandomUtil.getRandom().nextInt(20),
				0
		);
		this.populationGas = Characteristic.withChangingValue(
				0,
				RandomUtil.getRandom().nextInt(50)
		);
	}
	
	public static CastleCharacteristics standardCharacteristics(final KingdomCharacteristics characteristics) {
		return new CastleCharacteristics(characteristics, 30, 20, 100000, 1);
	}
	
	public static CastleCharacteristics defenseCharacteristics(final KingdomCharacteristics characteristics) {
		return new CastleCharacteristics(characteristics, 30, 20, 100000, 1);
	}

	public int population() {
		return population.value();
	}

	public int maxPopulation() {
		return population.maxValue();
	}

	public void population(int value) {
		population.updateValue(value);
	}

	public int populationGas() {
		return populationGas.value();
	}

	public void populationGas(int value) {
		populationGas.updateValue(value);
	}

	public int spawnCapacity() {
		return spawnCapacity.value();
	}

	public double offenseCapacity() {
		return offenseCapacity.value();
	}
	
	public double defenseCapacity() {
		return 100 - offenseCapacity.value();
	}
}
