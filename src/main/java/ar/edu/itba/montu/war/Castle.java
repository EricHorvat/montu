package ar.edu.itba.montu.war;

import ar.edu.itba.montu.Test;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.DamageSkill;
import ar.edu.itba.montu.war.utils.WarObject;

import java.util.Objects;
import java.util.Random;

public class Castle extends WarObject implements ICastle {

    private Castle rival;

    public Castle(double hp, Test t) {
        super(hp, 0, new DamageSkill(300, 30, 0, 50), t);
        do{
            coordinate = new Coordinate(Math.random()*200,Math.random()*200);
        }while (coordinate.in(new Coordinate(75,75),new Coordinate(125,125)));
    }

    public Castle(Test t) {
        this(1000, t);
    }

    @Override
    public void move() {
        if (getDelay() == 0){
            if (false /*EnemyInRange*/){
                //Attack
            }else{
                generatePetard();
            }
        }else{
            setDelay(getDelay()-1);
        }
    }

    public void generatePetard(){
        Petard p = new Petard(rival,test);
        p.setCoordinate(new Coordinate(this.coordinate.X + Math.random() - 0.5,this.coordinate.Y + Math.random() - 0.5));
        test.newWarObject(p,kingdom);
        setDelay(1000);
    }

    public void setRival(Castle rival) {
        this.rival = rival;
    }

    @Override
    public String outfileFormat() {
        return super.outfileFormat() + "\t" + "5";
    }

}
