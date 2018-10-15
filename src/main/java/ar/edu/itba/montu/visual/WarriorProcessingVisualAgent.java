package ar.edu.itba.montu.visual;

import java.util.UUID;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.people.Warrior;

/*package*/ class WarriorProcessingVisualAgent extends ProcessingVisualAgent{
  private static final float R = 6;

  /*package*/ WarriorProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    if (((Warrior)locatableAgent).isDefender()){
      applet.ellipse(x, y, 2 * R, 2 * R);
    }else if (((Warrior)locatableAgent).isAttacker()) {
      applet.triangle(x, y, x + 2 * R, y + R, x, y + 2 * R);
    }else
      applet.ellipse(x, y, 2 * R, R);
    /* See attack distance
    float attackD = ((Warrior) locatableAgent).getAttackD();
    applet.noFill();
    applet.stroke(255);
    applet.ellipse(x-R,y-R,attackD,attackD);
    applet.noStroke();*/
  }
}
