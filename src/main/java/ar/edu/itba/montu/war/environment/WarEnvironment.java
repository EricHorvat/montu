package ar.edu.itba.montu.war.environment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.visual.ProcessingApplet;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarEnvironment {

	private static WarEnvironment environment;

	private final List<Kingdom> kingdoms;
	private final WarStrategy strategy;
	ProcessingApplet processingApplet;

	private WarEnvironment(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		kingdoms.forEach(kingdom -> {
			kingdom.enforceStrategy(strategy);
		});
		this.kingdoms = kingdoms;
		this.strategy = strategy;
		String[] processingArgs = {"MySketch"};
		this.processingApplet = new ProcessingApplet(520);
		ProcessingApplet.runSketch(processingArgs,processingApplet);

	}
	
	/*package*/ static WarEnvironment withKingdomsAndStrategy(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		if (environment == null) {
			environment = new WarEnvironment(strategy, kingdoms);
		}
		return environment;
	}

	public static WarEnvironment getInstance() {
		return environment;
	}

	private static List<Kingdom> shuffledKingdoms(final List<Kingdom> kingdoms) {
		final List<Integer> indexList = IntStream.range(0, kingdoms.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList, RandomUtil.getRandom());
		return indexList.stream().map(kingdoms::get).collect(Collectors.toList());
	}

	private static List<LocatableAgent> shuffledAgents(final List<LocatableAgent> agents) {
		final List<Integer> indexList = IntStream.range(0, agents.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList, RandomUtil.getRandom());
		return indexList.stream().map(agents::get).collect(Collectors.toList());
	}
	
	public void start(final long time) {
		// before we start kingdoms should build initial strategy
		// we could think about it as time=0
		// since we are using LongStream.rangeClosed(-> 1 <-, time)
		// from time =1
		kingdoms.forEach(Kingdom::buildInitialStrategy);
		LongStream.rangeClosed(1, time).forEach(this::tick);
	}

	private void tick(final long timeElapsed) {
		final List<Kingdom> shuffledKingdoms = shuffledKingdoms(kingdoms);
		final List<LocatableAgent> shuffledAgents = shuffledAgents(locatableAgents());


		shuffledKingdoms.forEach(k -> k.tick(timeElapsed));
		shuffledAgents.forEach(a -> a.tick(timeElapsed));
		this.processingApplet.noLoop();
		this.processingApplet.redraw();
		try{Thread.sleep(100L);}catch (InterruptedException e){}
	}

//	public List<WarFieldAgent> getAgentsFromCoordinate(Coordinate coordinate, int viewDistance){
//		return agents.stream().filter(agent -> Coordinate.sees(agent.getCoordinate(),coordinate,viewDistance)).collect(Collectors.toList());
//	}

	public List<Kingdom> kingdoms() {
		return kingdoms;
	}
	
	public List<LocatableAgent> locatableAgents() {
		return kingdoms.stream().map(Kingdom::agents).flatMap(List::stream).collect(Collectors.toList());
	}
	
	public List<LocatableAgent> agentsWithinRadiusOfCoordinate(final Coordinate coordinate, final double radius) {
		return locatableAgents().stream().filter(agent -> coordinate.distanceTo(agent.location()) <= radius).collect(Collectors.toList());
	}

	public void onCastleDeath(final Castle castle) {
		kingdoms.forEach(k -> k.onCastleDeath(castle));
	}
	

}
