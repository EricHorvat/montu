package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.DefendObjective;
import ar.edu.itba.montu.war.objective.WalkObjective;
import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.utils.Coordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public abstract class MovingAgent extends LocatableAgent {
	
	protected PriorityQueue<Objective> targetsObjectives = new PriorityQueue<>(Comparator.comparing(o -> ((Objective) o ).priority()).reversed());
	protected String status = MovingAgentStatus.UNASSIGNED;
	protected Castle castle;
	
	private static final Logger logger = LogManager.getLogger(Castle.class);
	
	public MovingAgent(Castle ownCastle) {
		targetsObjectives.add(new WalkObjective(ownCastle));
		castle = ownCastle;
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
	
	public void assignTarget(LocatableAgent target, Double priority) {
		for (Objective o : targetsObjectives) {
			if (o.involves(target) && !(o instanceof WalkObjective)) {
				o.basePriority(priority);
				return;
			}
		}
		Objective objective;
		if (target.kingdom.equals(kingdom)) {
			objective = DefendObjective.fromWithPriority(target, priority);
		} else{
			objective = AttackObjective.headedToWithPriority(target, priority);
		}
		targetsObjectives.add(objective);
	}
	
	
	public void unassign(LocatableAgent target) {
		targetsObjectives.removeIf(o -> o.target().equals(target));
		if (targetsObjectives.size() == 0) {
			status = MovingAgentStatus.UNASSIGNED;
		} else {
			targetsObjectives.forEach(targetObjective -> targetObjective.updatePriority(
					1.0 / Double.max(
							Coordinate.distanceBetween(this.location(), targetObjective.target().location()),
							App.getConfiguration().getMinPriorityDistance())
					)
			);
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
	
	@Override
	public Castle castle() {
		return castle;
	}
}
