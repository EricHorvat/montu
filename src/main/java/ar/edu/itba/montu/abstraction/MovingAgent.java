package ar.edu.itba.montu.abstraction;

import java.util.Optional;

public abstract class MovingAgent extends LocatableAgent {
	
	protected Optional<LocatableAgent> target;
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
		
		this.displace();
	}
	
	public void assignTarget(LocatableAgent target) {
		this.target = Optional.ofNullable(target);
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
	
	public Optional<LocatableAgent> target() {
		return target;
	}
}
