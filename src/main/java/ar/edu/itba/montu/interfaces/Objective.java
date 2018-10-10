package ar.edu.itba.montu.interfaces;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;

public interface Objective extends Comparable<Objective> {
	<T extends Agent> void enforce(T agent);
	int priority();
	boolean involves(final LocatableAgent agent);
	<T extends Agent> T target();
}
