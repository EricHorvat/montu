package ar.edu.itba.montu.abstraction;

public abstract class WarFieldAgent extends WarAgent implements IWarFieldAgent {

  @Override
  public void loop() {
    if (getCharacteristics().getActualDelay() <= 0){
      act();
    }
  }

  public abstract <T extends WarFieldAgentCharacteristics> T getCharacteristics();

  public abstract void act();
}
