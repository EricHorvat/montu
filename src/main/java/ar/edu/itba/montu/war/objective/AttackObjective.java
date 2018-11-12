package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.utils.RandomUtil;

import java.util.Objects;

public class AttackObjective implements Objective {
	
	private final LocatableAgent target;
	private double priority;
	private double basePriority;
	
	private AttackObjective(final LocatableAgent target, final double priority) {
		this.target = target;
		this.priority = priority;
		this.basePriority = priority;
	}
	
	public static AttackObjective headedToWithPriority(final LocatableAgent target, final double priority) {
		return new AttackObjective(target, priority);
	}
	
	public void apply(final Spawner spawner) {
		
		spawner.availableAttackers()
			.forEach(attacker -> {
				attacker.assignToTarget(target, App.getConfiguration().getMaxPriority());
				spawner.attackObjectives().stream().filter(attackObjective -> !attackObjective.equals(this)).forEach(attackObjective -> attacker.assignToTarget(attackObjective.target(),attackObjective.priority()));
			});
		
	}

	@Override
	public double priority() {
		return priority;
	}
	
	@Override
	public void updatePriority(double coefficient) {
		this.priority = basePriority * coefficient;
	}

	@Override
	public int compareTo(Objective o) {
		return Double.compare(priority, o.priority());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AttackObjective that = (AttackObjective) o;
		return Double.compare(that.priority, priority) == 0 &&
			Double.compare(that.basePriority, basePriority) == 0 &&
			Objects.equals(target, that.target);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(target, priority, basePriority);
	}
	
	@Override
	public boolean involves(final LocatableAgent agent) {
		return (target).equals(agent);
	}
	
	@Override
	public <T extends LocatableAgent> T target() {
		return (T)target;
	}
	
	@Override
	public void basePriority(double priority) {
		basePriority = priority;
	}
	
	@Override
	public String toString() {
        return "AObjective [target=" + target + ", priority=" + priority + "]";
    }
}
