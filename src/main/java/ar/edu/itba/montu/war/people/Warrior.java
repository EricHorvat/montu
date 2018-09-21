package ar.edu.itba.montu.war.people;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.edu.itba.montu.abstraction.IWarAgent;
import ar.edu.itba.montu.abstraction.WarFieldAgent;
import ar.edu.itba.montu.interfaces.IPerson;
import ar.edu.itba.montu.interfaces.IWarrior;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.environment.WarEnvironment;

public abstract class Warrior extends WarFieldAgent implements IWarrior {
	
	protected WarriorStatus status;
	protected Optional<IPerson> target;
	protected List<Kingdom> friendKingdoms;

	@Override
	public WarriorStatus getStatus() {
		return status;
	}
	public Optional<IPerson> getTarget() {
		return target;
	}

	@Override
	public void act() {
	  WarEnvironment environment = WarEnvironment.getInstance();
		List<IWarAgent> others = environment.getAgentsFromCoordinate(getCoordinate(),50);/*TODO SET VIEW AND ATTACK DISTANCE*/
		others = others.stream().filter(warAgent -> !(friendKingdoms.contains(warAgent.getKingdom()))).collect(Collectors.toList());

		if(others.size() != 0){
			//TODO FILTER IF ANY IN ATTACK RANGE AND ATTACK ELSE MOVE
		}else {

			switch (status) {
				case DEAD:
				case UNASSIGNED:
					return;
				case ASSIGNED:
					/*TODO MOVE TO TARGET*/
					break;
			}
		}
	}
}
