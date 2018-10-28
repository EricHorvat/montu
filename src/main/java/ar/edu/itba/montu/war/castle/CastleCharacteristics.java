package ar.edu.itba.montu.war.castle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.AttackingAgentCharacteristics;
import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.people.WarriorCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class CastleCharacteristics extends AttackingAgentCharacteristics {
	
	private static final Logger logger = LogManager.getLogger(CastleCharacteristics.class);
	
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
				(int)Math.min(Math.max(RandomUtil.getNormalDistribution(kingdomCharacteristics.offenseCapacity(), 10), 0), 100)
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
	
	public CastleCharacteristics increaseGas(int gas) {
		this.gas.updateValue(this.gas.value() + gas);
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
		return 100.0 - offenseCapacity.value();
	}

	@Override
	public String toString() {
		return "CastleCharacteristics [offenseCapacity=" + offenseCapacity + ", gas=" + gas + ", attackDistance()="
				+ attackDistance() + ", attackHarm()=" + attackHarm() + ", viewDistance()=" + viewDistance()
				+ ", healthPoints()=" + healthPoints() + ", maxHealthPoints()=" + maxHealthPoints() + ", healthPercentage()="
				+ healthPercentage() + "]";
	}
}
