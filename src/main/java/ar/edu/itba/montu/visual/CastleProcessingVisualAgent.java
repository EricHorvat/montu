package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;

import java.util.UUID;

public class CastleProcessingVisualAgent extends ProcessingVisualAgent{
  static float R = 10;

  public CastleProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    applet.rect(x,y,2*R,2*R);
  }
}
