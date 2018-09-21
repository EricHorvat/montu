package ar.edu.itba.montu.war.castle;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.montu.abstraction.IWarAgent;
import ar.edu.itba.montu.abstraction.WarFieldAgent;
import ar.edu.itba.montu.interfaces.IBuilding;
import ar.edu.itba.montu.interfaces.IWarrior;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.utils.Coordinate;

public class Castle extends WarFieldAgent implements IBuilding {
	
	final String name;
	final CastleCharacteristics characteristics;
	final Coordinate coordinate;
	
	final List<IWarrior> warriors = new ArrayList<>();
	private List<IWarAgent> visibleAgents = new ArrayList<>();
	
	/* package */ Castle(final String name, final CastleCharacteristics characteristics, final Coordinate coords, final int warriors, final int healers) {
		this.name = name;
		this.characteristics = characteristics;
		this.coordinate = coords;
		
//		this.warriors = IntStream.range(0, warriors).map(mapper)
	}
	
	private IWarrior buildWarriorWithCharateristics(final CastleCharacteristics characteristics) {
		return null;
	}
	
	private void reallocateWarriors() {
		
	}

  @Override
  public void act() {

  }

  @Override
	public Coordinate getCoordinate() {
		return coordinate;
	}


	@Override
  public CastleCharacteristics getCharacteristics() {
    return characteristics;
  }

	@Override
	public Kingdom getKingdom() {
		return null;
	}

	public List<IWarAgent> getVisibleAgents() {
		/* TODO environment should be Singleton?*/
		if (true){
			/* TODO CHECK TIME, OR STH LIKE THAT TO ONLY UPDATE ONE PER LOOP*/
      WarEnvironment environment = WarEnvironment.getInstance();
      visibleAgents = environment.getAgentsFromCoordinate(getCoordinate(),50);
		}
		return visibleAgents;
	}

	//    private Castle rival;
//
//    public Castle(double hp, Test t) {
//        super(hp, 0, new DamageSkill(300, 30, 0, 50), t);
//        coordinate = new Coordinate(Math.random()*200,Math.random()*200);
//    }
//
//    public Castle(Test t) {
//        this(1000, t);
//    }
//
//    @Override
//    public void move() {
//        if (getDelay() == 0){
//            if (false /*EnemyInRange*/){
//                //Attack
//            }else{
//                generatePetard();
//            }
//        }else{
//            setDelay(getDelay()-1);
//        }
//    }
//
//    public void setRival(Castle rival) {
//        this.rival = rival;
//    }
//
//    @Override
//    public String outfileFormat() {
//        return super.outfileFormat() + "\t" + "5";
//    }

}
