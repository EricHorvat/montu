package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.Objective;

public class AttackObjective implements Objective {
	
	final LocatableAgent target;
	final int priority;
	
	private AttackObjective(final LocatableAgent target, final int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static AttackObjective headedToWithPriority(final LocatableAgent target, final int priority) {
		return new AttackObjective(target, priority);
	}
	
	@Override
	public void enforce() {
		
	}

	@Override
	public int priority() {
		return priority;
	}

	@Override
	public int compareTo(Objective o) {
		return priority - o.priority();
	}
}
