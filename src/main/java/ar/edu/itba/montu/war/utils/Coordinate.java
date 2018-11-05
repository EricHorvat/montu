package ar.edu.itba.montu.war.utils;

import java.util.Objects;

public class Coordinate {
	
	public final double X;
	public final double Y;
	public static final double TEN_PERCENT = 0.1;
	
	private Coordinate(final double x, final double y) {
		X = x;
		Y = y;
	}
	
	public static Coordinate at(final double x, final double y) {
		return new Coordinate(x, y);
	}

	public static double distanceBetween(final Coordinate a, final Coordinate b) {
		return Math.sqrt(Math.pow(a.X - b.X, 2) + Math.pow(a.Y - b.Y, 2));
	}
	
	public double distanceTo(final Coordinate c) {
		return distanceBetween(this, c);
	}
	
	public Coordinate applyingDeltaInDirectionTo(final double delta, final Coordinate to) {
		final Coordinate dir = Coordinate.direction(this, to);
		final double angle = Math.atan2(dir.Y, dir.X);
		return Coordinate.sum(this, Coordinate.at(delta * Math.cos(angle), delta * Math.sin(angle)));
	}
	
	public Coordinate applyingNoisyDeltaInDirectionTo(final double delta, final Coordinate to) {
		final Coordinate dir = Coordinate.direction(this, to);
		final double theta = Math.atan2(dir.Y, dir.X);
		final double angle = RandomUtil.getNormalDistribution(theta, theta * TEN_PERCENT);
		return Coordinate.sum(this, Coordinate.at(delta * Math.cos(angle), delta * Math.sin(angle)));
	}
	
	public static Coordinate direction(final Coordinate from, final Coordinate to) {
		final double h = Math.sqrt(Math.pow(from.X - to.X, 2) + Math.pow(from.Y - to.Y, 2));
		return new Coordinate((to.X - from.X) / h, (to.Y - from.Y) / h);
	}

//	public static boolean sees(Coordinate looker, Coordinate looked, double lookerViewDistance) {
//		double distance = distanceBetween(looker,looked);
//		return distance < lookerViewDistance && RandomUtil.getRandom().nextDouble() > Math.pow(distance,2);
//	}

	public static Coordinate sum(final Coordinate a, final Coordinate b) {
		return Coordinate.at(a.X + b.X, a.Y + b.Y);
	}

	public boolean in(Coordinate a, Coordinate b) {
		if (a.X >= b.X && a.Y >= b.Y) {
			Coordinate q = a;
			a = b;
			b = q;
		} else if (a.X <= b.X && a.Y >= b.Y) {
			Coordinate q = new Coordinate(a.X, b.Y);
			Coordinate r = new Coordinate(b.X, a.Y);
			a = q;
			b = r;
		} else if (a.X >= b.X && a.Y <= b.Y) {
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
		return Double.compare(that.X, X) == 0 && Double.compare(that.Y, Y) == 0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(X, Y);
	}
}