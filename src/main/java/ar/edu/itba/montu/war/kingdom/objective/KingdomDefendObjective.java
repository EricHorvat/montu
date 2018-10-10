package ar.edu.itba.montu.war.kingdom.objective;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.kingdom.Kingdom;

import java.util.List;

public class KingdomDefendObjective implements KingdomObjective {
	
	final private LocatableAgent target;
	final private int priority;
	
	private KingdomDefendObjective(final LocatableAgent target, final int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static KingdomDefendObjective fromWithPriority(final LocatableAgent target, final int priority) {
		return new KingdomDefendObjective(target, priority);
	}

	@Override
	public int compareTo(KingdomObjective o) {
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
		KingdomDefendObjective other = (KingdomDefendObjective) obj;
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
	public Kingdom target() {
		return null; /*TODO*/
	}
	
	@Override
	public boolean involves(final LocatableAgent agent) {
		return target.equals(agent);
	}
	
	@Override
	public void alterPriority(int priority) {
	
	}
	
	@Override
	public List<Objective> translate() {
		return null;
	}
}
