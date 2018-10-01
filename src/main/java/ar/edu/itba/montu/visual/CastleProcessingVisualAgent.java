package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;

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
  }
}
