package ar.edu.itba.montu.war.castle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ar.edu.itba.montu.abstraction.LocatableAgentStatus;
import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.objective.AttackObjective;
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
  	
  	characteristics.increaseGas(characteristics.gas() + GAS_PER_MINUTE);
  	updateObjetives();
  	/*TODO EVALUATE OBJECTIVES AND NEGOTIATE WITH OWN CASTLES
	   *
	   * TODO if GAS
	   *
	   *   A = RANDOM VALUE (0,SUM_PRIORITIES_VALUES)
	   *   PQ.TO_LIST_RANDOM_SORT
	   *   ITERATE LIST it ->
	   *     A -= it.priority
	   *     if A <=0
	   *       TAKE IT AS NEXT OBJ
	   *   CREATE WARRIOR FOR OBJ
	   *
	   *  K1 = [p = 500]
	   *  K2 = [p = 3]
	   *  K3 = [p = 2]
	   *  A = 7
	   *  [K1]
	   *  A -= 5 (A = 2)
	   *  [K2]
	   *  A -= 3 (A = -1)
	   *  K2.apply()
	   * */
		
  	/*This could be optimal; by near enemy castles; but its difficult to apply*/
	  int prioritySum = objectives.stream().mapToInt(Objective::priority).sum();
	  int priorityValue = RandomUtil.getRandom().nextInt(prioritySum);
	  for (Objective objective : objectives){
		  priorityValue -= objective.priority();
		  if (priorityValue <= 0 ){
		  	WarriorRole warriorRole = objective instanceof AttackObjective ? WarriorRole.ATTACKER : WarriorRole.DEFENDER;
		  	createWarriors(1,warriorRole);
		  	if (warriors.size() > 0){
		  		int e = 9;}
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
	  	visibleRivalAgents.forEach(rival -> availableWarriors().forEach(def -> def.assignToTarget(rival, RandomUtil.getRandom().nextInt(1000)))); /*TODO WARN*/
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
		
		return IntStream
			.range(0,quantity)
			.filter(i -> RandomUtil.getRandom().nextDouble() < 0.01)/*TODO CHANGE THIS*/
			.mapToObj(i -> createAWarrior(role))
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
		return name + " \nGas:" + characteristics.gas() + "/" + characteristics.maxGas() + " \nHP:" + characteristics.healthPoints() + "/" + characteristics.maxHealthPoints();
	}
	
	public String name() {
		return name;
	}
	
	public void updateObjetives() {
		/*TODO IDEA PROXIMITY OR STH LIKE THAT*/
		List<Objective> objectives = new ArrayList<>();
		for (KingdomObjective kingdomObjective : kingdom.objectivePriorityList()) {
			List<Objective> partialObjectives = kingdomObjective.translate();
			partialObjectives.forEach(objective -> {/*TODO 19/10 ALTER PRIORITY*/});
			objectives.addAll(partialObjectives);
		}
		this.objectives = objectives;
	}
	
	@Override
	public LocatableAgent asLocatableAgent() {
		return this;
	}
}
