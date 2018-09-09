package ar.edu.itba.montu.interfaces;

import java.util.Optional;

import ar.edu.itba.montu.war.people.WarriorStatus;

public interface IWarrior extends IPerson {
	public Optional<IPerson> getTarget();
	public void assignTarget(final IPerson person);
	public WarriorStatus getStatus();
}
