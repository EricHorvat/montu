package ar.edu.itba.montu.war.kingdom.objective;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.objective.DefendObjective;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KingdomDefendObjective implements KingdomObjective {
	
	final private Kingdom target;
	final private double priority;
	
	private KingdomDefendObjective(final Kingdom target, final double priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static KingdomDefendObjective fromWithPriority(final Kingdom target, final double priority) {
		return new KingdomDefendObjective(target, priority);
	}

	@Override
	public int compareTo(KingdomObjective o) {
		return Double.compare(priority, o.priority());
	}

	@Override
	public double priority() {
		return priority;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KingdomDefendObjective that = (KingdomDefendObjective) o;
		return Double.compare(that.priority, priority) == 0 &&
			Objects.equals(target, that.target);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(target, priority);
	}
	
	@Override
	public Kingdom target() {
		return target; /*TODO*/
	}
	
	@Override
	public boolean involves(final LocatableAgent agent) {
		return target.castles().contains(agent);
	}
	
	@Override
	public void alterPriority(double priority) {
	
	}
	
	@Override
	public List<Objective> translate() {
		return target.castles().stream()
			.map(castle -> DefendObjective.fromWithPriority(castle, priority))
			.collect(Collectors.toList());
	}
}
