package ar.edu.itba.montu.war.castle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ar.edu.itba.montu.abstraction.Attacker;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.people.WarriorStatus;
import ar.edu.itba.montu.war.utils.Coordinate;

public class Castle extends LocatableAgent {

	final private String name;
	final private CastleCharacteristics characteristics;
	/**
	 * The height of the castle is a measure of how
	 * far away it can see
	 */
	final private double height;
	
	private Objective currentObjective;
	
	final List<Warrior> warriors;
//	private List<WarFieldAgent> visibleAgents = new ArrayList<>();
	
	/* package */ Castle(final Kingdom kingdom, final String name, final CastleCharacteristics characteristics, final Coordinate coordinate, final int warriors, final int healers, final double height) {
		super();
		this.name = name;
		this.characteristics = characteristics;
		this.location = coordinate;
		this.height = height;
		this.kingdom = kingdom;
		
		this.warriors = IntStream
				.range(0, warriors)
				.mapToObj(i -> Warrior.createWithCharacteristicsInKingdomAtLocation(coordinate, characteristics, kingdom))
				.collect(Collectors.toList());
	}

  public void act() {
	  // Decide to attack or spawn
    // if attack
//    List<WarFieldAgent> enemyAgentsOnRange;
    //Collections.shuffle(enemyAgentsOnRange,RandomUtil.getRandom());
    /*for(int i = 0; i < characteristics.getConcurrentAttackCount; i++){
      enemyAgentsOnRange.get(i % enemyAgentsOnRange.size()).attacked()
    }*/
    // if spawn
//    buildWarriorWithCharacteristics(this.characteristics);

  }
  
  private void enforceCurrentObjective() {
  	currentObjective.enforce(this);
  }

  public void tick(final long timeEllapsed) {
  	/// TODO: template what a castle does on each tick
  	
  	final Objective kingdomObjective = kingdom.currentObjective().get();
  	
  	if (kingdomObjective == null) {
  		return;
  	}
  	
  	if (kingdomObjective.equals(currentObjective)) {
  		
  		this.enforceCurrentObjective();
  		return;
  	}

  	currentObjective = kingdomObjective;
  	
//    super.tick(timeEllapsed);
    //if Kingdom has decided
//    List<WarFieldAgent> ownAgents =
//        getVisibleAgents().stream().filter(
//            warFieldAgent ->  warFieldAgent.getKingdom().equals(getKingdom())
//                && warFieldAgent instanceof IPerson
//                && ((IPerson)warFieldAgent).isIdle())
//            .collect(Collectors.toList());
    // for each agent set work
  }

	public Coordinate location() {
		return location;
	}


  public CastleCharacteristics characteristics() {
    return characteristics;
  }

	public List<LocatableAgent> visibleAgents() {
		final WarEnvironment environment = WarEnvironment.getInstance();
		
		///TODO: make the proper calculations to get the value of radius
		return environment.agentsWithinRadiusOfCoordinate(location, 1000);
	}
	
	public List<LocatableAgent> agents() {
		return warriors.stream().map(w -> (LocatableAgent)w).collect(Collectors.toList());
	}

	@Override
	public List<Warrior> attackers() {
		return warriors;
	}
	
	private Warrior buildWarrior() {
		return Warrior.createWithCharacteristicsInKingdomAtLocation(location, characteristics, kingdom);
	}

	@Override
	public void createAttackers(final int attackers) {
		final List<Warrior> warriors = IntStream.range(0, attackers).mapToObj(i -> buildWarrior()).collect(Collectors.toList());
		this.warriors.addAll(warriors);
	}

	@Override
	public List<Warrior> availableAttackers() {
		return warriors
				.stream()
				.filter(w -> w.status() != WarriorStatus.SPAWNING && w.status() != WarriorStatus.DEAD)
				.collect(Collectors.toList());
	}

	@Override
	public void defend(float damageSkill) {
		double hp = characteristics.getHealthPoints() - damageSkill;
		if (hp < 0) {
			//TODO STATUS = DEAD
			hp = 0;
			WarEnvironment.getInstance().onCastleDeath(this);
			return;
		}
		characteristics.setHealthPoints(hp);
	}

	@Override
	public int getHealthPointPercentage() {
		return (int)(100*characteristics.getHealthPercentage());
	}
}
