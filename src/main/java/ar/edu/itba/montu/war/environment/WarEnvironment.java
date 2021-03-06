package ar.edu.itba.montu.war.environment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.people.WarriorStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.Streamer;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.visual.ProcessingApplet;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

import static ar.edu.itba.montu.visual.ProcessingApplet.paused;

public class WarEnvironment {

	private static final Logger logger = LogManager.getLogger(WarEnvironment.class);
	
	private static WarEnvironment environment;

	private final List<Kingdom> kingdoms;
	
	private boolean ended = false;
	
	private long time;
	private WarStrategy strategy;

	private WarEnvironment(final List<Kingdom> kingdoms, final WarStrategy strategy) {
		this.kingdoms = kingdoms;
		this.strategy = strategy;
	}
	
	/*package*/ static WarEnvironment withKingdomsAndStrategy(final List<Kingdom> kingdoms, final WarStrategy strategy) {
		if (environment == null) {
			environment = new WarEnvironment(kingdoms, strategy);
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
		
		long timeElapsed = 0L;
		while(!ended) {
			if (!paused) {
				tick(timeElapsed);
				timeElapsed++;
			}
			if (App.getConfiguration().getViewport().isEnabled()) {
				updateVisual(timeElapsed);
			}
		}
		logger.info("Time elapsed {}", timeElapsed);
	}

	private void tick(final long timeElapsed) {
		logger.debug("environment ticking at={}", timeElapsed);
		
		time = timeElapsed;
		
		final List<Kingdom> shuffledKingdoms = shuffledKingdoms(kingdoms);
		final List<LocatableAgent> shuffledAgents = shuffledAgents(locatableAgents());

		shuffledKingdoms.forEach(k -> k.tick(timeElapsed));
		shuffledAgents.forEach(a -> a.tick(timeElapsed));
		
		Streamer.currentStreamer().streamOnTick(timeElapsed);
		
		checkEndCondition();
	}
	
	private void checkEndCondition(){
		ended = locatableAgents().stream().filter(agent -> agent instanceof Castle).map(LocatableAgent::kingdom).distinct().count() <= 1;
		ended &= locatableAgents().stream().filter(agent -> !(agent instanceof Castle)).map(agent -> ((Warrior)agent).status().equals(WarriorStatus.DEFENDING) || ((Warrior)agent).status().equals(WarriorStatus.UNASSIGNED)).reduce(Boolean::logicalAnd).orElse(true);
	}
	
	private void updateVisual(final long timeElapsed) {
		ProcessingApplet.instance().noLoop();
		ProcessingApplet.instance().redraw();
		long updateEvery = App.getConfiguration().getViewport().getUpdateEvery();
		if (updateEvery > 0) {
			try {
				Thread.sleep(updateEvery);
			} catch (InterruptedException e) {
			}
		}
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
	
	public WarStrategy strategy() {
		return strategy;
	}
	

}
