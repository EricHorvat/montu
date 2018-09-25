package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.visual.VisualAgent;
import ar.edu.itba.montu.war.utils.Coordinate;

public abstract class LocatableAgent extends Agent {

	private VisualAgent visualAgent;

	public LocatableAgent() {
		super();
		visualAgent = new VisualAgent(getUid(),this);
	}

	protected Coordinate location;
	
	public Coordinate location() {
		return location;
	}
}
