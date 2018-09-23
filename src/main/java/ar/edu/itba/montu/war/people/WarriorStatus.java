package ar.edu.itba.montu.war.people;

import ar.edu.itba.montu.abstraction.MovingAgentStatus;

public interface WarriorStatus extends MovingAgentStatus {
	final static String ATTACKING = "ATTACKING";
	final static String DEFENDING = "DEFENDING";
	final static String DEAD = "DEAD";
}
