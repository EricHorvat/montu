package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.people.WarriorRole;

import java.util.List;

public interface Spawner {
	List<LocatableAgent> createWarriors(final int quantity, WarriorRole role );
	List<Warrior> availableAttackers();
	List<Warrior> availableDefenders();
	List<Warrior> availableWarriors();
}