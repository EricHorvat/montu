package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.Objective;

public class DefendObjective implements Objective {
	
	final private LocatableAgent target;
	final private int priority;
	
	private DefendObjective(final LocatableAgent target, final int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static DefendObjective fromWithPriority(final LocatableAgent target, final int priority) {
		return new DefendObjective(target, priority);
	}
	
	public void enforce() {
		
	}

	@Override
	public int compareTo(Objective o) {
		return priority - o.priority();
	}

	@Override
	public int priority() {
		return priority;
	}
}
