package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.DefendObjective;
import ar.edu.itba.montu.war.objective.WalkObjective;

import java.util.*;

public abstract class MovingAgent extends LocatableAgent {
	
	protected PriorityQueue<Objective> targetsObjectives = new PriorityQueue<>(Comparator.comparing(o -> ((Objective) o ).priority()).reversed());
	protected String status = MovingAgentStatus.UNASSIGNED;
	

	public MovingAgent(Castle ownCastle) {
		targetsObjectives.add(new WalkObjective(ownCastle));
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
		this.displace();
	}
	
	public void assignTarget(LocatableAgent target, Integer priority) {
		for(Objective o : targetsObjectives){
			if(o.involves(target) && !(o instanceof WalkObjective)){
				o.priority(priority);
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
	
	
	public void unassign(LocatableAgent target) {
		targetsObjectives.removeIf(o -> o.target().equals(target));
		if (targetsObjectives.size() == 0){
			status = MovingAgentStatus.UNASSIGNED;
		}else {
		status = MovingAgentStatus.MOVING;
		}
	}
	
	public boolean isUnassigned() {
		return status.equals(MovingAgentStatus.UNASSIGNED);
	}
	
	public Optional<LocatableAgent> target() {
		if(targetsObjectives.size() > 0){
			return Optional.of(targetsObjectives.peek().target());
		}
		return Optional.empty();
	}
}
