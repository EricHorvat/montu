package ar.edu.itba.montu.abstraction;

public abstract class MovingAgentCharacteristics extends LocatableAgentCharacteristics {

  private Characteristic<Double> timespan;
  private Characteristic<Double> actualDelay;

  public MovingAgentCharacteristics(double viewDistance, double attackDistance, double healthPoint, double attack) {
    super(viewDistance, attackDistance, healthPoint, attack);
  }

  // TODO CAN HAVE A MAP <"CharacteristicName", Characteristic>, but what to do if key not present?
  public double getTimespan() {
    return timespan.value();
  }

  public double getActualDelay() {
    return actualDelay.value();
  }

}
