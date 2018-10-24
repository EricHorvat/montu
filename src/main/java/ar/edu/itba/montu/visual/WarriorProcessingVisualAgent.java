package ar.edu.itba.montu.visual;

import java.util.UUID;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.people.Warrior;

/*package*/ class WarriorProcessingVisualAgent extends ProcessingVisualAgent{
  private static final float R = 0.35f;

  /*package*/ WarriorProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    final float Rx = linearMapX(R);
    final float Ry = linearMapY(R);
  
    if (((Warrior)locatableAgent).isDefender()){
      applet.ellipse(x, y, 2 * Rx, 2 * Ry);
    }else if (((Warrior)locatableAgent).isAttacker()) {
      applet.triangle(x-Rx, y-Ry, x + Rx, y, x-Rx, y + Ry);
    }else
      applet.ellipse(linearMapX(x), linearMapY(y), linearMapX(2 * R), linearMapY(R));
  
    applet.noFill();
    if (ProcessingApplet.drawDistance) {
      float viewDistance = (float) ((Warrior) locatableAgent).characteristics().viewDistance();
      applet.stroke(applet.color(120, 100, 100, 50));
      applet.ellipse(x, y, linearMapX(2 * viewDistance), linearMapY(2 * viewDistance));
      float attackDistance = (float) ((Warrior) locatableAgent).characteristics().attackDistance();
      applet.stroke(applet.color(0, 100, 100, 50));
      applet.ellipse(x, y, linearMapX(2 * attackDistance), linearMapY(2 * attackDistance));
      applet.stroke(applet.color(0, 0, 0));
    }
  }
}
