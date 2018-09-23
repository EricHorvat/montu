package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.war.utils.Coordinate;

public abstract class LocatableAgent extends Agent {
	
	protected Coordinate location;
	
	public Coordinate location() {
		return location;
	}
}
