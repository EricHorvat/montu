package ar.edu.itba.montu.war.castle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ar.edu.itba.montu.abstraction.*;
import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.DefendObjective;
import ar.edu.itba.montu.war.people.WarriorCharacteristics;
import ar.edu.itba.montu.war.people.WarriorRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  	
  	characteristics.increaseGas(GAS_PER_MINUTE);
  	updateObjetives();
  	/*TODO EVALUATE OBJECTIVES AND NEGOTIATE WITH OWN CASTLES
	   *
	   * */
		
  	List<Objective> turnObjectives;
  	if(RandomUtil.getRandom().nextDouble() * 100 < this.characteristics().offenseCapacity()){
  		turnObjectives = attackObjectives();
	  }else{
  		turnObjectives = defendObjectives();
	  }
  	
  	/*This could be optimal; by near enemy castles; but its difficult to apply*/
	  double prioritySum = turnObjectives.stream().mapToDouble(Objective::priority).sum();
	  double priorityValue = RandomUtil.getRandom().nextDouble() * prioritySum;
	  for (Objective objective : turnObjectives) {
		  priorityValue -= objective.priority();
		  if (priorityValue <= 0 ) {
		  	WarriorRole warriorRole = objective instanceof AttackObjective ? WarriorRole.ATTACKER : WarriorRole.DEFENDER;
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
			  double sum = visibleRivalAgents.stream().mapToDouble(attacker -> Double.max(1.0 / Coordinate.distanceBetween(this.location(), attacker.location()), Configuration.MIN_PRIORITY_DISTANCE)).sum();
			  double value = RandomUtil.getRandom().nextDouble() * sum;
			  for (LocatableAgent enemy : visibleRivalAgents) {
				  value -= Double.max(1.0 / Coordinate.distanceBetween(this.location(), enemy.location()), Configuration.MIN_PRIORITY_DISTANCE);
				  if (value <= 0) {
					  enemySelected = enemy;
					  break;
				  }
			  }
			  availableWarrior.assignToTarget(enemySelected,Configuration.MAX_PRIORITY);
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
	private Warrior buildWarrior(WarriorRole role) {
		int gas = characteristics.gas();
		Warrior w = Warrior.createWarriorInCastle(this, role);
		if (gas - w.gasCost() < 0){
			w.noCreated();
			return null;
		}
		this.useGas(w.gasCost());
		return w;
	}

	public Warrior createAWarrior(WarriorRole role) {
		final List<Warrior> warriors = IntStream
				.range(0, 1)
				.mapToObj(i -> buildWarrior(role))
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
	public void defend(LocatableAgent agent, int harm) {
		int hp = characteristics.healthPoints() - harm;
		kingdom().addEnemy(agent.kingdom());
		if (hp < 0) {
			this.status = LocatableAgentStatus.DEAD;
			hp = 0;
			WarEnvironment.getInstance().onCastleDeath(this);
			int ownGas = this.characteristics.gas();
			Castle enemyCastle = agent.castle();
			if (!enemyCastle.isAlive()){
				List<Castle> aliveCastles = agent.kingdom().castles();
				if(aliveCastles.size()==0){
					enemyCastle = null;
				}else if(aliveCastles.size()==1){
					enemyCastle = aliveCastles.get(0);
				}else{
					enemyCastle = aliveCastles.get(RandomUtil.getRandom().nextInt(aliveCastles.size()-1));
				}
			}
			if (enemyCastle != null){
				CastleCharacteristics enemyCharacteristics = agent.castle().characteristics();
				enemyCharacteristics.boostGasBy(Integer.max(0,ownGas + enemyCharacteristics.gas() - enemyCharacteristics.maxGas()));
				enemyCharacteristics.increaseGas(ownGas);
				this.characteristics.useGas(ownGas);
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
	public List<Warrior> createWarriors(final int quantity, WarriorRole role /*, Characteristics?*/){
		
		List<Warrior> warriorList =
			IntStream
			.range(0,quantity)
			.filter(i -> RandomUtil.getRandom().nextDouble() < 0.01)/*TODO CHANGE THIS*/
			.mapToObj(i -> createAWarrior(role))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
			
		warriorList.forEach(warrior -> kingdom.addAgent(warrior));
		return warriorList;
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
		return name + " \nGas:" + characteristics.gas() + "/" + characteristics.maxGas() + " \nHP:" + characteristics.healthPoints() + "/" + characteristics.maxHealthPoints();
	}
	
	public String name() {
		return name;
	}
	
	public void updateObjetives() {
		/*TODO IDEA PROXIMITY OR STH LIKE THAT*/
		List<Objective> objectives = new ArrayList<>();
		kingdom.objectivePriorityList().stream().forEach(kingdomObjective -> {
			List<Objective> partialObjectives = kingdomObjective.translate(location);
			partialObjectives.forEach(objective -> {/*TODO 19/10 ALTER PRIORITY*/});
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
