package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;

import java.util.UUID;

/*package*/ class CastleProcessingVisualAgent extends ProcessingVisualAgent{
  private static final float R = 10;

  /*package*/ CastleProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    applet.rect(x-R,y-R,2*R,2*R);
    applet.noFill();
    float viewDistance = (float) ((Castle)locatableAgent).characteristics().viewDistance();
    applet.stroke(applet.color(120,100,100));
    applet.ellipse(x-viewDistance,y-viewDistance,2*viewDistance,2*viewDistance);
    float attackDistance = (float) ((Castle)locatableAgent).characteristics().attackDistance();
    applet.stroke(applet.color(0,100,100));
    applet.ellipse(x-attackDistance,y-attackDistance,2*attackDistance,2*attackDistance);
    applet.noStroke();
  }
}
