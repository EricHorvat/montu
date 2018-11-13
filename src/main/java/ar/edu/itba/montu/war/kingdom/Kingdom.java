package ar.edu.itba.montu.war.kingdom;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.war.people.Warrior;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.NonLocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.kingdom.objective.KingdomAttackObjective;
import ar.edu.itba.montu.war.kingdom.objective.KingdomDefendObjective;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Kingdom extends Agent implements NonLocatableAgent {
	
	private static final Logger logger = LogManager.getLogger(Kingdom.class);
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	private final List<Castle> castles;
	private final List<Kingdom> enemies = new ArrayList<>();
	private final List<Kingdom> rivals = new ArrayList<>();
	private final Map<Kingdom,Integer> friends = new HashMap<>();
	private final List<KingdomObjective> objectives = new ArrayList<>();
	private long lastNegotiation;
	
	private int color = 0xffffff;
	private List<LocatableAgent> agents = new ArrayList<>();
	private KingdomStatus status = KingdomStatus.IDLE;
	private int lastFriendNumber;

	/* package */protected Kingdom(final String name, final KingdomCharacteristics kingdomCharacteristics, final List<CastleBuilder> castles) {
	  super();
	  this.name = name;
		this.characteristics = kingdomCharacteristics;
		this.castles = castles.stream().map(c -> c.kingdom(this).build()).collect(Collectors.toList());
	}

	public KingdomStatus currentStatus() {
		return status;
	}
	
	private void findFriends(int friendsToFind, List<Kingdom> possibleFriendKingdoms){
		if(Configuration.FRIEND_WEAKERS) {
			possibleFriendKingdoms = Lists.reverse(possibleFriendKingdoms);
		}
		
		possibleFriendKingdoms = possibleFriendKingdoms.stream()
			.filter(possibleFriend -> !(friends.keySet().contains(possibleFriend) || rivals.contains(possibleFriend)))
			.collect(Collectors.toList());
		
		for (Kingdom possibleFriend: possibleFriendKingdoms) {
			if (friendsToFind > 0) {
				/*EVALUATE FRIENDSHIP?*/
				if(possibleFriend.befriend(this)){
					friends.put(possibleFriend,Configuration.FRIENDSHIP_TICKS);
					friendsToFind--;
				}
			}
		}
		
		lastFriendNumber = friends.size();
	}
	
	private boolean befriend(Kingdom kingdom){
		WarEnvironment environment = WarEnvironment.getInstance();
		if (friends.size() < Configuration.FRIEND_PERCENTAGE * (environment.kingdoms().size()-1)) {
			//      rand * myPower < theirPower => more prob true if its strong
			boolean itsStrong = RandomUtil.getRandom().nextDouble() * this.power() < kingdom.power();
			// itsStrong XOR true = !itsStrong; itsStrong XOR false = itsStrong
			boolean befriend = itsStrong ^ Configuration.FRIEND_WEAKERS;
			if (befriend){
				friends.put(kingdom, Configuration.FRIENDSHIP_TICKS);
				return true;
			}
		}
		return false;
	}
	
	private void findRivals(int rivalsToFind, List<Kingdom> kingdoms){
		rivals.clear();
		enemies.clear();
		rivals.addAll(kingdoms.stream().filter(k -> !friends.keySet().contains(k)).limit(rivalsToFind).collect(Collectors.toList()));
	}
	
	public void negotiate() {
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<Kingdom> otherKingdoms = environment.kingdoms().stream().filter(k -> !k.equals(this)).collect(Collectors.toList());
		
		otherKingdoms.sort(Comparator.comparingDouble(Kingdom::power));
		int friendsToFind = (int)(Configuration.FRIEND_PERCENTAGE * otherKingdoms.size());
		int rivalsToFind = (int) (Configuration.RIVAL_PERCENTAGE* otherKingdoms.size());
		friendsToFind -= friends.size();
		
		findRivals(rivalsToFind, otherKingdoms);
		findFriends(friendsToFind, otherKingdoms);
		
		buildStrategy();
	}
	
	/**
	 * Here we build the initial strategy
	 * This happens at time=0
	 * We have to construct initial objective PQ
	 * This has to be done based on characteristics
	 */
	public void buildStrategy() {
		
		logger.debug("[{}] {} is building strategy", uid(), name);
		objectives.clear();
		
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
				if (rivals.contains(kingdom)){
					priority *= Configuration.RIVAL_PRIORITY_COEF;
				}
				if (friends.keySet().contains(kingdom)){
					priority *= Configuration.FRIEND_PRIORITY_COEF;
				}
				ko = KingdomAttackObjective.headedToWithPriority(kingdom,priority);
			}
			logger.debug("{} has objective {}", name, ko);
			objectives.add(ko);
			//}
		});
		
		return;
	}
	
	public List<Coordinate> castleCoordinates() {
		return castles.stream().map(Castle::location).collect(Collectors.toList());
	}
	
	public boolean shouldNegotiate(double timeElapsed){
		WarEnvironment warEnvironment = WarEnvironment.getInstance();
		int friendsDesiredSize = (int)(Configuration.FRIEND_PERCENTAGE * (warEnvironment.kingdoms().size() - 1));
		return (timeElapsed - lastNegotiation) >= Configuration.UPDATE_NEGOTATION_TICKS
			|| friends.size() < Math.min(friendsDesiredSize,lastFriendNumber);
	}

	public void tick(final long timeEllapsed) {
		
		logger.debug("{} tick={}", name, timeEllapsed);
		
		Iterator<Kingdom> iterator = friends.keySet().iterator();
		
		while (iterator.hasNext()){
			Kingdom friend = iterator.next();
			int updatedValue = friends.get(friend) -1;
			if(updatedValue <= 0){
				iterator.remove();
			}else{
				friends.replace(friend,updatedValue);
			}
		}
		
		/*for (Kingdom friend: friends.keySet()) {
			int updatedValue = friends.get(friend) -1;
			if(updatedValue <= 0){
				friends.remove(friend);
			}else{
				friends.replace(friend,updatedValue);
			}
		}*/
		
		if(shouldNegotiate(timeEllapsed)){
			lastNegotiation = timeEllapsed;
			negotiate();
		}
		
		agents = agents.stream().filter(LocatableAgent::isAlive).collect(Collectors.toList());
	}
	
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
		return enemies.contains(k);
	}
	
	public void addEnemy(Kingdom k){
		if (!isEnemy(k))
			enemies.add(k);
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
