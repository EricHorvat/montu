package ar.edu.itba.montu.war.people;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.MovingAgent;
import ar.edu.itba.montu.abstraction.MovingAgentStatus;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Warrior extends MovingAgent {

	private static final Logger logger = LogManager.getLogger(Warrior.class);
	
	private static final int MINUTES_IN_A_DAY = 24 * 60;
	private static final int BASE_WARRIOR_COST = MINUTES_IN_A_DAY * 3;
	
	final WarriorCharacteristics warriorCharacteristics;
	final WarriorRole role;
  final boolean isSuper;
  
  Optional<LocatableAgent> immediateTarget = Optional.empty();
  
	/**
	 * Expressed in metres/delta time
	 */
//	private double speed = 5;// km per minute

	private Warrior(final Castle castle, final WarriorRole role, boolean isSuper) {
		super(castle);
		this.kingdom = castle.kingdom();
		this.location = castle.location();
		this.role = role;
		this.isSuper = isSuper;
		this.warriorCharacteristics = WarriorCharacteristics.fromCastle(castle, isSuper);
		
	}
	
	public static Warrior createWarriorInCastle(final Castle castle, WarriorRole role, boolean isSuper) {
		final Warrior w = new Warrior(castle, role, isSuper);
		
		return w;
	}
	
	/**
	 * Warrior applies simple logic to movement
	 * It always moves at the pace of his speed
	 */
	@Override
	protected void displace() {
		
		// Displace will get called only if target is not null
		if (immediateTarget.isPresent()) {
			this.location = this.location.applyingNoisyDeltaInDirectionTo(warriorCharacteristics.speed(), immediateTarget.get().location());
		} else if (target().isPresent()) {
			this.location = this.location.applyingNoisyDeltaInDirectionTo(warriorCharacteristics.speed(), target().get().location());
			/*IF WE WANT, THIS IS FOR BATTLING IN THE PATH* /
			if (Coordinate.distanceBetween(this.location,target().get().location()) < this.characteristics().viewDistance()){
				defending();
			}
			*/
		}
	}
	
	private void moveToImmediateTarget() {
		if (immediateTarget.isPresent()) {
			this.displace();
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
		if (targetsObjectives.size() > 0) {
			status = MovingAgentStatus.MOVING;
			return;
		}
		defending();
	}
	
	private void defending() {
		// get from the environment the enemies within viewing distance
		/// based on warrior characteristics; this should be called only
		final WarEnvironment environment = WarEnvironment.getInstance();
		final List<LocatableAgent> enemies = environment
				.agentsWithinRadiusOfCoordinate(this.location, warriorCharacteristics.viewDistance())
				.stream()
				.filter(e -> !e.kingdom().equals(this.kingdom()))
				.collect(Collectors.toList());
		
		if (enemies.isEmpty()) {
			return;
		}
		
		LocatableAgent enemySelected = enemies.get(0);
		
		// get the random enemy and attack him (with prioritized value)
		double prioritySum = enemies.stream().mapToDouble(attacker -> Double.max(1.0 / Coordinate.distanceBetween(this.location(), attacker.location()), App.getConfiguration().getMinPriorityDistance())).sum();
		double priorityValue = RandomUtil.getRandom().nextDouble() * prioritySum;
		for (LocatableAgent enemy: enemies) {
			priorityValue -= Double.max(
					1.0 / Coordinate.distanceBetween(this.location(),enemy.location()),
					App.getConfiguration().getMinPriorityDistance()
			);
			if (priorityValue <= 0 ) {
				enemySelected = enemy;
				break;
			}
		}
		
		logger.debug(status + " warrior of {} will attack {}", kingdom, enemySelected);
		
		this.assignTarget(enemySelected, App.getConfiguration().getMaxPriority());
	}
	
	public void assignToTarget(final LocatableAgent target, double priority) {

		if (status.equals(WarriorStatus.DEAD)) {
			return;
		}
		
		super.assignTarget(target, priority);
	}
	
	public void tick(final long timeElapsed) {

		switch (status) {
			case WarriorStatus.UNASSIGNED:
				this.unassigned(timeElapsed);
				return;
			case WarriorStatus.MOVING:
				if (immediateTarget.isPresent()) {
					if (Coordinate.distanceBetween(location, immediateTarget.get().location()) < warriorCharacteristics.attackDistance()) {
						status = WarriorStatus.ATTACKING;
						return;
					}
					this.moveToImmediateTarget();
					return;
				}
				// if we are headed toward a target then keep moving
				if (Coordinate.distanceBetween(location, target().get().location()) < warriorCharacteristics.attackDistance()) {
					if (kingdom().castles().contains(target().get())) {
						status = WarriorStatus.DEFENDING;
						return;
					}
					status = WarriorStatus.ATTACKING;
				} else {
					final List<LocatableAgent> nearbyEnemies = WarEnvironment
							.getInstance()
							.agentsWithinRadiusOfCoordinate(location, warriorCharacteristics.viewDistance())
							.stream()
							.filter(a -> !a.kingdom().equals(kingdom))
							.filter(a -> !(a instanceof Castle))
							.collect(Collectors.toList());
					if (!nearbyEnemies.isEmpty()) {
						immediateTarget = Optional.of(nearbyEnemies.get(0));
					}
					this.move();
				}
				break;
			case WarriorStatus.DEFENDING:
				if (target().isPresent()) {
					if (target().get().isAlive()) {
						this.defending();
					} else {
						unassign(target().get());
					}
				}
				return;
			case WarriorStatus.ATTACKING:
				if (immediateTarget.isPresent()) {
					if (immediateTarget.get().isAlive()) {
						if (Coordinate.distanceBetween(location, immediateTarget.get().location()) < warriorCharacteristics.attackDistance()) {
							immediateTarget.get().defend(this, warriorCharacteristics.attackHarm());
							return;
						} else {
							status = WarriorStatus.MOVING;
						}
					} else{
						immediateTarget = Optional.empty();
						status = WarriorStatus.MOVING;
					}
					return;
				} else if (target().isPresent()) {
					if (target().get().isAlive() && !target().get().kingdom().equals(kingdom())) {
						if (Coordinate.distanceBetween(location, target().get().location()) < warriorCharacteristics.attackDistance()) {
							target().get().defend(this, warriorCharacteristics.attackHarm());
							return;
						} else {
							status = WarriorStatus.MOVING;
						}
					} else{
						unassign(target().get());
						status = WarriorStatus.MOVING;
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
		if (hp < 0) {
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

	public int attackDistance() {
		return (int)(warriorCharacteristics.attackDistance());
	}

	@Override
	public boolean isAlive() {
		return !status.equals(WarriorStatus.DEAD);
	}
	
	public boolean isAvailable() {
		return status.equals(WarriorStatus.UNASSIGNED); // status != WarriorStatus.SPAWNING && status != WarriorStatus.DEAD && !target.isPresent();
	}

	public int resourcesCost(){
		/*Warn, this should be a better formula in base of WarriorCharacteristic*/
		return BASE_WARRIOR_COST * (isSuper ? 5 : 1);
	}
	
	@Override
	public String toString() {
		return this.uid().toString();// + "\n"+ status.substring(0,3) + " " + getHealthPointPercentage();
	}
	
	public void noCreated() {
		this.status = WarriorStatus.DEAD;
		this.warriorCharacteristics.healthPoints(0);
	}
	
	public WarriorRole role() {
		return role;
	}
	
	public boolean isAttacker() {
		return role.equals(WarriorRole.ATTACKER);
	}
	
	public boolean isDefender() {
		return role.equals(WarriorRole.DEFENDER);
	}
	
	public WarriorCharacteristics characteristics() {
		return this.warriorCharacteristics;
	}
	
	public boolean isSuper() {
		return isSuper;
	}
}
