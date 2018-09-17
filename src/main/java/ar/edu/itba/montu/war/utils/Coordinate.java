package ar.edu.itba.montu.war.utils;

import java.util.Objects;

public class Coordinate {

    public final double X;
    public final double Y;

    public Coordinate(double x, double y) {
        X = x;
        Y = y;
    }

    public static double getDistance(Coordinate a, Coordinate b){
        return Math.sqrt(Math.pow(a.X - b.X,2) + Math.pow(a.Y - b.Y,2));
    }

    public static Coordinate getDirection(Coordinate from, Coordinate to){
        double h = Math.sqrt(Math.pow(from.X - to.X,2) + Math.pow(from.Y - to.Y,2));
        return new Coordinate((to.X - from.X)/h, (to.Y - from.Y)/h);
    }

    public static Coordinate getNoisyDirection(Coordinate from, Coordinate to, double noise){
        return sum(getDirection(from, to), new Coordinate(Math.random()*noise-noise/2.0,Math.random()*noise-noise/2.0));
    }

    public static boolean sees(Coordinate looker, Coordinate looked, double lookerViewDistance){
        double distance = getDistance(looker,looked);
        return distance < lookerViewDistance && RandomUtil.getRandom().nextDouble() > Math.pow(distance,2);
    }

    public static Coordinate sum(Coordinate a, Coordinate b){
        return new Coordinate(a.X + b.X, a.Y + b.Y);
    }

    public boolean in(Coordinate a, Coordinate b){
        if (a.X >= b.X && a.Y >= b.Y){
            Coordinate q = a;
            a = b;
            b = q;
        }else if (a.X <= b.X && a.Y >= b.Y){
            Coordinate q = new Coordinate(a.X, b.Y);
            Coordinate r = new Coordinate(b.X, a.Y);
            a = q;
            b = r;
        }else if (a.X >= b.X && a.Y <= b.Y){
            Coordinate r = new Coordinate(b.X, a.Y);
            Coordinate q = new Coordinate(a.X, b.Y);
            a = r;
            b = q;
        }
        return this.X >= a.X && this.Y >= a.Y && this.X <= b.X && this.Y <= b.Y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.X, X) == 0 &&
                Double.compare(that.Y, Y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y);
    }
}