package ar.edu.itba.montu.war.people;

import java.util.Arrays;
import java.util.List;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.MovingAgent;

public class Messenger extends MovingAgent {

	/**
	 * Expressed in metres/delta time
	 */
	private double speed;
	
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
	
	private void unassigned(final long timeEllapsed) {
		
	}
	
	public void tick(final long timeElapsed) {
		
		switch (status) {
			case WarriorStatus.UNASSIGNED:
				this.unassigned(timeElapsed);
				return;
			case WarriorStatus.MOVING:
				// if we are headed toward a target then keep moving
				///TODO: attack or dodge depending on characteristics
		}
		
	}

	@Override
	public List<Messenger> attackers() {
		return Arrays.asList(this);
	}

	@Override
	public Warrior createAnAttacker() {
		throw new UnsupportedOperationException("a warrior cant create attackers");
	}

	@Override
	public List<Messenger> availableAttackers() {
		return Arrays.asList(this);
	}
	
	@Override
	public List<Messenger> availableDefenders() {
		return Arrays.asList(this);
	}
	
	@Override
	public int getHealthPointPercentage() {
		return 0;
	}

	@Override
	public void defend(LocatableAgent agent, double damageSkill) {

	}

	@Override
	public boolean isAlive() {
		return false;
	}
}
