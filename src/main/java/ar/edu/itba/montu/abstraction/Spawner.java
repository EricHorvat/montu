package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.DefendObjective;
import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.people.WarriorRole;

import java.util.List;

public interface Spawner {
	List<Warrior> createWarriors(final int quantity, WarriorRole role );
	List<Warrior> availableAttackers();
	List<Warrior> availableDefenders();
	List<Warrior> availableWarriors();
	LocatableAgent asLocatableAgent();
	List<Objective> attackObjectives();
	List<Objective> defendObjectives();
}