package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.interfaces.Objective;

public class WalkObjective implements Objective {
	
	private final LocatableAgent target;
	private int priority;
	
	private WalkObjective(final LocatableAgent target, final int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public WalkObjective(final LocatableAgent target) {
		this.target = target;
		this.priority = 0;
	}
	
	@Override
	public void apply(Spawner spawner) {
		//MUST BE EMPTY TODO REVISE
	}
	
	@Override
	public int priority() {
		return 0;
	}
	
	@Override
	public void priority(int priority) {
		//MUST BE EMPTY TODO REVISE
	}
	
	
	@Override
	public boolean involves(final LocatableAgent agent) {
		return (target).equals(agent);
	}
	
	@Override
	public <T extends Agent> T target() {
		return (T)target;
	}
	
	@Override
	public int compareTo(Objective o) {
		return priority - o.priority();
	}

}
