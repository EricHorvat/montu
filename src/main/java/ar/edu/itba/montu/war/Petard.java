package ar.edu.itba.montu.war;

import ar.edu.itba.montu.Test;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.DamageSkill;
import ar.edu.itba.montu.war.utils.WarObject;

import javax.print.attribute.standard.Destination;

public class Petard extends WarObject implements IWarrior{

    // based on http://ageofempires.wikia.com/wiki/Petard_(Age_of_Empires_II)
    private Castle destination;


    public Petard(double hp, double speedDelay, Castle destination, Test t) {
        super(hp, speedDelay, new DamageSkill(2,100,100,0), t);
        this.destination = destination;
    }

    public Petard(Castle destination,Test t) {
        this(100,10, destination, t);
    }

    @Override
    public void move() {
        if (getDelay() == 0) {
            if (Coordinate.getDistance(coordinate, destination.getCoordinate()) < getDamageSkill().getDistance()) {
                destination.damaged(getDamageSkill().getDamage());
                damagedPercentage(getDamageSkill().getRecoilDamage());
                setDelay(getDamageSkill().getDelay());
            } else {
                Coordinate desiredCoordinate;
                double noise = 0.1;
                do{
                    desiredCoordinate = Coordinate.sum(getCoordinate(), Coordinate.getNoisyDirection(getCoordinate(), destination.getCoordinate(), noise));
                    noise += 0.1;
                }while (desiredCoordinate.in(new Coordinate(75,75),new Coordinate(125,125)));
                setCoordinate(desiredCoordinate);
                setDelay(getSpeedDelay());
            }
        }else{
            setDelay(getDelay()-1);
        }
    }

    @Override
    public String outfileFormat() {
        return super.outfileFormat() + "\t" + "5";
    }
}
