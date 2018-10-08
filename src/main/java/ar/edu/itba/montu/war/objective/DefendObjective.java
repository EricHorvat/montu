package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.Attacker;
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
	
	public void enforce(Attacker attacker) {
		
	}

	@Override
	public int compareTo(Objective o) {
		return priority - o.priority();
	}

	@Override
	public int priority() {
		return priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + priority;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefendObjective other = (DefendObjective) obj;
		if (priority != other.priority)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public boolean involves(final LocatableAgent agent) {
		return target.equals(agent);
	}
	
	@Override
	public LocatableAgent target() {
		return target;
	}
}
