package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.Spawner;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class AttackObjective implements Objective {
	
	final LocatableAgent target;
	final int priority;
	
	private AttackObjective(final LocatableAgent target, final int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static AttackObjective headedToWithPriority(final LocatableAgent target, final int priority) {
		return new AttackObjective(target, priority);
	}
	
	public void apply(final Spawner spawner) {
		
		spawner.availableAttackers()
			.forEach(a -> {
				a.assignTarget(target, RandomUtil.getRandom().nextInt(1000)); /* TODO WARNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN CHANGE 1000*/
			});
		
	}

	@Override
	public int priority() {
		return priority;
	}

	@Override
	public int compareTo(Objective o) {
		return priority - o.priority();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + priority;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttackObjective other = (AttackObjective) obj;
		if (priority != other.priority)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public boolean involves(final LocatableAgent agent) {
		return ((LocatableAgent)target).equals(agent);
	}
	
	@Override
	public <T extends Agent> T target() {
		return (T)target;
	}
	
	@Override
	public String toString() {
        return "AObjective [target=" + target + ", priority=" + priority + "]";
    }
}
