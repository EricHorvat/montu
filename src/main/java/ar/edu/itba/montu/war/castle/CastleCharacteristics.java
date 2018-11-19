package ar.edu.itba.montu.war.castle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.AttackingAgentCharacteristics;
import ar.edu.itba.montu.abstraction.Characteristic;
import ar.edu.itba.montu.abstraction.LocatableAgentCharacteristics;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class CastleCharacteristics extends AttackingAgentCharacteristics {
	
	private static final Logger logger = LogManager.getLogger(CastleCharacteristics.class);
	
	private final Characteristic<Integer> offenseCapacity;
	private Characteristic<Integer> resources;
	private Characteristic<Integer> deaths;
	private Characteristic<Double> spawnProbability;
	
	public CastleCharacteristics(
			final KingdomCharacteristics kingdomCharacteristics,
			final AttackingAgentCharacteristics attackCharacteristics,
			final int resources,
			final double spawnProbability,
			final int deaths
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
		this.resources = Characteristic.withChangingValue(0, resources);
		this.spawnProbability = Characteristic.withFixedValue(spawnProbability);
		this.deaths = Characteristic.withChangingValue(0, deaths, 0);
	}

	public int resources() {
		return resources.value();
	}
	
	public int maxResources() {
		return resources.maxValue();
	}

	public CastleCharacteristics useResources(int resources) {
		this.resources.updateValue(this.resources.value() - resources);
		return this;
	}
	
	public CastleCharacteristics increaseResources(int resources) {
		this.resources.updateValue(this.resources.value() + resources);
		return this;
	}
	
	public boolean hasResources() {
		return resources() > 0;
	}
	
	public CastleCharacteristics boostResourcesBy(int resources) {
		this.resources = Characteristic.withChangingValue(0, maxResources() + resources, resources());
		return this;
	}
	
	public CastleCharacteristics boostResourcesByWithCost(int resources, int cost) {
		return this.useResources(cost).boostResourcesBy(resources);
	}

	public double offenseCapacity() {
		return offenseCapacity.value();
	}
	
	public double defenseCapacity() {
		return 100.0 - offenseCapacity.value();
	}
	
	public double spawnProbability() {
		return spawnProbability.value();
	}
	
	public int deaths() {
		return deaths.value();
	}
	
	
	public void increaseDeaths() {
		if (!this.deaths.value().equals(this.deaths.maxValue())) {
			this.deaths.updateValue(this.deaths.value() + 1);
		}
	}

	@Override
	public String toString() {
		return "CastleCharacteristics [offenseCapacity=" + offenseCapacity + ", resources=" + resources + ", attackDistance()="
				+ attackDistance() + ", attackHarm()=" + attackHarm() + ", viewDistance()=" + viewDistance()
				+ ", healthPoints()=" + healthPoints() + ", maxHealthPoints()=" + maxHealthPoints() + ", healthPercentage()="
				+ healthPercentage() + "]";
	}
}
