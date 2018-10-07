package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.war.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Attacker {
	List<? extends Attacker> attackers();
	List<? extends Attacker> availableAttackers();
	Attacker createAnAttacker();
	
	default List<? extends Attacker> createAttackers(final int attackers){
		return IntStream
			.range(0,attackers)
			.mapToObj(i -> RandomUtil.getRandom().nextDouble() < 0.01 ? createAnAttacker() : null)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
}
