package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;

public abstract class LocatableAgent extends Agent {
	
	protected Coordinate location;
	protected Kingdom kingdom;
	
	public Coordinate location() {
		return location;
	}
	
	public Kingdom kingdom() {
		return kingdom;
	}
}
