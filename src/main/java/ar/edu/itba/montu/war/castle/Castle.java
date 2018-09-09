package ar.edu.itba.montu.war.castle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import ar.edu.itba.montu.abstraction.WarAgent;
import ar.edu.itba.montu.interfaces.IWarrior;
import ar.edu.itba.montu.war.utils.Coordinate;

public class Castle implements WarAgent {
	
	final String name;
	final CastleCharacteristics characteristics;
	final Coordinate coordinate;
	
	final List<IWarrior> warriors = new ArrayList<>();
	
	/* package */ Castle(final String name, final CastleCharacteristics characteristics, final Coordinate coords, final int warriors, final int healers) {
		this.name = name;
		this.characteristics = characteristics;
		this.coordinate = coords;
		
//		this.warriors = IntStream.range(0, warriors).map(mapper)
	}
	
	private IWarrior buildWarriorWithCharateristics(final CastleCharacteristics characteristics) {
		return null;
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
//    public void generatePetard(){
//        Petard p = new Petard(rival,test);
//        p.setCoordinate(new Coordinate(this.coordinate.X + Math.random() - 0.5,this.coordinate.Y + Math.random() - 0.5));
//        test.newWarObject(p,kingdom);
//        setDelay(1000);
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
