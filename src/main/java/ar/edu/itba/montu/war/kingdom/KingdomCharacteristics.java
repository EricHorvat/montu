package ar.edu.itba.montu.war.kingdom;

import ar.edu.itba.montu.abstraction.Characteristic;

public class KingdomCharacteristics {

	final Characteristic<Double> offenseCapacity;
	final Characteristic<Double> warriorSpeed;

	public KingdomCharacteristics(final double attack, final double speed) {
		this.offenseCapacity = Characteristic.withFixedValue(attack);
		this.warriorSpeed = Characteristic.withFixedValue(speed);
	}
	
	public static KingdomCharacteristics withOffenseCapacityAndWarriorSpeed(final double offenseCapacity, final double warriorSpeed) {
		
		return new KingdomCharacteristics(offenseCapacity, warriorSpeed);
	}
	
	public double offenseCapacity() {
		return offenseCapacity.value();
	}
	
	public double defenseCapacity() {
		return 100 - offenseCapacity.value();
	}
	
	public double warriorSpeed() {
		return warriorSpeed.value();
	}
}
