package ar.edu.itba.montu.war.castle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.war.people.WarriorRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.MovingAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Castle extends LocatableAgent implements Spawner {

	private static final Logger logger = LogManager.getLogger(Castle.class);
	
	/// TODO should be retrieved from Configuration 
	private static final int GAS_PER_MINUTE = 1;
	
	final private String name;
	final private CastleCharacteristics characteristics;
	/**
	 * The height of the castle is a measure of how
	 * far away it can see
	 */
	final private double height;
	
	private KingdomObjective currentObjective;
	
	final List<Warrior> warriors;
//	private List<WarFieldAgent> visibleAgents = new ArrayList<>();
	
	/* package */ Castle(final Kingdom kingdom, final String name, final CastleCharacteristics characteristics, final Coordinate coordinate, final int warriors, final int healers, final double height) {
		super();
		this.name = name;
		this.characteristics = characteristics;
		this.location = coordinate;
		this.height = height;
		this.kingdom = kingdom;
		
		this.warriors = new ArrayList<>();
		/*this.warriors = IntStream
				.range(0, warriors)
				.mapToObj(i -> Warrior.createWithCharacteristicsInKingdomAtLocation(coordinate, characteristics, this))
				.collect(Collectors.toList());*/
	}

  public void act() {
	  // Decide to attack or spawn
    // if attack
//    List<WarFieldAgent> enemyAgentsOnRange;
    //Collections.shuffle(enemyAgentsOnRange,RandomUtil.getRandom());
    /*for(int i = 0; i < characteristics.getConcurrentAttackCount; i++){
      enemyAgentsOnRange.get(i % enemyAgentsOnRange.size()).attacked()
    }*/
    // if spawn
//    buildWarriorWithCharacteristics(this.characteristics);

  }
  
  private void enforceCurrentObjective() {
  	currentObjective.translate().forEach(castleObjective -> {
  		logger.debug("{} enforces {}", name, castleObjective);
  		castleObjective.enforce(this);
  	});
  }

  public void tick(final long timeEllapsed) {
	  /// TODO: template what a castle does on each tick
	
  	logger.trace("{} tick={}", name, timeEllapsed);
  	
  	characteristics.increaseGas(characteristics.gas() + GAS_PER_MINUTE);
  	
	  final Optional<KingdomObjective> kingdomObjective = kingdom.currentObjective();
	
	  if (!kingdomObjective.isPresent()) {
	  	logger.debug("{} has no objectives");
		  return;
	  }
	
	  final double d = RandomUtil.getRandom().nextDouble() * 100;
	
	  final List<LocatableAgent> visibleRivalAgents =
	  		visibleAgents()
	  		.stream()
	  		.filter(l -> l instanceof MovingAgent && this.kingdom().isEnemy(l.kingdom()))
	  		.collect(Collectors.toList());
	
	  /* TODO SHITTY HACK THEN COME BACK*/
//	  availableDefenders().stream().filter(Warrior::isUnassigned).forEach(d -> );
	  
	  if (!visibleRivalAgents.isEmpty()) {
	  	visibleRivalAgents.forEach(rival -> availableWarriors().forEach(def -> def.assignToTarget(rival, RandomUtil.getRandom().nextInt(1000)))); /*TODO WARN*/
		  /* TODO THEN COME BACK*/
	  }
	  
	  if (d < characteristics.defenseCapacity()) {
		  // spawn defense warrior
		  logger.debug("{} spawns a defending warrior", name);
	  	createWarriors(1,WarriorRole.DEFENDER);
		  return;
	  }
	
	  if (kingdomObjective.get().equals(currentObjective)) {
	  	logger.debug("{} enforce current objective", name);
		  this.enforceCurrentObjective();
		  return;
	  }
	
	  currentObjective = kingdomObjective.get();
  }

	public Coordinate location() {
		return location;
	}


  public CastleCharacteristics characteristics() {
    return characteristics;
  }

	public List<LocatableAgent> visibleAgents() {
		final WarEnvironment environment = WarEnvironment.getInstance();
		
		///TODO: make the proper calculations to get the value of radius
		return environment
				.agentsWithinRadiusOfCoordinate(location, characteristics.viewDistance())
				.stream()
				.filter(agent -> !agent.kingdom().equals(kingdom))
				.collect(Collectors.toList());
	}
	
	public List<LocatableAgent> agents() {
		return warriors.stream().map(w -> (LocatableAgent)w).collect(Collectors.toList());
	}
	
	/*WARN CAN BE NULL*/
	private Warrior buildAttacker() {
		int gas = characteristics.gas();
		Warrior w = Warrior.createWithCharacteristicsInKingdomAtLocation(location, characteristics, this);
		if (gas - w.gasCost() < 0){
			w.noCreated();
			return null;
		}
		this.useGas(w.gasCost());
		return w;
	}
	
	/*WARN CAN BE NULL*/
	private Warrior build() {
		int gas = characteristics.gas();
		Warrior w = Warrior.createWithCharacteristicsInKingdomAtLocation(location, characteristics, this);
		if (gas - w.gasCost() < 0){
			w.noCreated();
			return null;
		}
		this.useGas(w.gasCost());
		return w;
	}

	public Warrior createAnAttacker() {
		int population = characteristics.population();
		int populationLimit = characteristics.maxPopulation() - population;
		final int attackerCount = Math.min(populationLimit,Math.min(1, characteristics.spawnCapacity()));
		final List<Warrior> warriors = IntStream
				.range(0, attackerCount)
				.mapToObj(i -> buildAttacker())
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		this.warriors.addAll(warriors);
		characteristics.population(population + warriors.size());
		return warriors.size() == 0 ? null : warriors.get(0);
	}

	@Override
	public List<Warrior> availableAttackers() {
		return warriors
			.stream()
			.filter(Warrior::isAttacker)
			.filter(Warrior::isAvailable)
			.collect(Collectors.toList());
	}
	
	@Override
	public List<Warrior> availableDefenders() {
		return warriors
			.stream()
			.filter(Warrior::isDefender)
			.filter(Warrior::isAvailable)
			.collect(Collectors.toList());
	}
	
	@Override
	public List<Warrior> availableWarriors() {
		return warriors
			.stream()
			.filter(Warrior::isAvailable)
			.collect(Collectors.toList());
	}

	@Override
	public void defend(LocatableAgent agent, int damageSkill) {
		int hp = characteristics.healthPoints() - damageSkill;
		kingdom().addEnemy(agent.kingdom());
		if (hp < 0) {
			//TODO STATUS = DEAD
			hp = 0;
			WarEnvironment.getInstance().onCastleDeath(this);
		}
		characteristics.healthPoints(hp);
	}

	@Override
	public int getHealthPointPercentage() {
		return (int)(100 * characteristics.healthPercentage());
	}

	@Override
	public boolean isAlive() {
		//TODO CHANGE
		return characteristics.healthPoints() > 0;
	}
	
	@Override
	public List<LocatableAgent> createWarriors(final int quantity, WarriorRole role /*, Characteristics?*/){
		
		return IntStream
			.range(0,quantity)
			.filter(i -> RandomUtil.getRandom().nextDouble() < 0.01)
			.mapToObj(i -> createAnAttacker())
			.collect(Collectors.toList());
	}
	
	public boolean hasGas() {
		return characteristics.hasGas();
	}

	public Castle useGas(int gas) {
		characteristics.useGas(gas);
		return this;
	}
	
	@Override
	public String toString() {
		return name + " \nRes:" + characteristics.gas() + "/" + characteristics.maxGas() + " \nPop:" + characteristics.population() + "/" + characteristics.maxPopulation() ;
	}
	
}
