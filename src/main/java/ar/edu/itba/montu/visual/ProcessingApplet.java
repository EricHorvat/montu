package ar.edu.itba.montu.visual;

import processing.core.PApplet;

public class ProcessingApplet extends PApplet {

  int L;

  public ProcessingApplet(int L) {
    this.L = L;
    setSize(L,L);
  }

  @Override
  public void draw() {
    clear();
    colorMode(HSB, 360,100,100);
    super.draw();
    ProcessingVisualAgent.getAgents().forEach(visualAgent -> visualAgent.draw(this));
    noLoop();
  }
}
