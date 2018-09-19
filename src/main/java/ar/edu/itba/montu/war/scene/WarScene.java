package ar.edu.itba.montu.war.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import ar.edu.itba.montu.abstraction.WarAgent;
import ar.edu.itba.montu.interfaces.IScene;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarScene implements IScene {

	private final List<Kingdom> kingdoms;
	private final List<WarAgent> agents;
	private final WarStrategy strategy;
	private final int kingdomTurns = 1000;
	private final float kingdomNegociatePercetage = 0.1f;
	/*(a) TODO SHOULD this object be refactored? To a public getter Singleton Object, which other Objs uses
		and if you want an snapshot of the war, store them in a List o sth like that
	(b) TODO SHOULD SET A PUBLIC VARIABLE time? Other objects must sense/see their enviroment once per dt*/
	
	private WarScene(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		kingdoms.forEach(kingdom -> {
			kingdom.enforceStrategy(strategy);
		});
		this.kingdoms = kingdoms;
		this.strategy = strategy;
		this.agents = new ArrayList<>();
	}
	
	public static WarScene withKingdomsAndStrategy(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		final WarScene warScene = new WarScene(strategy, kingdoms);
		return warScene;
	}

	private static List<Kingdom> shuffledKingdoms(final List<Kingdom> kingdoms) {
		final List<Integer> indexList = IntStream.range(0, kingdoms.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList, RandomUtil.getRandom());
		return indexList.stream().map(kingdoms::get).collect(Collectors.toList());
	}

	private static List<WarAgent> shuffledAgents(final List<WarAgent> agents) {
		final List<Integer> indexList = IntStream.range(0, agents.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList, RandomUtil.getRandom());
		return indexList.stream().map(agents::get).collect(Collectors.toList());
	}
	
	public void start(final long time) {
		LongStream.rangeClosed(1, time).forEach(this::loop);
	}

	public void loop(final long timeEllapsed){
		final List<Kingdom> shuffledKingdoms = shuffledKingdoms(kingdoms);
		final List<WarAgent> shuffledAgents = shuffledAgents(agents);


		shuffledKingdoms.forEach(kingdom -> kingdom.loop(this/*(a) makes this an empty f*/));
		shuffledAgents.forEach(agent -> agent.loop(this /*(a) makes this an empty f*/));
	}

	public List<WarAgent> getAgentsFromCoordinate(Coordinate coordinate, int viewDistance){
		return agents.stream().filter(agent -> Coordinate.sees(agent.getCoordinate(),coordinate,viewDistance)).collect(Collectors.toList());
	}

	public List<Kingdom> getKingdoms() {
		return kingdoms;
	}
}
