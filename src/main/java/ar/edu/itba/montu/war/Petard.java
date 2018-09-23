package ar.edu.itba.montu.war;

import java.util.Optional;

import ar.edu.itba.montu.interfaces.IPerson;
import ar.edu.itba.montu.interfaces.IWarrior;
import ar.edu.itba.montu.war.people.WarriorStatus;

public class Petard {


	public WarriorStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<IPerson> getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void assignTarget(IPerson person) {
		// TODO Auto-generated method stub
		
	}

//    // based on http://ageofempires.wikia.com/wiki/Petard_(Age_of_Empires_II)
//    private Castle destination;
//
//
//    public Petard(double hp, double speedDelay, Castle destination, Test t) {
//        super(hp, speedDelay, new DamageSkill(2,100,100,0), t);
//        this.destination = destination;
//    }
//
//    public Petard(Castle destination,Test t) {
//        this(100,10, destination, t);
//    }
//
//    @Override
//    public void move() {
//        if (getDelay() == 0) {
//            if (Coordinate.getDistance(coordinate, destination.getCoordinate()) < getDamageSkill().getDistance()) {
//                destination.damaged(getDamageSkill().getDamage());
//                damagedPercentage(getDamageSkill().getRecoilDamage());
//                setDelay(getDamageSkill().getDelay());
//            } else {
//                Coordinate desiredCoordinate;
//                double noise = 0.3;
//                desiredCoordinate = Coordinate.sum(getCoordinate(), Coordinate.getNoisyDirection(getCoordinate(), destination.getCoordinate(), noise));
//                setCoordinate(desiredCoordinate);
//                setDelay(getSpeedDelay());
//            }
//        }else{
//            setDelay(getDelay()-1);
//        }
//    }
//
//    @Override
//    public String outfileFormat() {
//        return super.outfileFormat() + "\t" + "5";
//    }
}
