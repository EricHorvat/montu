package ar.edu.itba.montu.war.kingdom.objective;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.objective.AttackObjective;

public class KingdomAttackObjective implements KingdomObjective {
	
	final private Kingdom target;
	private int priority;
	
	private KingdomAttackObjective(final Kingdom target, final int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static KingdomAttackObjective headedToWithPriority(final Kingdom target, final int priority) {
		return new KingdomAttackObjective(target, priority);
	}
	
	@Override
	public void enforce(final Agent a) {
	
	}

	@Override
	public int priority() {
		return priority;
	}
	
	@Override
	public void alterPriority(final int priority) {
		this.priority = priority;
	} 

	@Override
	public int compareTo(KingdomObjective o) {
		return priority - o.priority();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + priority;
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
		KingdomAttackObjective other = (KingdomAttackObjective) obj;
//		if (priority != other.priority)
//			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public boolean involves(final LocatableAgent agent) {
		return target.castles().equals(agent);
	}

	@Override
	public List<Objective> translate() {
		return target.castles().stream()
				.map(castle -> AttackObjective.headedToWithPriority(castle, priority))
				.collect(Collectors.toList());
	}

	@Override
	public Kingdom target() {
		return target;
	}

	@Override
	public String toString() {
		return "KAObjective [target=" + target + ", priority=" + priority + "]";
	}
	
	
	
	
}
