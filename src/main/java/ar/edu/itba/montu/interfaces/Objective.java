package ar.edu.itba.montu.interfaces;

import ar.edu.itba.montu.abstraction.Attacker;

public interface Objective extends Comparable<Objective> {
	<T extends Attacker> void enforce(T agent);
	int priority();
}
