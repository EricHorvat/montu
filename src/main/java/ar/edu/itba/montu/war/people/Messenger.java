package ar.edu.itba.montu.war.people;

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
	public int attackers() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void createAttackers(int attackers) {
		throw new UnsupportedOperationException("a warrior cant create attackers");
	}

	@Override
	public int availableAttackers() {
		return 1;
	}
}
