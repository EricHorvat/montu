package ar.edu.itba.montu.war.kingdom;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.war.people.Warrior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.NonLocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.environment.WarStrategy;
import ar.edu.itba.montu.war.kingdom.objective.KingdomAttackObjective;
import ar.edu.itba.montu.war.kingdom.objective.KingdomDefendObjective;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Kingdom extends Agent implements NonLocatableAgent {
	
	private static final Logger logger = LogManager.getLogger(Kingdom.class);
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	private final List<Castle> castles;
	private final List<Kingdom> rivals = new ArrayList<>();
	private final List<KingdomObjective> objectives = new ArrayList<>();
	
	private int color = 0xffffff;
	private List<LocatableAgent> agents = new ArrayList<>();
	private Optional<WarStrategy> strategy;
	private KingdomStatus status = KingdomStatus.IDLE;

	/* package */protected Kingdom(final String name, final KingdomCharacteristics kingdomCharacteristics, final List<CastleBuilder> castles) {
	  super();
	  this.name = name;
		this.characteristics = kingdomCharacteristics;
		this.castles = castles.stream().map(c -> c.kingdom(this).build()).collect(Collectors.toList());
	}

	public void applyStrategy(final WarStrategy strategy) {
		this.strategy = Optional.of(strategy);
	}

	public KingdomStatus currentStatus() {
		return status;
	}
	
	private void negotiate() {
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> otherKingdoms = environment.kingdoms().stream().filter(k -> !k.equals(this)).collect(Collectors.toList());
		// TODO Should negotiate based on PQ, that should modify strategies or new actions but MUSTN'T edit PQ
    // TODO MUST DEFINE HOW ACTIONS WILL BE TAKEN AND THEN FILL

	}
	
	/**
	 * Here we build the initial strategy
	 * This happens at time=0
	 * We have to construct initial objective PQ
	 * This has to be done based on characteristics
	 */
	public void buildInitialStrategy() {
		
		logger.debug("[{}] {} is building initial strategy", uid(), name);
		
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> kingdoms = environment.kingdoms();
		final Map<Kingdom, List<Coordinate>> kingdomCastleCoordinates = kingdoms.stream().collect(Collectors.toMap(Function.identity(), Kingdom::castleCoordinates));
		final List<LocatableAgent> visibleAgents = castles.stream().map(Castle::visibleAgents).flatMap(List::stream).collect(Collectors.toList());
		
		kingdoms.forEach(kingdom -> {
			KingdomObjective ko;
			double priority;
			if (kingdom.equals(this)) {
				priority = RandomUtil.getRandom().nextDouble()* characteristics().defenseCapacity();
				ko = KingdomDefendObjective.fromWithPriority(this,priority);
			} else{
				priority = RandomUtil.getRandom().nextDouble() * characteristics().offenseCapacity();
				ko = KingdomAttackObjective.headedToWithPriority(kingdom,priority);
			}
			logger.debug("{} has objective {}", name, ko);
			objectives.add(ko);
			//}
		});
		
		return;
		
		
		
		///TODO: algorithm to build initial strategy
		
//		double d = RandomUtil.getRandom().nextDouble();
//		
//		
//		
//		
//		if (d > 0.5) {
//			/// attack first agent
//			final Spawner enemy = visibleAgents.get(RandomUtil.getRandom().nextInt(kingdoms.size()));
//			logger.debug("[{}] {} will attack {}", uid(), name, ((Agent)enemy).uid());
//			objectives.add(AttackObjective.headedToWithPriority(enemy, 100));
//			status = KingdomStatus.ATTACKING;
//			return;
//		}
//		
//		logger.debug("[{}] {} will negotiate", uid(), name);
//		
//		final Map<Boolean, List<Kingdom>> otherKingdoms =
//				kingdoms
//				.stream()
//				.filter(k -> !k.equals(this))
//				.collect(Collectors.partitioningBy(v -> RandomUtil.getRandom().nextDouble() > 0.5));
//		
//		final List<Kingdom> friendKingdoms = otherKingdoms.get(true);
//		final List<Kingdom> enemyKingdoms = otherKingdoms.get(false);
//		
//		objectives.add(NegotiateObjective.withOtherToIntentTargetsAndPriority(friendKingdoms, Intention.ATTACK, enemyKingdoms, 100));
//		status = KingdomStatus.NEGOTIATING;
	}

	private void sense() {
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> kingdoms = environment.kingdoms();
		final Map<Kingdom, List<Coordinate>> kingdomCastleCoordinates = kingdoms.stream().collect(Collectors.toMap(Function.identity(), Kingdom::castleCoordinates));
		final List<LocatableAgent> visibleAgents = castles.stream().map(Castle::visibleAgents).flatMap(List::stream).collect(Collectors.toList());

		/*TODO Now I got castle & warriors, with my castles status -> Objective list*/
//		PriorityQueue<IObjective> newObjectives = findObjectives(kingdomCastleCoordinates, visibleAgents);
		// Compare new Objectives with old, relies in PQ equals so relies in Objective equals (and hashCode)
//		if(!newObjectives.equals(objectives)){
//			negotiate();
//		}

	}

	public List<Coordinate> castleCoordinates() {
		return castles.stream().map(Castle::location).collect(Collectors.toList());
	}
	
	public List<KingdomObjective> objectiveIntersectionWith(final Kingdom other) {
    return objectives.stream()
    		.filter(other.objectives::contains)
    		.collect(Collectors.toList());
}

	public void tick(final long timeEllapsed) {
		/// TODO: template what a kingdom does on each tick
		
//		KingdomObjective objective = objectives.peek();
		
//		switch (status) {
//			case IDLE:
//				this.sense();
//				break;
//			case ATTACKING:
//				break;
//			case NEGOTIATING:
//				break;
//			default:
//				break;
//		}
		
		logger.debug("{} tick={}", name, timeEllapsed);
		
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> kingdoms = environment.kingdoms();
		
		kingdoms.forEach(kingdom -> {
			if (kingdom.equals(this)) return;
			
			final double coinFlip = RandomUtil.getRandom().nextDouble();
			
			if (coinFlip > 0.9) return;
			
			final List<KingdomObjective> objectiveIntersection = objectiveIntersectionWith(kingdom);
			
			if (objectiveIntersection.isEmpty()) return;
			
			logger.debug("{} neg with {} over {}", this.name, kingdom.name, objectiveIntersection);
			
			objectiveIntersection.forEach(objective -> {
				if (objective.priority() > 50) {
					objective.alterPriority(objective.priority() / 2);
				}
			});
		});
		
		agents = agents.stream().filter(LocatableAgent::isAlive).collect(Collectors.toList());
//		sense();
	}

//	private PriorityQueue<Objective> findObjectives(Map<Kingdom,List<Coordinate>> kingdomCastleMap, List<WarFieldAgent> visibleAgents){
		/*TODO */
//		return new PriorityQueue<>();
//	}
	
	public List<LocatableAgent> agents() {
		return Stream.concat(castles.stream().map(v -> (LocatableAgent)v), agents.stream()).collect(Collectors.toList());
	}

	public void castleWillDie(final Castle castle) {
		objectives.removeIf(o -> o.target().castles.size() == 1 && o.target().castles.get(0).equals(castle));
	}
	
	public void castleDidDie(final Castle castle) {
		castles.remove(castle);
	}
	
	public List<Castle> castles() {
		return this.castles;
	}
	
	public boolean isEnemy(Kingdom k){
		return rivals.contains(k);
	}
	
	public void addEnemy(Kingdom k){
		rivals.add(k);
	}
	
	public KingdomCharacteristics characteristics() {
		return characteristics;
	}
	
	public List<KingdomObjective> objectivePriorityList(){
		return objectives;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public String name() {
		return name;
	}
	
	public void addAgent(LocatableAgent agent){
		agents.add(agent);
	}

	public void changeColor(final int color) {
		this.color = color;
	}

	public int color() {
		return color;
	}
	
	public double power() {
		OptionalDouble hpTotalPercentage = castles.stream().mapToDouble(Castle::getHealthPointPercentage).average();
		if (hpTotalPercentage.isPresent()){
			long totalWarriors = agents.stream().filter(LocatableAgent::isAlive).filter(agent -> agent instanceof Warrior).count();
			return  Configuration.CASTLE_POWER_COEF * castles.size() +
				Configuration.HP_POWER_COEF * hpTotalPercentage.getAsDouble() +
				Configuration.WARRIOR_POWER_COEF * totalWarriors;
		}else{
			return 0;
		}
	}
}
