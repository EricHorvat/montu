package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.DefendObjective;

import java.util.*;

public abstract class MovingAgent extends LocatableAgent {
	
	protected PriorityQueue<Objective> targetsObjectives = new PriorityQueue<>();
	protected String status = MovingAgentStatus.UNASSIGNED;
	
	protected Castle ownCastle;/*Promove to PQ, with creator as more priority*/

		public MovingAgent(Castle ownCastle) {
			this.ownCastle = ownCastle;
			this.assignTarget(ownCastle,Integer.MIN_VALUE);
			}
	/**
	 * The subclass should describe how the target
	 * should be pursued
	 */
	protected abstract void displace();
	
	protected void move() {
		// don't move if there is no target present 
		if (!target().isPresent()) {
			return;
		}
		
		if (!target().get().isAlive()) {
			this.comeBack();
		}
		
		this.displace();
	}
	
	public void assignTarget(LocatableAgent target, Integer priority) {
		for(Objective o : targetsObjectives){
			if(o.involves(target)){
				//TODO? o.priority(priority);
				return;
			}
		}
		Objective ob;
		if (target.kingdom.equals(kingdom)){
			ob = DefendObjective.fromWithPriority(target,priority);
		}else{
			ob = AttackObjective.headedToWithPriority(target,priority);
		}
		targetsObjectives.add(ob);
	}
	
	
 
	public void comeBack(){
		assignTarget(ownCastle, Integer.MIN_VALUE);
		this.status = MovingAgentStatus.MOVING;
	}
	
	public void unassign(LocatableAgent target) {
		targetsObjectives.removeIf(o -> o.target().equals(target));
	}
	
	public Optional<LocatableAgent> target() {
		if(targetsObjectives.size() > 0){
			return Optional.of(targetsObjectives.peek().target());
		}
		return Optional.empty();
	}
}
