package ar.edu.itba.montu.visual;

import java.util.UUID;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;
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
      applet.triangle(x-R, y-R, x + R, y, x-R, y + R);
    }else
      applet.ellipse(x, y, 2 * R, R);
  
    applet.noFill();
    float viewDistance = (float) ((Warrior)locatableAgent).characteristics().viewDistance();
    applet.stroke(applet.color(120,100,100));
    applet.ellipse(x,y,2*viewDistance,2*viewDistance);
    float attackDistance = (float) ((Warrior)locatableAgent).characteristics().attackDistance();
    applet.stroke(applet.color(0,100,100));
    applet.ellipse(x,y,2*attackDistance,2*attackDistance);
    applet.stroke(applet.color(0,0,0));
  }
}
