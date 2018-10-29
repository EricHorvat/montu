package ar.edu.itba.montu.war.kingdom.objective;

import java.util.List;
import java.util.Objects;

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
	
	final double priority;
	
	private KingdomNegotiateObjective(final List<? extends NonLocatableAgent> with, final Intention intention, final List<? extends NonLocatableAgent> targets, final double priority) {
		this.with = with;
		this.intention = intention;
		this.targets = targets;
		this.priority = priority;
	}
	
	public static KingdomNegotiateObjective withOtherToIntentTargetsAndPriority(final List<? extends NonLocatableAgent> friendKingdoms, final Intention intention, final List<? extends NonLocatableAgent> enemyKingdoms, final double priority) {
		return new KingdomNegotiateObjective(friendKingdoms, intention, enemyKingdoms, priority);
	}

	@Override
	public int compareTo(KingdomObjective o) {
		return Double.compare(priority, o.priority());
	}

	@Override
	public double priority() {
		return priority;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KingdomNegotiateObjective that = (KingdomNegotiateObjective) o;
		return Double.compare(that.priority, priority) == 0 &&
			Objects.equals(with, that.with) &&
			Objects.equals(targets, that.targets) &&
			intention == that.intention;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(with, targets, intention, priority);
	}
	
	@Override
	public boolean involves(final LocatableAgent agent) {
		return targets.contains(agent);
	}
	
	@Override
	public void alterPriority(double priority) {
	
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
