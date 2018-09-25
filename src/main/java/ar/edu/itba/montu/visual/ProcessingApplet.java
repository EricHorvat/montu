package ar.edu.itba.montu.visual;

import processing.core.PApplet;

public class ProcessingApplet extends PApplet {

  @Override
  public void draw() {
    clear();
    super.draw();
    ProcessingVisualAgent.getAgents().forEach(visualAgent -> visualAgent.draw(this));
    noLoop();
  }
}
