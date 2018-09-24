package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.Objective;

public class AttackObjective implements Objective {
	
	final LocatableAgent target;
	
	private AttackObjective(final LocatableAgent target) {
		this.target = target;
	}
	
	public static AttackObjective headedTo(final LocatableAgent target) {
		return new AttackObjective(target);
	}
	
	public void enforce() {
		
	}
}
