package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.war.castle.Castle;

import java.util.Optional;

public abstract class MovingAgent extends LocatableAgent {
	
	protected Optional<LocatableAgent> target = Optional.empty();
	protected String status = MovingAgentStatus.UNASSIGNED;
	
	protected Castle ownCastle;/*Promove to PQ, with creator as more priority*/

		public MovingAgent(Castle ownCastle) {
				this.ownCastle = ownCastle;
			}
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
			this.comeBack();
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
	
	
 
	public void comeBack(){
		assignTarget(ownCastle);
		this.status = MovingAgentStatus.MOVING;
	}
	
	public void unassign() {
		this.target = Optional.empty();
		this.status = MovingAgentStatus.UNASSIGNED;
	}
	
	public Optional<LocatableAgent> target() {
		return target;
	}
}
