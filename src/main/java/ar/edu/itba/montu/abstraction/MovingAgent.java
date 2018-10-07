package ar.edu.itba.montu.abstraction;

import java.util.Optional;

public abstract class MovingAgent extends LocatableAgent {
	
	protected Optional<LocatableAgent> target = Optional.empty();
	protected String status = MovingAgentStatus.UNASSIGNED;
	
	/**
	 * The subclass should describe how the target
	 * should be pursued
	 */
	protected abstract void displace();
	
	protected void move() {
		// don't move if there is no target present 
		if (!target.isPresent()) {
			return;
		}
		
		if (!target.get().isAlive()) {
			this.unassign();
		}
		
		this.displace();
	}
	
	public void assignTarget(LocatableAgent target) {
		Optional<LocatableAgent> nextTarget = Optional.ofNullable(target); 
		if (this.target.equals(nextTarget)) return;
		this.target = nextTarget;
		if (this.target.isPresent()) {
			this.status = MovingAgentStatus.MOVING;
		} else {
			this.status = MovingAgentStatus.UNASSIGNED;
		}
	}
	
	public void unassign() {
		this.target = Optional.empty();
		this.status = MovingAgentStatus.UNASSIGNED;
	}
	
	public boolean isUnassigned() {
		return status == MovingAgentStatus.UNASSIGNED;
	}
	
	public Optional<LocatableAgent> target() {
		return target;
	}
}
