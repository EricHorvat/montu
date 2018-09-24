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
	
	private NegotiateObjective(final List<? extends NonLocatableAgent> with, final Intention intention, final List<? extends NonLocatableAgent> targets) {
		this.with = with;
		this.intention = intention;
		this.targets = targets;
	}
	
	public static NegotiateObjective withOtherToIntentTargets(final List<? extends NonLocatableAgent> friendKingdoms, final Intention intention, final List<? extends NonLocatableAgent> enemyKingdoms) {
		return new NegotiateObjective(friendKingdoms, intention, enemyKingdoms);
	}
	
	public void enforce() {
		
	}
}
