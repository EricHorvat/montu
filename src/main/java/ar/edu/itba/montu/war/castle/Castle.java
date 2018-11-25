package ar.edu.itba.montu.war.castle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.abstraction.*;
import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.environment.WarStrategy;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.DefendObjective;
import ar.edu.itba.montu.war.people.WarriorRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

import static ar.edu.itba.montu.war.people.WarriorRole.ATTACKER;
import static ar.edu.itba.montu.war.people.WarriorRole.DEFENDER;

public class Castle extends LocatableAgent implements Spawner {
	
	
	private static final Logger logger = LogManager.getLogger(Castle.class);
	
	final private String name;
	final private CastleCharacteristics characteristics;
	
	private KingdomObjective currentObjective;
	private String status;
	private List<Objective> objectives;
	
	final List<Warrior> warriors;
//	private List<WarFieldAgent> visibleAgents = new ArrayList<>();
	
	/* package */ Castle(final Kingdom kingdom, final String name, final CastleCharacteristics characteristics, final Coordinate coordinate, final int warriors, final int healers) {
		super();
		this.name = name;
		this.status = LocatableAgentStatus.ALIVE;
		this.characteristics = characteristics;
		this.location = coordinate;
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
  
  public void tick(final long timeEllapsed) {
	
  	logger.trace("{} tick={}", name, timeEllapsed);
  	
  	characteristics.increaseResources(App.getConfiguration().getResourcesPerMinute());
  	updateObjetives();
  	/*TODO EVALUATE OBJECTIVES AND NEGOTIATE WITH OWN CASTLES
	   *
	   * */
		
  	List<Objective> turnObjectives;
  	double turnOffensiveRoll = RandomUtil.getRandom().nextDouble() * (1.0 - App.getConfiguration().getHealthOffensiveRollCoefficient() / 2.0) + (-0.5 + this.characteristics().healthPercentage()) * App.getConfiguration().getHealthOffensiveRollCoefficient();
  	if (turnOffensiveRoll * 100 < this.characteristics().offenseCapacity()) {
  		turnObjectives = attackObjectives();
	  } else {
  		turnObjectives = defendObjectives();
	  }
  	
  	/*This could be optimal; by near enemy castles; but its difficult to apply*/
	  double prioritySum = turnObjectives.stream().mapToDouble(Objective::priority).sum();
	  double priorityValue = RandomUtil.getRandom().nextDouble() * prioritySum;
	  for (Objective objective : turnObjectives) {
		  priorityValue -= objective.priority();
		  if (priorityValue <= 0 ) {
		  	WarriorRole warriorRole = objective instanceof AttackObjective ? ATTACKER : DEFENDER;
		  	createWarriors(1, warriorRole);
		    objective.apply(this);
			  break;
		  }
	  }
	  
	  final List<LocatableAgent> visibleRivalAgents =
	  		visibleAgents()
	  		.stream()
	  		.filter(l -> l instanceof MovingAgent && this.kingdom().isEnemy(l.kingdom()))
	  		.collect(Collectors.toList());
	  
	  if (!visibleRivalAgents.isEmpty()) {
		  for (Warrior availableWarrior: availableWarriors()) {
				LocatableAgent enemySelected = visibleRivalAgents.get(0);
			  double sum = visibleRivalAgents.stream().mapToDouble(attacker -> Double.max(1.0 / Coordinate.distanceBetween(this.location(), attacker.location()), App.getConfiguration().getMinPriorityDistance())).sum();
			  double value = RandomUtil.getRandom().nextDouble() * sum;
			  for (LocatableAgent enemy : visibleRivalAgents) {
				  value -= Double.max(1.0 / Coordinate.distanceBetween(this.location(), enemy.location()), App.getConfiguration().getMinPriorityDistance());
				  if (value <= 0) {
					  enemySelected = enemy;
					  break;
				  }
			  }
			  availableWarrior.assignToTarget(enemySelected, App.getConfiguration().getMaxPriority());
		  }
	  }
	  
  }

	public Coordinate location() {
		return location;
	}


  public CastleCharacteristics characteristics() {
    return characteristics;
  }

	public List<LocatableAgent> visibleAgents() {
		final WarEnvironment environment = WarEnvironment.getInstance();
		
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
	private Warrior buildWarrior(WarriorRole role, boolean isSuper) {
		int resources = characteristics.resources();
		Warrior w = Warrior.createWarriorInCastle(this, role, isSuper);
		if (resources - w.resourcesCost() < 0){
			w.noCreated();
			return null;
		}
		this.useResources(w.resourcesCost());
		return w;
	}
	
	public Warrior createAWarrior(WarriorRole role) {
		final List<Warrior> warriors = IntStream
			.range(0, 1)
			.mapToObj(i -> buildWarrior(role,false))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
		this.warriors.addAll(warriors);
		return warriors.size() == 0 ? null : warriors.get(0);
	}
	
	public Warrior createASuperWarrior(WarriorRole role) {
		final List<Warrior> warriors = IntStream
			.range(0, 1)
			.mapToObj(i -> buildWarrior(role,true))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
		this.warriors.addAll(warriors);
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
	
	public List<Warrior> warriors() {
		return warriors
				.stream()
				.filter(Warrior::isAlive)
				.collect(Collectors.toList());
	}
	
	public List<Warrior> attackers() {
		return warriors
			.stream()
			.filter(Warrior::isAttacker)
			.filter(Warrior::isAlive)
			.collect(Collectors.toList());
	}
	
	public List<Warrior> defenders() {
		return warriors
			.stream()
			.filter(Warrior::isDefender)
			.filter(Warrior::isAlive)
			.collect(Collectors.toList());
	}

	@Override
	public void defend(LocatableAgent agent, double harm) {
		double hp = characteristics.healthPoints() - harm;
		kingdom().addEnemy(agent.kingdom());
		if (hp < 0) {
			if (WarEnvironment.getInstance().strategy().equals(WarStrategy.DOMINATION_BY_DESTRUCTION)) {
				this.status = LocatableAgentStatus.DEAD;
				hp = 0;
				WarEnvironment.getInstance().onCastleDeath(this);
				int ownResources = this.characteristics.resources();
				Castle enemyCastle = agent.castle();
				if (!enemyCastle.isAlive()) {
					List<Castle> aliveCastles = agent.kingdom().castles();
					if (aliveCastles.size() == 0) {
						enemyCastle = null;
					} else if (aliveCastles.size() == 1) {
						enemyCastle = aliveCastles.get(0);
					} else {
						enemyCastle = aliveCastles.get(RandomUtil.getRandom().nextInt(aliveCastles.size() - 1));
					}
				}
				if (enemyCastle != null) {
					CastleCharacteristics enemyCharacteristics = agent.castle().characteristics();
					enemyCharacteristics.boostResourcesBy(Integer.max(0, ownResources + enemyCharacteristics.resources() - enemyCharacteristics.maxResources()));
					enemyCharacteristics.increaseResources(ownResources);
					this.characteristics.useResources(ownResources);
				}
			} else {
				this.characteristics().increaseDeaths();
				this.kingdom.castles().remove(this);
				this.kingdom = agent.kingdom();
				this.characteristics().setOffenseCapacity(agent.kingdom().characteristics());
				this.kingdom.castles().add(this);
				hp = (int) (this.characteristics().maxHealthPoints() / (1 + this.characteristics().deaths()));
			}
		}
		characteristics.healthPoints(hp);
	}

	@Override
	public int getHealthPointPercentage() {
		return (int)(100 * characteristics.healthPercentage());
	}

	@Override
	public boolean isAlive() {
		return status.equals(LocatableAgentStatus.ALIVE);
	}
	
	@Override
	public List<Warrior> createWarriors(final int quantity, WarriorRole role){
		
		List<Warrior> warriorList =
			IntStream
			.range(0, quantity)
			.filter(i -> RandomUtil.getRandom().nextDouble() < characteristics.spawnProbability())
			.mapToObj(i -> (RandomUtil.getRandom().nextDouble() < App.getConfiguration().getSuperWarriorProbability() ? createASuperWarrior(role) : createAWarrior(role)))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
			
		warriorList.forEach(warrior -> kingdom.addAgent(warrior));
		return warriorList;
	}
	
	public boolean hasResources() {
		return characteristics.hasResources();
	}

	public Castle useResources(int resources) {
		characteristics.useResources(resources);
		return this;
	}
	
	@Override
	public String toString() {
		return name + " \nResources:" + characteristics.resources() + "/" + characteristics.maxResources() + " \nHP:" + characteristics.healthPoints() + "/" + characteristics.maxHealthPoints();
	}
	
	public String name() {
		return name;
	}
	
	public void updateObjetives() {
		List<Objective> objectives = new ArrayList<>();
		kingdom.objectivePriorityList().stream().forEach(kingdomObjective -> {
			List<Objective> partialObjectives = kingdomObjective.translate(location);
			/*Can alter before assignment*/
			objectives.addAll(partialObjectives);
		});
		this.objectives = objectives;
	}
	
	@Override
	public LocatableAgent asLocatableAgent() {
		return this;
	}
	
	@Override
	public List<Objective> attackObjectives() {
		return objectives.stream().filter(objective -> objective instanceof AttackObjective).collect(Collectors.toList());
	}
	
	@Override
	public List<Objective> defendObjectives() {
		return objectives.stream().filter(objective -> objective instanceof DefendObjective).collect(Collectors.toList());
	}
	
	@Override
	public Castle castle() {
		return this;
	}
}
