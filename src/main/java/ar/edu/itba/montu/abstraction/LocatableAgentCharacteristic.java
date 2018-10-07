package ar.edu.itba.montu.abstraction;

public abstract class LocatableAgentCharacteristic {
  private Characteristic<Double> viewDistance;
  private Characteristic<Double> attackDistance;
  private Characteristic<Double> attack;
  private Characteristic<Double> healthPoints;

  public LocatableAgentCharacteristic(double viewDistance, double attackDistance, double healthPoints, double attack) {
    this.viewDistance = Characteristic.withFixedValue(viewDistance);
    this.attackDistance = Characteristic.withFixedValue(attackDistance);
    this.attack = Characteristic.withFixedValue(attack); 
    this.healthPoints = Characteristic.withChangingValue(0.0, healthPoints);
  }

  public double viewDistance() {
    return viewDistance.value();
  }

  public double attackDistance() {
    return attackDistance.value();
  }

  public double attack() {
    return attackDistance.value();
  }

  public double healthPoints() {
    return healthPoints.value();
  }

  public void healthPoints(double value) {
    healthPoints.updateValue(value);
  }

  public double maxHealthPoints() {
    return healthPoints.maxValue();
  }

  public double healthPercentage() {
    return healthPoints.value()/healthPoints.maxValue();
  }
}
