package ar.edu.itba.montu.war.people;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.MovingAgent;
import ar.edu.itba.montu.war.environment.WarEnvironment;

public class Warrior extends MovingAgent {

	/**
	 * Expressed in metres/delta time
	 */
	private double speed;
	
	private double attack;
	private double defense;
	
	private double health;
	
	/**
	 * Warrior applies simple logic to movement
	 * It always moves at the pace of his speed
	 */
	@Override
	protected void displace() {
		// Displace will get called only if target is no null
		this.location = this.location.applyingDeltaInDirectionTo(speed, target.get().location());
	}
	
	public String status() {
		return status;
	}
	
	public void tick(final long timeElapsed) {
		
		switch (status) {
			case WarriorStatus.UNASSIGNED:
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
				final List<LocatableAgent> enemies = environment.agentsWithinRadiusOfCoordinate(this.location, 500);
				
				final List<Warrior> attackingEnemies =
						enemies.stream()
							.filter(e -> e.getClass().equals(Warrior.class))
							.map(e -> (Warrior)e)
							.filter(w -> w.status().equals(WarriorStatus.ATTACKING))
							.collect(Collectors.toList());
				
				if (attackingEnemies.isEmpty()) {
					break;
				}
				
				// get the closest enemy and attack him
				/// TODO: this should be done only when attack to defense ratio is big enough
				final Warrior enemy = attackingEnemies.stream()
						.sorted(Comparator.comparingDouble(e -> e.location.distanceTo(location)))
						.findFirst()
						.get();
				
				this.assignTarget(enemy);
				
				break;
			case WarriorStatus.MOVING:
				// if we are headed toward a target then keep moving
				///TODO: attack or dodge depending on characteristics
				
				this.move();
				break;
			case WarriorStatus.ATTACKING:
				break;
			case WarriorStatus.DEFENDING:
				break;
			case WarriorStatus.DEAD:
				// I'm f*ing dead
				break;
		}
		
	}
}
