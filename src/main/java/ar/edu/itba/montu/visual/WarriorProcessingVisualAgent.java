package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;

import java.util.UUID;

public class WarriorProcessingVisualAgent extends ProcessingVisualAgent{
  static float R = 6;

  public WarriorProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    applet.ellipse(x,y,2*R,2*R);
  }
}
