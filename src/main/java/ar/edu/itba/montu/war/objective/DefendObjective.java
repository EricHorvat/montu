package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.IObjective;

public class DefendObjective implements IObjective {
	
	final LocatableAgent target;
	
	private DefendObjective(final LocatableAgent target) {
		this.target = target;
	}
	
	public static DefendObjective from(final LocatableAgent target) {
		return new DefendObjective(target);
	}
}
