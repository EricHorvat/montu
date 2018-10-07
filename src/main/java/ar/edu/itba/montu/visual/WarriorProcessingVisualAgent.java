package ar.edu.itba.montu.visual;

import java.util.UUID;

import ar.edu.itba.montu.abstraction.LocatableAgent;

/*package*/ class WarriorProcessingVisualAgent extends ProcessingVisualAgent{
  private static final float R = 6;

  /*package*/ WarriorProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    applet.ellipse(x - R, y - R, 2 * R, 2 * R);
    /* See attack distance
    float attackD = ((Warrior) locatableAgent).getAttackD();
    applet.noFill();
    applet.stroke(255);
    applet.ellipse(x-R,y-R,attackD,attackD);
    applet.noStroke();*/
  }
}
