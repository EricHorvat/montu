package ar.edu.itba.montu.abstraction;

import java.util.List;

public interface Attacker {
	List<? extends Attacker> attackers();
	List<? extends Attacker> availableAttackers();
	void createAttackers(final int attackers);
}
