package ar.edu.itba.montu.interfaces;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;

public interface Objective extends Comparable<Objective> {
	void apply(Spawner spawner);
	int priority();
	boolean involves(final LocatableAgent agent);
	<T extends Agent> T target();
}
