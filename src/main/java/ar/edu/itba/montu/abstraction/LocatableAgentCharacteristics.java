package ar.edu.itba.montu.abstraction;

public abstract class LocatableAgentCharacteristics {
  private Characteristic<Double> viewDistance;
  private Characteristic<Double> attackDistance;
  private Characteristic<Double> attack;
  private Characteristic<Double> healthPoint;

  public LocatableAgentCharacteristics(double viewDistance, double attackDistance, double healthPoint, double attack) {
    this.viewDistance = new Characteristic<>(viewDistance);
    this.attackDistance = new Characteristic<>(attackDistance);
    this.attack = new Characteristic<>(attack);
    this.healthPoint = new Characteristic<>(healthPoint);
  }

  public double getViewDistance() {
    return viewDistance.value();
  }

  public double getAttackDistance() {
    return attackDistance.value();
  }

  public double getAttack() {
    return attackDistance.value();
  }

  public double getHealthPoints() {
    return healthPoint.value();
  }

  public void setHealthPoints(double value) {
    healthPoint.setValue(value);
  }

  public double getMaxHealthPoints() {
    return healthPoint.maxValue();
  }

  public double getHealthPercentage() {
    return healthPoint.value()/healthPoint.maxValue();
  }
}
