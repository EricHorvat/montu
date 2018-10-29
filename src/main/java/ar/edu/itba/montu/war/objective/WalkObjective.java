package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.interfaces.Objective;

import java.util.Objects;

public class WalkObjective implements Objective {
	
	private final LocatableAgent target;
	private double priority;
	private double basePriority;
	
	private WalkObjective(final LocatableAgent target, final int priority) {
		this.target = target;
		this.priority = priority;
		this.priority = basePriority;
	}
	
	public WalkObjective(final LocatableAgent target) {
		this.target = target;
		this.priority = 0;
		this.basePriority = 0;
	}
	
	@Override
	public void apply(Spawner spawner) {
		//MUST BE EMPTY TODO REVISE
	}
	
	@Override
	public double priority() {
		return 0;
	}
	
	@Override
	public void updatePriority(double coefficient) {
		//MUST BE EMPTY TODO REVISE
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
	public int compareTo(Objective o) {
		return Double.compare(priority, o.priority());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WalkObjective that = (WalkObjective) o;
		return Double.compare(that.priority, priority) == 0 &&
			Double.compare(that.basePriority, basePriority) == 0 &&
			Objects.equals(target, that.target);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(target, priority, basePriority);
	}
	
	
	@Override
	public void basePriority(double priority) {
		basePriority = priority;
	}
}
