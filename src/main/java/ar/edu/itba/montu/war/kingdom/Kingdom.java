package ar.edu.itba.montu.war.kingdom;

import java.util.*;
import java.util.stream.Collectors;

import ar.edu.itba.montu.abstraction.IWarAgent;
import ar.edu.itba.montu.abstraction.WarFieldAgent;
import ar.edu.itba.montu.interfaces.IObjective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.environment.WarStrategy;
import ar.edu.itba.montu.war.utils.Coordinate;

public class Kingdom implements IWarAgent {
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	
	private Optional<WarStrategy> strategy;
	
	private KingdomStatus status = KingdomStatus.ALIVE;

	private final List<Castle> castles;
	private final PriorityQueue<IObjective> objectives;

	/* package */protected Kingdom(final String name, final KingdomCharacteristics kingdomCharacteristics, final List<Castle> castles) {
		this.name = name;
		this.characteristics = kingdomCharacteristics;
		this.castles = castles;
		this.objectives = new PriorityQueue<>();
	}

	public void enforceStrategy(final WarStrategy strategy) {
		this.strategy = Optional.of(strategy);
	}

	public KingdomStatus getCurrentStatus() {
		return status;
	}

	private void negotiate(){
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> otherKingdoms = environment.getKingdoms().stream().filter(k -> !k.equals(this)).collect(Collectors.toList());
		// TODO Should negotiate based on PQ, that should modify strategies or new actions but MUSTN'T edit PQ
    // TODO MUST DEFINE HOW ACTIONS WILL BE TAKEN AND THEN FILL

	}

	private void sense(){
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> kingdoms = environment.getKingdoms();
		Map<Kingdom,List<Coordinate>> kingdomCastleCoordinates = kingdoms.stream().collect(Collectors.toMap(Kingdom::getKingdom,Kingdom::getCastleCoordinates));
		List<WarFieldAgent> visibleAgents = castles.stream().map(Castle::getVisibleAgents).flatMap(List::stream).collect(Collectors.toList());

		/*TODO Now I got castle & warriors, with my castles status -> Objective list*/
		PriorityQueue<IObjective> newObjectives = findObjectives(kingdomCastleCoordinates, visibleAgents);
		// Compare new Objectives with old, relies in PQ equals so relies in Objective equals (and hashCode)
		if(!newObjectives.equals(objectives)){
			negotiate();
		}

	}

	public List<Coordinate> getCastleCoordinates(){
		return castles.stream().map(Castle::getCoordinate).collect(Collectors.toList());
	}

	@Override
	public void loop(){
		sense();
	}

	private PriorityQueue<IObjective> findObjectives(Map<Kingdom,List<Coordinate>> kingdomCastleMap, List<WarFieldAgent> visibleAgents){
		/*TODO */
		return new PriorityQueue<>();
	}

	@Override
	public Kingdom getKingdom() {
		return this;
	}
}
