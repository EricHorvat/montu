package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.interfaces.ICastle;
import ar.edu.itba.montu.war.utils.Coordinate;

public class Castle implements ICastle {
	
	final String name;
	final CastleCharacteristics characteristics;
	final Coordinate coordinate;
	
	/* package */ Castle(final String name, final CastleCharacteristics characteristics, final Coordinate coords) {
		this.name = name;
		this.characteristics = characteristics;
		this.coordinate = coords;
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
