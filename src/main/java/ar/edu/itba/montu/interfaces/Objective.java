package ar.edu.itba.montu.interfaces;

public interface Objective extends Comparable<Objective> {
	void enforce();
	int priority();
}
