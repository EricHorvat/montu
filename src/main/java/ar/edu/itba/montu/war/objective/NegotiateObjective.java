package ar.edu.itba.montu.war.objective;

import java.util.List;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.IObjective;

public class NegotiateObjective implements IObjective {
	
	public static enum Intention {
		ATTACK,
		DEFEND
	}
	
	final List<LocatableAgent> with;
	final List<LocatableAgent> targets;
	final Intention intention;
	
	private NegotiateObjective(final List<LocatableAgent> with, final Intention intention, final List<LocatableAgent> targets) {
		this.with = with;
		this.intention = intention;
		this.targets = targets;
	}
	
	public static NegotiateObjective withOtherToIntentTargets(final List<LocatableAgent> with, final Intention intention, final List<LocatableAgent> targets) {
		return new NegotiateObjective(with, intention, targets);
	}
}
