package ar.edu.itba.montu.abstraction;

public class LocatableAgentCharacteristics {
  private Characteristic<Double> viewDistance;
  private Characteristic<Integer> healthPoints;

  /* package */ LocatableAgentCharacteristics(double viewDistance, int healthPoints) {
    this.viewDistance = Characteristic.withFixedValue(viewDistance);
    this.healthPoints = Characteristic.withChangingValue(0, healthPoints);
  }
  
  public static LocatableAgentCharacteristics withViewDistanceAndHealthPoints(double viewDistance, int healthPoints) {
  	return new LocatableAgentCharacteristics(viewDistance, healthPoints);
  }

  public double viewDistance() {
    return viewDistance.value();
  }

  public int healthPoints() {
    return healthPoints.value();
  }

  public LocatableAgentCharacteristics healthPoints(int value) {
    healthPoints.updateValue(value);
    return this;
  }

  public double maxHealthPoints() {
    return healthPoints.maxValue();
  }

  public double healthPercentage() {
    return healthPoints.value() / healthPoints.maxValue();
  }
}
