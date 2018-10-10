package ar.edu.itba.montu.war.people;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.MovingAgent;
import ar.edu.itba.montu.abstraction.MovingAgentStatus;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Warrior extends MovingAgent {

	private static final Logger logger = LogManager.getLogger(Castle.class);
	
	private static final int MINUTES_IN_A_DAY = 24 * 60;
	private static final int BASE_WARRIOR_COST = MINUTES_IN_A_DAY * 3;
	
	final WarriorCharacteristics warriorCharacteristics;
	final WarriorRole role;

	/**
	 * Expressed in metres/delta time
	 */
	private double speed = 5;// km per minute

	private Warrior(final Castle castle, final WarriorRole role) {
		super(castle);
		this.kingdom = castle.kingdom();
		this.location = castle.location();
		this.role = role;
		this.warriorCharacteristics = WarriorCharacteristics.fromCastleCharacteristics(castle.characteristics());
	}
	
	public static Warrior createDefenderInCastle(final Castle castle) {
		final Warrior w = new Warrior(castle, WarriorRole.DEFENDER);
		
		return w;
	}
	
	public static Warrior createAttackerInCastle(final Castle castle) {
		final Warrior w = new Warrior(castle, WarriorRole.ATTACKER);
		
		return w;
	}
	
	/**
	 * Warrior applies simple logic to movement
	 * It always moves at the pace of his speed
	 */
	@Override
	protected void displace() {
		// Displace will get called only if target is no null
		if (target().isPresent()) {
			this.location = this.location.applyingNoisyDeltaInDirectionTo(speed, target().get().location());
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
		
		if (targetsObjectives.size() >0 ){
			status = MovingAgentStatus.MOVING;
			return;
		}
		// get from the environment the enemies within viewing distance
		/// TODO: remove hardcoded 500 value, it should be computed
		/// based on warrior characteristics
		final List<LocatableAgent> enemies = environment.agentsWithinRadiusOfCoordinate(this.location, warriorCharacteristics.viewDistance());
		
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
		
		this.assignTarget(enemy, RandomUtil.getRandom().nextInt(1000));
	}
	
	public void assignToTarget(final LocatableAgent target, int priority) {

		/*TODO IF ALREADY ASSIGNED?*/
		
		if (status == WarriorStatus.DEAD) {
			return;
		}
		
		super.assignTarget(target,priority);
	}
	
	public void tick(final long timeElapsed) {

		switch (status) {
			case WarriorStatus.UNASSIGNED:
				this.unassigned(timeElapsed);
				return;
			case WarriorStatus.MOVING:
				// if we are headed toward a target then keep moving
				///TODO: attack or dodge depending on characteristics
				if (Coordinate.distanceBetween(location, target().get().location()) < warriorCharacteristics.attackDistance()) {
					if (target().get().equals(ownCastle)){
						status = WarriorStatus.UNASSIGNED;
						return;
					}
					status = WarriorStatus.ATTACKING;
				} else {
					this.move();
				}
				break;
			case WarriorStatus.ATTACKING:
				if (target().isPresent()){
					if(target().get().isAlive()) {
						if (Coordinate.distanceBetween(location, target().get().location()) < warriorCharacteristics.attackDistance()) {
							target().get().defend(this, warriorCharacteristics.attackHarm());
							return;
						}
					}else{
						unassign(target().get());
					}
				}
			case WarriorStatus.DEAD:
				// I'm f*ing dead
				break;
		}

	}

	@Override
	public void defend(LocatableAgent agent, int harm) {
		int hp = warriorCharacteristics.healthPoints() - harm;
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

	public int gasCost(){
		/*TODO FORMULA*/
		return BASE_WARRIOR_COST;
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
	
	public WarriorRole role() {
		return role;
	}
	
	public boolean isAttacker(){
		return role.equals(WarriorRole.ATTACKER);
	}
	
	public boolean isDefender(){
		return role.equals(WarriorRole.ATTACKER);
	}
}
