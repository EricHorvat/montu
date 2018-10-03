package ar.edu.itba.montu.abstraction;

import java.util.List;

public interface Attacker {
	List<? extends Attacker> attackers();
	List<? extends Attacker> availableAttackers();
	List<? extends Attacker> createAttackers(final int attackers);
}
