package ar.edu.itba.montu.abstraction;

public abstract class WarFieldAgentCharacteristics extends WarAgentCharacteristics {

  private Characteristic<Double> timespan;
  private Characteristic<Double> actualDelay;
  private Characteristic<Double> viewDistance;
  private Characteristic<Double> attackDistance;

  // TODO CAN HAVE A MAP <"CharacteristicName", Characteristic>, but what to do if key not present?
  public double getTimespan() {
    return timespan.getValue();
  }

  public double getActualDelay() {
    return actualDelay.getValue();
  }

  double getViewDistance() {
    return viewDistance.getValue();
  }

  double getAttackDistance() {
    return attackDistance.getValue();
  }
}
