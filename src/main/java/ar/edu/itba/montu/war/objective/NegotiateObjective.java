package ar.edu.itba.montu.war.objective;

import java.util.List;
import java.util.Objects;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.NonLocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.interfaces.Objective;

public class NegotiateObjective implements Objective {
	
	public static enum Intention {
		ATTACK,
		DEFEND
	}
	
	final private List<? extends NonLocatableAgent> with;
	final private List<? extends NonLocatableAgent> targets;
	final private Intention intention;
	
	private double priority;
	private double basePriority;
	
	private NegotiateObjective(final List<? extends NonLocatableAgent> with, final Intention intention, final List<? extends NonLocatableAgent> targets, final int priority) {
		this.with = with;
		this.intention = intention;
		this.targets = targets;
		this.priority = priority;
		this.basePriority = priority;
	}
	
	public static NegotiateObjective withOtherToIntentTargetsAndPriority(final List<? extends NonLocatableAgent> friendKingdoms, final Intention intention, final List<? extends NonLocatableAgent> enemyKingdoms, final int priority) {
		return new NegotiateObjective(friendKingdoms, intention, enemyKingdoms, priority);
	}

	@Override
	public int compareTo(Objective o) {
		return Double.compare(priority, o.priority());
	}
	
	@Override
	public double priority() {
		return priority;
	}
	
	@Override
	public void basePriority(double priority) {
		basePriority = priority;
	}
	
	@Override
	public void updatePriority(double coefficient) {
		this.priority = basePriority * coefficient;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NegotiateObjective that = (NegotiateObjective) o;
		return Double.compare(that.priority, priority) == 0 &&
			Double.compare(that.basePriority, basePriority) == 0 &&
			Objects.equals(with, that.with) &&
			Objects.equals(targets, that.targets) &&
			intention == that.intention;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(with, targets, intention, priority, basePriority);
	}
	
	@Override
	public boolean involves(final LocatableAgent agent) {
		return targets.contains(agent);
	}
	
	@Override
	public <T extends LocatableAgent> T target() {
		return (T)targets.get(0); /*TODO CHECK*/
	}
	
	@Override
	public void apply(Spawner callerAgent) {
	
	}
}
