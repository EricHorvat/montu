package ar.edu.itba.montu.interfaces;

import java.util.List;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;

public interface KingdomObjective extends Comparable<KingdomObjective> {
	double priority();
	void alterPriority(double priority);
	boolean involves(final LocatableAgent agent);
	
	List<Objective> translate(Coordinate location);
	
	Kingdom target();
}
