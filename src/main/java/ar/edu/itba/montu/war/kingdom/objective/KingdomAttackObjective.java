package ar.edu.itba.montu.war.kingdom.objective;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.objective.AttackObjective;
import ar.edu.itba.montu.war.utils.Coordinate;

public class KingdomAttackObjective implements KingdomObjective {
	
	final private Kingdom target;
	private double priority;
	
	private KingdomAttackObjective(final Kingdom target, final double priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static KingdomAttackObjective headedToWithPriority(final Kingdom target, final double priority) {
		return new KingdomAttackObjective(target, priority);
	}

	@Override
	public double priority() {
		return priority;
	}
	
	@Override
	public void alterPriority(final double priority) {
		this.priority = priority;
	} 

	@Override
	public int compareTo(KingdomObjective o) {
		return Double.compare(priority, o.priority());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KingdomAttackObjective that = (KingdomAttackObjective) o;
		return Double.compare(that.priority, priority) == 0 &&
			Objects.equals(target, that.target);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(target, priority);
	}
	
	@Override
	public boolean involves(final LocatableAgent agent) {
		return target.castles().contains(agent);
	}

	@Override
	public List<Objective> translate(Coordinate sourceLocation) {
		return target.castles().stream()
				.map(castle -> AttackObjective.headedToWithPriority(castle,
					priority
						* castle.characteristics().healthPoints()
						/castle.characteristics().maxHealthPoints()
						/ Double.max(Coordinate.distanceBetween(castle.location(),sourceLocation), App.getConfiguration().getMinPriorityDistance())
				))
				.collect(Collectors.toList());
	}

	@Override
	public Kingdom target() {
		return target;
	}

	@Override
	public String toString() {
		return "KAObjective [target=" + target + ", priority=" + priority + "]";
	}
	
	
	
	
}
