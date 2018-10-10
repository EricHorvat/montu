package ar.edu.itba.montu.war.kingdom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.NonLocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.environment.WarEnviromentGenerator;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.environment.WarStrategy;
import ar.edu.itba.montu.war.kingdom.objective.KingdomAttackObjective;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.NegotiateObjective;
import ar.edu.itba.montu.war.objective.NegotiateObjective.Intention;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Kingdom extends Agent implements NonLocatableAgent {
	
	private static final Logger logger = LogManager.getLogger(Kingdom.class);
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	private final List<Castle> castles;
	private final List<Kingdom> rivals = new ArrayList<>();
	private final PriorityQueue<KingdomObjective> objectives = new PriorityQueue<>();
	
	private Optional<WarStrategy> strategy;
	private KingdomStatus status = KingdomStatus.IDLE;

	/* package */protected Kingdom(final String name, final KingdomCharacteristics kingdomCharacteristics, final List<CastleBuilder> castles) {
	  super();
	  this.name = name;
		this.characteristics = kingdomCharacteristics;
		this.castles = castles.stream().map(c -> c.kingdom(this).build()).collect(Collectors.toList());
	}

	public void enforceStrategy(final WarStrategy strategy) {
		this.strategy = Optional.of(strategy);
	}

	public KingdomStatus currentStatus() {
		return status;
	}
	
	public Optional<KingdomObjective> currentObjective() {
		return Optional.ofNullable(objectives.peek());
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
			if (kingdom.equals(this)) return;
			
			final double coinFlip = RandomUtil.getRandom().nextDouble();
			
			if (coinFlip > 0.5) {
				final int priority = RandomUtil.getRandom().nextInt(100);
				final KingdomAttackObjective objective = KingdomAttackObjective.headedToWithPriority(kingdom, priority);
				logger.debug("{} has objective {}", name, objective);
				objectives.add(objective);
			}
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
    		.filter(o-> other.objectives.contains(o))
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
		
		
//		sense();
	}

//	private PriorityQueue<Objective> findObjectives(Map<Kingdom,List<Coordinate>> kingdomCastleMap, List<WarFieldAgent> visibleAgents){
		/*TODO */
//		return new PriorityQueue<>();
//	}
	
	public List<LocatableAgent> agents() {
		return Stream.concat(castles.stream().map(v -> (LocatableAgent)v), castles.stream().map(Castle::agents).flatMap(List::stream)).collect(Collectors.toList());
	}

	public void castleWillDie(final Castle castle) {
		final Iterator<KingdomObjective> it = objectives.iterator();
		while (it.hasNext()) {
			final KingdomObjective o = it.next();
			if (o.target().castles.size() == 1 && o.target().castles.get(0).equals(castle)) {
				it.remove();
			}
		}
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

	@Override
	public String toString() {
		return name;
	}
	
}
