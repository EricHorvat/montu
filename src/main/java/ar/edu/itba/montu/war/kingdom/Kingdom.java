package ar.edu.itba.montu.war.kingdom;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.NonLocatableAgent;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.environment.WarStrategy;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.NegotiateObjective;
import ar.edu.itba.montu.war.objective.NegotiateObjective.Intention;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Kingdom extends Agent implements NonLocatableAgent {
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	private final List<Castle> castles;
	private final PriorityQueue<Objective> objectives = new PriorityQueue<>();
	
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
	
	public Optional<Objective> currentObjective() {
		return Optional.of(objectives.peek());
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
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> kingdoms = environment.kingdoms();
		final Map<Kingdom, List<Coordinate>> kingdomCastleCoordinates = kingdoms.stream().collect(Collectors.toMap(Function.identity(), Kingdom::castleCoordinates));
		final List<LocatableAgent> visibleAgents = castles.stream().map(Castle::visibleAgents).flatMap(List::stream).collect(Collectors.toList());
		
		///TODO: algorithm to build initial strategy
		
		double d = RandomUtil.getRandom().nextDouble();
		
		if (d > 0.5) {
			/// attack first agent
			objectives.add(AttackObjective.headedTo(visibleAgents.get(0)));
			status = KingdomStatus.ATTACKING;
			return;
		}
		
		final Map<Boolean, List<Kingdom>> otherKingdoms =
				kingdoms
				.stream()
				.filter(k -> !k.equals(this))
				.collect(Collectors.partitioningBy(v -> RandomUtil.getRandom().nextDouble() > 0.5));
		
		final List<Kingdom> friendKingdoms = otherKingdoms.get(true);
		final List<Kingdom> enemyKingdoms = otherKingdoms.get(false);
		
		objectives.add(NegotiateObjective.withOtherToIntentTargets(friendKingdoms, Intention.ATTACK, enemyKingdoms));
		status = KingdomStatus.NEGOTIATING;
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

	public void tick(final long timeEllapsed) {
		/// TODO: template what a kingdom does on each tick
		
		Objective objective = objectives.peek();
		
		switch (status) {
			case IDLE:
				this.sense();
				break;
			case ATTACKING:
				break;
			case NEGOTIATING:
				break;
			default:
				break;
		}
		
		
		
		sense();
	}

//	private PriorityQueue<Objective> findObjectives(Map<Kingdom,List<Coordinate>> kingdomCastleMap, List<WarFieldAgent> visibleAgents){
		/*TODO */
//		return new PriorityQueue<>();
//	}
	
	public List<LocatableAgent> agents() {
		return Stream.concat(castles.stream().map(v -> (LocatableAgent)v), castles.stream().map(Castle::agents).flatMap(List::stream)).collect(Collectors.toList());
	}
}
