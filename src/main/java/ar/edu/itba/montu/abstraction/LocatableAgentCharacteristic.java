package ar.edu.itba.montu.abstraction;

public abstract class LocatableAgentCharacteristic {
  private Characteristic<Double> viewDistance;
  private Characteristic<Double> attackDistance;
  private Characteristic<Double> attack;
  private Characteristic<Double> healthPoint;

  public LocatableAgentCharacteristic(double viewDistance, double attackDistance, double healthPoint, double attack) {
    this.viewDistance = Characteristic.withFixedValue(viewDistance);
    this.attackDistance = Characteristic.withFixedValue(attackDistance);
    this.attack = Characteristic.withFixedValue(attack); 
    this.healthPoint = Characteristic.withChangingValue(0.0, healthPoint, healthPoint);
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
    return healthPoint.value();
  }

  public void healthPoints(double value) {
    healthPoint.updateValue(value);
  }

  public double maxHealthPoints() {
    return healthPoint.maxValue();
  }

  public double healthPercentage() {
    return healthPoint.value()/healthPoint.maxValue();
  }
}
