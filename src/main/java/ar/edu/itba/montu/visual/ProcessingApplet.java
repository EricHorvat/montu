package ar.edu.itba.montu.visual;

import processing.core.PApplet;

import java.util.Iterator;
import java.util.List;

public class ProcessingApplet extends PApplet {

  int L;

  @Override
  public void setup() {
    super.setup();
  }

  public ProcessingApplet(int L) {
    this.L = L;
    setSize(L,L);
  }

  @Override
  public void draw() {
    clear();
    colorMode(HSB, 360,100,100);
    List<ProcessingVisualAgent> a = ProcessingVisualAgent.getAgents();
    for(int i = a.size() - 1; i >=0; i-- ){
      a.get(i).draw(this);
    }
    noLoop();
  }
}
