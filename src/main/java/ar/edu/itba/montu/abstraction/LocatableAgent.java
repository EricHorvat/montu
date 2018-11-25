package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.visual.VisualAgent;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;

public abstract class LocatableAgent extends Agent{
	
	protected Coordinate location;
	protected Kingdom kingdom;
	
    private VisualAgent visualAgent;
    
    public LocatableAgent() {
    	super();
	    visualAgent = VisualAgent.buildNew(uid(),this);
    }
    
	public Coordinate location() {
		return location;
	}
	
	public Kingdom kingdom() {
		return kingdom;
	}

	public abstract void defend(LocatableAgent agent, double harm);

	public abstract boolean isAlive();

  public abstract int getHealthPointPercentage();
  
  public abstract Castle castle();
}
