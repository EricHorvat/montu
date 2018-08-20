package ar.edu.itba.montu.war.utils;

import ar.edu.itba.montu.Test;
import ar.edu.itba.montu.war.Kingdom;

import java.util.Objects;

public abstract class WarObject {

    private double hp;
    private double speedDelay;
    private double delay = 0;
    private DamageSkill damageSkill;
    protected Kingdom kingdom;
    protected Test test;
    protected Coordinate coordinate;

    public WarObject(double hp, double speedDelay, DamageSkill damageSkill, Kingdom kingdom, Test test) {
        this(hp,speedDelay,damageSkill,test);
        this.kingdom = kingdom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarObject warObject = (WarObject) o;
        return Double.compare(warObject.hp, hp) == 0 &&
                Double.compare(warObject.speedDelay, speedDelay) == 0 &&
                Double.compare(warObject.delay, delay) == 0 &&
                Objects.equals(damageSkill, warObject.damageSkill) &&
                Objects.equals(kingdom, warObject.kingdom) &&
                Objects.equals(test, warObject.test) &&
                Objects.equals(coordinate, warObject.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hp, speedDelay, delay, damageSkill, test, coordinate);
    }

    public WarObject(double hp, double speedDelay, DamageSkill damageSkill, Test test) {
        this.hp = hp;
        this.speedDelay = speedDelay;
        this.damageSkill = damageSkill;
        this.test = test;
    }

    public double getHp() {
        return hp;
    }

    public double getSpeedDelay() {
        return speedDelay;
    }

    public DamageSkill getDamageSkill() {
        return damageSkill;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    public abstract void move();

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void damaged(double value){
        hp -= value;
    }

    public void damagedPercentage(double value){
        damaged(value*hp/100.0);
    }

    public String outfileFormat(){
        return coordinate.X + "\t" + coordinate.Y + "\t" + kingdom.hashCode();
    }
}
