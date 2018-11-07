package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.utils.RandomUtil;

import java.util.Objects;

public class DefendObjective implements Objective {
	
	final private LocatableAgent target;
	private double priority;
	private double basePriority;
	
	private DefendObjective(final LocatableAgent target, final double priority) {
		this.target = target;
		this.priority = priority;
		this.basePriority = priority;
	}
	
	public static DefendObjective fromWithPriority(final LocatableAgent target, final double priority) {
		return new DefendObjective(target, priority);
	}
	
	@Override
	public int compareTo(Objective o) {
		return Double.compare(priority, o.priority());
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DefendObjective that = (DefendObjective) o;
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
		return target.equals(agent);
	}
	
	@Override
	public void basePriority(double priority) {
		basePriority = priority;
	}
	@Override
	public <T extends LocatableAgent> T target() {
		return (T)target;
	}
	
	
	public void apply(final Spawner spawner) {
		
		spawner.availableDefenders()
			.forEach(defender -> {
				defender.assignToTarget(target, App.getConfiguration().getMaxPriority() /10.0);
				spawner.defendObjectives().stream().filter(defendObjective -> !this.equals(defendObjective)).forEach(defendObjective -> defender.assignToTarget(defendObjective.target(),defendObjective.priority()));
			});
	}
	
}
