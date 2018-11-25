package ar.edu.itba.montu.abstraction;

public class LocatableAgentCharacteristics {
  private Characteristic<Double> viewDistance;
  private Characteristic<Double> healthPoints;

  /* package */ LocatableAgentCharacteristics(double viewDistance, double healthPoints) {
    this.viewDistance = Characteristic.withFixedValue(viewDistance);
    this.healthPoints = Characteristic.withChangingValue(0.0, healthPoints);
  }
  
  public static LocatableAgentCharacteristics withViewDistanceAndHealthPoints(double viewDistance, double healthPoints) {
  	return new LocatableAgentCharacteristics(viewDistance, healthPoints);
  }

  public double viewDistance() {
    return viewDistance.value();
  }

  public double healthPoints() {
    return healthPoints.value();
  }

  public LocatableAgentCharacteristics healthPoints(double value) {
    healthPoints.updateValue(value);
    return this;
  }

  public double maxHealthPoints() {
    return healthPoints.maxValue();
  }

  public double healthPercentage() {
    return 1.0 * healthPoints.value() / healthPoints.maxValue();
  }
}
