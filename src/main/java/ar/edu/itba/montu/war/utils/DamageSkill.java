package ar.edu.itba.montu.war.utils;

import java.util.Objects;

public final class DamageSkill {
    private float distance;
    private float damage;
    private float recoilDamage;
    private float delay;

    public DamageSkill(float distance, float damage, float recoilDamage, float delay) {
        this.distance = distance;
        this.damage = damage;
        this.recoilDamage = recoilDamage;
        this.delay = delay;
    }

    public float getDistance() {
        return distance;
    }

    public float getDamage() {
        return damage;
    }

    public float getRecoilDamage() {
        return recoilDamage;
    }

    public float getDelay() {
        return delay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DamageSkill that = (DamageSkill) o;
        return Float.compare(that.distance, distance) == 0 &&
                Float.compare(that.damage, damage) == 0 &&
                Float.compare(that.recoilDamage, recoilDamage) == 0 &&
                Float.compare(that.delay, delay) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, damage, recoilDamage, delay);
    }
}
