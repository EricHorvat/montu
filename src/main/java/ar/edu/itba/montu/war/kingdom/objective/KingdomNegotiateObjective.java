package ar.edu.itba.montu.war.kingdom.objective;

import java.util.List;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.NonLocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.kingdom.Kingdom;

public class KingdomNegotiateObjective implements KingdomObjective {
	
	public static enum Intention {
		ATTACK,
		DEFEND
	}
	
	final List<? extends NonLocatableAgent> with;
	final List<? extends NonLocatableAgent> targets;
	final Intention intention;
	
	final int priority;
	
	private KingdomNegotiateObjective(final List<? extends NonLocatableAgent> with, final Intention intention, final List<? extends NonLocatableAgent> targets, final int priority) {
		this.with = with;
		this.intention = intention;
		this.targets = targets;
		this.priority = priority;
	}
	
	public static KingdomNegotiateObjective withOtherToIntentTargetsAndPriority(final List<? extends NonLocatableAgent> friendKingdoms, final Intention intention, final List<? extends NonLocatableAgent> enemyKingdoms, final int priority) {
		return new KingdomNegotiateObjective(friendKingdoms, intention, enemyKingdoms, priority);
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
		result = prime * result + ((intention == null) ? 0 : intention.hashCode());
		result = prime * result + priority;
		result = prime * result + ((targets == null) ? 0 : targets.hashCode());
		result = prime * result + ((with == null) ? 0 : with.hashCode());
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
		KingdomNegotiateObjective other = (KingdomNegotiateObjective) obj;
		if (intention != other.intention)
			return false;
		if (priority != other.priority)
			return false;
		if (targets == null) {
			if (other.targets != null)
				return false;
		} else if (!targets.equals(other.targets))
			return false;
		if (with == null) {
			if (other.with != null)
				return false;
		} else if (!with.equals(other.with))
			return false;
		return true;
	}

	@Override
	public boolean involves(final LocatableAgent agent) {
		return targets.contains(agent);
	}
	
	@Override
	public void alterPriority(int priority) {
	
	}
	
	@Override
	public List<Objective> translate() {
		return null;
	}
	
	@Override
	public Kingdom target() {
		return null;
	}
	
}
