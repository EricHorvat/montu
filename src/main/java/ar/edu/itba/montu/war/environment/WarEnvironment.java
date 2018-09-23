package ar.edu.itba.montu.war.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import ar.edu.itba.montu.abstraction.WarFieldAgent;
import ar.edu.itba.montu.interfaces.IEnvironment;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarEnvironment implements IEnvironment {

	private static WarEnvironment environment;

	private final List<Kingdom> kingdoms;
	private final List<WarFieldAgent> agents;
	private final WarStrategy strategy;

	private long time;

	private WarEnvironment(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		kingdoms.forEach(kingdom -> {
			kingdom.enforceStrategy(strategy);
		});
		this.kingdoms = kingdoms;
		this.strategy = strategy;
		this.agents = new ArrayList<>();
	}
	
	/*package*/ static void withKingdomsAndStrategy(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		if (environment == null) {
			environment = new WarEnvironment(strategy, kingdoms);
		} else {
			// TODO throw error
		}
	}

	public static WarEnvironment getInstance(){
		return environment;
	}

	private static List<Kingdom> shuffledKingdoms(final List<Kingdom> kingdoms) {
		final List<Integer> indexList = IntStream.range(0, kingdoms.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList, RandomUtil.getRandom());
		return indexList.stream().map(kingdoms::get).collect(Collectors.toList());
	}

	private static List<WarFieldAgent> shuffledAgents(final List<WarFieldAgent> agents) {
		final List<Integer> indexList = IntStream.range(0, agents.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList, RandomUtil.getRandom());
		return indexList.stream().map(agents::get).collect(Collectors.toList());
	}
	
	public void start(final long time) {
		LongStream.rangeClosed(1, time).forEach(this::loop);
	}

	public void loop(final long timeElapsed){
		time = timeElapsed;
		final List<Kingdom> shuffledKingdoms = shuffledKingdoms(kingdoms);
		final List<WarFieldAgent> shuffledAgents = shuffledAgents(agents);


		shuffledKingdoms.forEach(Kingdom::loop);
		shuffledAgents.forEach(WarFieldAgent::loop);
	}

	public List<WarFieldAgent> getAgentsFromCoordinate(Coordinate coordinate, int viewDistance){
		return agents.stream().filter(agent -> Coordinate.sees(agent.getCoordinate(),coordinate,viewDistance)).collect(Collectors.toList());
	}

	public List<Kingdom> getKingdoms() {
		return kingdoms;
	}

	public long getTime() {
		return time;
	}
}
