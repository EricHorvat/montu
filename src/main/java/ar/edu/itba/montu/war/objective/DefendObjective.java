package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.Objective;

public class DefendObjective implements Objective {
	
	final LocatableAgent target;
	
	private DefendObjective(final LocatableAgent target) {
		this.target = target;
	}
	
	public static DefendObjective from(final LocatableAgent target) {
		return new DefendObjective(target);
	}
	
	public void enforce() {
		
	}
}
