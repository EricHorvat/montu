package ar.edu.itba.montu.war.kingdom;

import java.util.*;
import java.util.stream.Collectors;

import ar.edu.itba.montu.abstraction.WarAgent;
import ar.edu.itba.montu.interfaces.IObjective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.scene.WarScene;
import ar.edu.itba.montu.war.scene.WarStrategy;
import ar.edu.itba.montu.war.utils.Coordinate;

public class Kingdom implements WarAgent /* TODO SHOULD IMPLEMENT WarFieldAgent?*/ {
	
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

	private void negotiate(final WarScene scene, final List<Kingdom> otherKingdoms){

	}

	private void sense(final WarScene scene, final List<Kingdom> otherKingdoms){
		Map<Kingdom,List<Coordinate>> kingdomCastleCoordinates = otherKingdoms.stream().collect(Collectors.toMap(Kingdom::getKingdom,Kingdom::getCastleCoordinates));
		List<WarAgent> visibleAgents = castles.stream().map(castle -> castle.getVisibleAgents(scene)).flatMap(List::stream).collect(Collectors.toList());

		/*TODO Now I got castle & warriors, with my castles status -> Objetive list*/
		PriorityQueue<IObjective> newObjectives = findObjectives(kingdomCastleCoordinates, visibleAgents);
		/*TODO compare new Objectives with old, equals for not error, must define*/
		if(!newObjectives.equals(objectives)){
			/*TODO See & negotiate*/
		}

	}

	public List<Coordinate> getCastleCoordinates(){
		return castles.stream().map(Castle::getCoordinate).collect(Collectors.toList());
	}

	public void loop(final WarScene scene){
		final List<Kingdom> otherKingdoms = scene.getKingdoms().stream().filter(k -> !k.equals(this)).collect(Collectors.toList());
		sense(scene,otherKingdoms);
		negotiate(scene,otherKingdoms);
	}

	private PriorityQueue<IObjective> findObjectives(Map<Kingdom,List<Coordinate>> kingdomCastleMap, List<WarAgent> visibleAgents){
		/*TODO */
		return new PriorityQueue<>();
	}

	@Override
	public Kingdom getKingdom() {
		return this;
	}
}
