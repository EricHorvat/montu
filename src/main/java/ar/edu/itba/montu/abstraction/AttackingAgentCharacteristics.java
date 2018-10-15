package ar.edu.itba.montu.abstraction;

public class AttackingAgentCharacteristics extends LocatableAgentCharacteristics {
  private Characteristic<Double> attackDistance;
  private Characteristic<Integer> attackHarm;

  public AttackingAgentCharacteristics(LocatableAgentCharacteristics characteristic, double attackDistance, int attackHarm) {
  	super(characteristic.viewDistance()*4, characteristic.healthPoints());
    this.attackDistance = Characteristic.withFixedValue(attackDistance*10);
    this.attackHarm = Characteristic.withChangingValue(0, attackHarm);
  }

  public double attackDistance() {
    return attackDistance.value();
  }

  public int attackHarm() {
    return attackHarm.value();
  }

  public AttackingAgentCharacteristics attackHarm(int attackHarm) {
  	this.attackHarm.updateValue(attackHarm);
  	return this;
  }
}
