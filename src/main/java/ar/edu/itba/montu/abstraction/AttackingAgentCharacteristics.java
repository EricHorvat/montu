package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AttackingAgentCharacteristics extends LocatableAgentCharacteristics {
  private Characteristic<Double> attackDistance;
  private Characteristic<Double> attackHarm;
  
	private static final Logger logger = LogManager.getLogger(AttackingAgentCharacteristics.class);
 
	public AttackingAgentCharacteristics(LocatableAgentCharacteristics characteristic, double attackDistance, double attackHarm) {
  	super(characteristic.viewDistance(), characteristic.healthPoints());
    this.attackDistance = Characteristic.withFixedValue(attackDistance);
    this.attackHarm = Characteristic.withChangingValue(0.0, attackHarm);
  }

  public double attackDistance() {
    return attackDistance.value();
  }

  public double attackHarm() {
    return attackHarm.value();
  }

  public AttackingAgentCharacteristics attackHarm(double attackHarm) {
  	this.attackHarm.updateValue(attackHarm);
  	return this;
  }
}
