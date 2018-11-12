package ar.edu.itba.montu.interfaces;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;

public interface Objective extends Comparable<Objective> {
	void apply(Spawner spawner);
	double priority();
	void updatePriority(double coefficient);
	void basePriority(double priority);
	boolean involves(final LocatableAgent agent);
	<T extends LocatableAgent> T target();
}