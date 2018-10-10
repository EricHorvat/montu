package ar.edu.itba.montu.war.kingdom;

import ar.edu.itba.montu.abstraction.Characteristic;

public class KingdomCharacteristics {

	final Characteristic<Double> offenseCapacity;

	public KingdomCharacteristics(double attack) {
		this.offenseCapacity = Characteristic.withFixedValue(attack);
	}
	
	public static KingdomCharacteristics withOffenseCapacity(final double offenseCapacity) {
		
		return new KingdomCharacteristics(offenseCapacity);
	}
	
	public double offenseCapacity() {
		return offenseCapacity.value();
	}
	
	public double defenseCapacity() {
		return 100 - offenseCapacity.value();
	}
}
