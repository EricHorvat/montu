package ar.edu.itba.montu.war.people;

import java.util.Optional;

import ar.edu.itba.montu.interfaces.IPerson;
import ar.edu.itba.montu.interfaces.IWarrior;

public abstract class Warrior implements IWarrior {
	
	protected WarriorStatus status;
	protected Optional<IPerson> target;
	
	@Override
	public WarriorStatus getStatus() {
		return status;
	}
	public Optional<IPerson> getTarget() {
		return target;
	}
	
}
