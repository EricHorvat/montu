package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;

import java.util.UUID;

/*package*/ class CastleProcessingVisualAgent extends ProcessingVisualAgent{
  private static final float R = 0.7f;

  /*package*/ CastleProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    final float Rx = zoomLinearMapX(R);
    final float Ry = zoomLinearMapY(R);
    applet.rect(x-Rx,y-Ry,2*Rx,2*Ry);
    applet.noFill();
    if(ProcessingApplet.drawDistance) {
      float viewDistance = (float) ((Castle) locatableAgent).characteristics().viewDistance();
      applet.stroke(applet.color(120, 100, 100));
      applet.ellipse(x, y, zoomLinearMapX(2 * viewDistance), zoomLinearMapY(2 * viewDistance));
      float attackDistance = (float) ((Castle) locatableAgent).characteristics().attackDistance();
      applet.stroke(applet.color(0, 100, 100));
      applet.ellipse(x, y, zoomLinearMapX(2 * attackDistance), zoomLinearMapY(2 * attackDistance));
      applet.stroke(applet.color(0, 0, 0));
    }
  }
}
