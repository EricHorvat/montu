package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.abstraction.AttackingAgentCharacteristics;
import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class CastleCharacteristics extends AttackingAgentCharacteristics {
	
	private final Characteristic<Integer> offenseCapacity;
	private Characteristic<Integer> gas;
	
	public CastleCharacteristics(
			final KingdomCharacteristics kingdomCharacteristics,
			final AttackingAgentCharacteristics attackCharacteristics,
			final int gas
	) {
		super(
				LocatableAgentCharacteristics.withViewDistanceAndHealthPoints(
						attackCharacteristics.viewDistance(),
						attackCharacteristics.healthPoints()
				),
				attackCharacteristics.attackDistance(),
				attackCharacteristics.attackHarm()
		);
		this.offenseCapacity = Characteristic.withFixedValue(
				(int)RandomUtil.getNormalDistribution(kingdomCharacteristics.offenseCapacity(), 0.1 * kingdomCharacteristics.offenseCapacity())
		);
		this.gas = Characteristic.withChangingValue(0, gas);
	}

	public int gas() {
		return gas.value();
	}
	
	public int maxGas() {
		return gas.maxValue();
	}

	public CastleCharacteristics useGas(int gas) {
		this.gas.updateValue(this.gas.value() - gas);
		return this;
	}
	
	public boolean hasGas() {
		return gas() > 0;
	}
	
	public CastleCharacteristics boostGasBy(int gas) {
		this.gas = Characteristic.withChangingValue(0, maxGas() + gas, gas());
		return this;
	}
	
	public CastleCharacteristics boostGasByWithCost(int gas, int cost) {
		return this.useGas(cost).boostGasBy(gas);
	}

	public double offenseCapacity() {
		return offenseCapacity.value();
	}
	
	public double defenseCapacity() {
		return 100 - offenseCapacity.value();
	}
}
