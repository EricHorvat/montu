package ar.edu.itba.montu.war.objective;

import java.util.List;

import ar.edu.itba.montu.abstraction.NonLocatableAgent;
import ar.edu.itba.montu.interfaces.Objective;

public class NegotiateObjective implements Objective {
	
	public static enum Intention {
		ATTACK,
		DEFEND
	}
	
	final List<? extends NonLocatableAgent> with;
	final List<? extends NonLocatableAgent> targets;
	final Intention intention;
	
	final int priority;
	
	private NegotiateObjective(final List<? extends NonLocatableAgent> with, final Intention intention, final List<? extends NonLocatableAgent> targets, final int priority) {
		this.with = with;
		this.intention = intention;
		this.targets = targets;
		this.priority = priority;
	}
	
	public static NegotiateObjective withOtherToIntentTargetsAndPriority(final List<? extends NonLocatableAgent> friendKingdoms, final Intention intention, final List<? extends NonLocatableAgent> enemyKingdoms, final int priority) {
		return new NegotiateObjective(friendKingdoms, intention, enemyKingdoms, priority);
	}
	
	@Override
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
