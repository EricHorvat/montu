package ar.edu.itba.montu.war.environment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.Streamer;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.visual.ProcessingApplet;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarEnvironment {

	private static final Logger logger = LogManager.getLogger(WarEnvironment.class);
	
	private static WarEnvironment environment;

	private final List<Kingdom> kingdoms;
	
	private long time;

	private WarEnvironment(final List<Kingdom> kingdoms) {
		this.kingdoms = kingdoms;
	}
	
	/*package*/ static WarEnvironment withKingdoms(final List<Kingdom> kingdoms) {
		if (environment == null) {
			environment = new WarEnvironment(kingdoms);
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
		kingdoms.forEach(Kingdom::negotiate);
		Streamer.currentStreamer().streamNow(0);
		LongStream.rangeClosed(1, time).forEach(this::tick);
	}

	private void tick(final long timeElapsed) {
		logger.debug("environment ticking at={}", timeElapsed);
		
		time = timeElapsed;
		
		final List<Kingdom> shuffledKingdoms = shuffledKingdoms(kingdoms);
		final List<LocatableAgent> shuffledAgents = shuffledAgents(locatableAgents());

		shuffledKingdoms.forEach(k -> k.tick(timeElapsed));
		shuffledAgents.forEach(a -> a.tick(timeElapsed));
		updateVisual(timeElapsed);
		
		Streamer.currentStreamer().streamOnTick(timeElapsed);
	}
	
	private void updateVisual(final long timeElapsed) {
		ProcessingApplet.instance().noLoop();
		ProcessingApplet.instance().redraw();
		try{Thread.sleep(20L);}catch (InterruptedException e){}
	}

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
		Streamer.currentStreamer().streamNow(time);
		kingdoms.forEach(k -> k.castleWillDie(castle));
		kingdoms.forEach(k -> k.castleDidDie(castle));
	}

	public Long time() {
		return time;
	}
	
	
	

}
