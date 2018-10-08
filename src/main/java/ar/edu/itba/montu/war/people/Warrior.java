package ar.edu.itba.montu.war.people;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.Attacker;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.MovingAgent;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Warrior extends MovingAgent {

	private static final Logger logger = LogManager.getLogger(Castle.class);
	
	final WarriorCharacteristics warriorCharacteristics;

	/**
	 * Expressed in metres/delta time
	 */
	private double speed = 0.03; // km per minute

	private Warrior(final Castle castle, final Coordinate xy) {
		super(castle);
		this.kingdom = castle.kingdom();
		this.location = xy;
		this.warriorCharacteristics = WarriorCharacteristics.standardCharacteristics();
	}
	
	public static Warrior createWithCharacteristicsInKingdomAtLocation(final Coordinate xy,  final CastleCharacteristics characteristics, final Castle castle) {
		final Warrior w = new Warrior(castle, xy);
		
		return w;
	}
	
	/**
	 * Warrior applies simple logic to movement
	 * It always moves at the pace of his speed
	 */
	@Override
	protected void displace() {
		// Displace will get called only if target is no null
		if (target.isPresent()) {
			this.location = this.location.applyingNoisyDeltaInDirectionTo(speed, target.get().location());
		}else {
			comeBack();
		}
	}
	
	public String status() {
		return status;
	}
	
	private void unassigned(final long timeEllapsed) {
	// if a warrior is unassigned
		// 1. He shouldn't move
		// 2. He only checks if there are
		//    any enemies around ONLY to defend
		//    himself unless attack-to-defend
		//    ratio is high

		final WarEnvironment environment = WarEnvironment.getInstance();
		// get from the environment the enemies within viewing distance
		/// TODO: remove hardcoded 500 value, it should be computed
		/// based on warrior characteristics
		final List<LocatableAgent> enemies = environment.agentsWithinRadiusOfCoordinate(this.location, 5);
		
		final List<Warrior> attackingEnemies =
				enemies.stream()
					.filter(e -> e.getClass().equals(Warrior.class))
					.map(e -> (Warrior)e)
					.filter(w -> w.status().equals(WarriorStatus.ATTACKING))
					.collect(Collectors.toList());
		
		if (attackingEnemies.isEmpty()) {
			return;
		}
		
		// get the closest enemy and attack him
		/// TODO: this should be done only when attack to defense ratio is big enough
		final Warrior enemy = attackingEnemies.stream()
				.sorted(Comparator.comparingDouble(e -> e.location.distanceTo(location)))
				.findFirst()
				.get();
		
		logger.debug("Unassigned warrior of {} will attack {}", kingdom, enemy);
		
		this.assignTarget(enemy);
	}
	
	public void assignToTarget(final LocatableAgent target) {

		/*TODO IF ALREADY ASSIGNED?*/
		
		if (status == WarriorStatus.DEAD) {
			return;
		}
		
		super.assignTarget(target);
	}
	
	public void tick(final long timeElapsed) {

		switch (status) {
			case WarriorStatus.UNASSIGNED:
				this.unassigned(timeElapsed);
				return;
			case WarriorStatus.MOVING:
				// if we are headed toward a target then keep moving
				///TODO: attack or dodge depending on characteristics
				if (Coordinate.distanceBetween(location, target.get().location()) < warriorCharacteristics.attackDistance()) {
					if (target.get().equals(ownCastle)){
						status = WarriorStatus.UNASSIGNED;
						return;
					}
					status = WarriorStatus.ATTACKING;
				} else {
					this.move();
				}
				break;
			case WarriorStatus.ATTACKING:
				if (target.isPresent() && target.get().isAlive()) {
					if (Coordinate.distanceBetween(location, target.get().location()) < warriorCharacteristics.attackDistance()) {
						target.get().defend(this, warriorCharacteristics.attack());
						return;
					}
				}
				comeBack();
			case WarriorStatus.DEFENDING:
				this.unassigned(timeElapsed);
				break;
			case WarriorStatus.DEAD:
				// I'm f*ing dead
				break;
		}

	}
	
	public void toDefend(){
		status = WarriorStatus.DEFENDING;
	}

	@Override
	public List<Warrior> attackers() {
		return Arrays.asList(this);
	}

	@Override
	public Attacker createAnAttacker() {
		throw new UnsupportedOperationException("a warrior cant create attackers");
	}
	
	@Override
	public List<Warrior> availableAttackers() {
		return Arrays.asList(this);
	}
	
	@Override
	public List<Warrior> availableDefenders() {
		return Arrays.asList(this);
	}

	@Override
	public void defend(LocatableAgent agent, double damageSkill) {
		double hp = warriorCharacteristics.healthPoints() - damageSkill;
		if (hp < 0){
			logger.debug("Warrior of kingdom {} is dead", kingdom);
			status = WarriorStatus.DEAD;
			hp = 0;
		}
		warriorCharacteristics.healthPoints(hp);
	}

	@Override
	public int getHealthPointPercentage() {
		return (int)(100 * warriorCharacteristics.healthPercentage());
	}

	public int getAttackD() {
		return (int)(warriorCharacteristics.attackDistance());
	}

	@Override
	public boolean isAlive() {
		return !status.equals(WarriorStatus.DEAD);
	}
	
	public boolean isAvailable() {
		return status.equals(WarriorStatus.UNASSIGNED); // status != WarriorStatus.SPAWNING && status != WarriorStatus.DEAD && !target.isPresent();
	}
	
	public boolean isDefending() {
		return status.equals(WarriorStatus.DEFENDING); // status != WarriorStatus.SPAWNING && status != WarriorStatus.DEAD && !target.isPresent();
	}

	public int gasCost(){
		/*TODO FORMULA*/
		return 12;
	}
	
	@Override
	public String toString() {
		//return this.hashCode() + "";
		return status.substring(0,3);//this.hashCode() + "";
	}
	
	public void noCreated(){
		this.status = WarriorStatus.DEAD;
		this.warriorCharacteristics.healthPoints(0);
	}
}
