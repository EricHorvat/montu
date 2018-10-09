package ar.edu.itba.montu.war.objective;

import ar.edu.itba.montu.abstraction.Agent;
import ar.edu.itba.montu.abstraction.Attacker;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.abstraction.MovingAgent;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.utils.RandomUtil;

import java.util.Random;

public class AttackObjective implements Objective {
	
	final Attacker target;
	final int priority;
	
	private AttackObjective(final Attacker target, final int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public static AttackObjective headedToWithPriority(final Attacker target, final int priority) {
		return new AttackObjective(target, priority);
	}
	
	@Override
	public void enforce(final Attacker attacker) {
		
		//final int requiredAttackers = target.availableDefenders().size() + 1;

		//if ( attacker.attackers().size() < requiredAttackers) {
			
			int s = attacker.createAttackers(1).size();
		//}
		
		/*if (attacker.availableAttackers().size() >= requiredAttackers) {*/
			attacker.availableAttackers().forEach(a -> {
				((MovingAgent)a).assignTarget((LocatableAgent)target, RandomUtil.getRandom().nextInt(1000)); /* TODO WARNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN CHANGE 1000*/
			});
			// I don't what happens now
		//}*/
		
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
