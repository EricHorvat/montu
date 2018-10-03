package ar.edu.itba.montu.war.castle;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ar.edu.itba.montu.abstraction.Attacker;
import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.interfaces.KingdomObjective;
import ar.edu.itba.montu.interfaces.Objective;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.people.Warrior;
import ar.edu.itba.montu.war.people.WarriorStatus;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class Castle extends LocatableAgent {

	final private String name;
	final private CastleCharacteristics characteristics;
	/**
	 * The height of the castle is a measure of how
	 * far away it can see
	 */
	final private double height;
	
	private KingdomObjective currentObjective;
	
	private int spawnCapacity;
	
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
  	currentObjective.translate().forEach(castleObjective -> {
  		castleObjective.enforce(this);
  	});
  }

  public void tick(final long timeEllapsed) {
  	/// TODO: template what a castle does on each tick
  	
  	final Optional<KingdomObjective> kingdomObjective = kingdom.currentObjective();
  	
  	spawnCapacity = characteristics.spawnCapacity();
  	
  	if (!kingdomObjective.isPresent()) {
  		return;
  	}
  	
  	final double d = RandomUtil.getRandom().nextDouble() * 100;
  	
  	if (d < characteristics.defenseCapacity()) {
  		// spawn defense warrior
  		createAttackers(1);
  		Optional.ofNullable(availableAttackers().get(0)).ifPresent(w -> {
  			if (!visibleAgents().isEmpty()) {
  				w.assignTarget(visibleAgents().get(0));
  			}
  		});
  		return;
  	}
  	
  	if (kingdomObjective.get().equals(currentObjective)) {
  		this.enforceCurrentObjective();
  		return;
  	}

  	currentObjective = kingdomObjective.get();
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
		return environment.agentsWithinRadiusOfCoordinate(location, 1000).stream().filter(agent -> {
			return !agent.kingdom().equals(kingdom);
		}).collect(Collectors.toList());
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
	public List<Warrior> createAttackers(final int attackers) {
		final int attackerCount = Math.min(attackers, spawnCapacity); 
		final List<Warrior> warriors = IntStream
				.range(0, attackerCount)
				.mapToObj(i -> buildWarrior())
				.collect(Collectors.toList());
		spawnCapacity -= attackerCount;
		this.warriors.addAll(warriors);
		return warriors;
	}

	@Override
	public List<Warrior> availableAttackers() {
		return warriors.stream().filter(Warrior::isAvailable).collect(Collectors.toList());
	}

	@Override
	public void defend(double damageSkill) {
		double hp = characteristics.healthPoints() - damageSkill;
		if (hp < 0) {
			//TODO STATUS = DEAD
			hp = 0;
			WarEnvironment.getInstance().onCastleDeath(this);
		}
		characteristics.healthPoints(hp);
	}

	@Override
	public int getHealthPointPercentage() {
		return (int)(100 * characteristics.healthPercentage());
	}

	@Override
	public boolean isAlive() {
		//TODO CHANGE
		return characteristics.healthPoints() > 0;
	}
}
