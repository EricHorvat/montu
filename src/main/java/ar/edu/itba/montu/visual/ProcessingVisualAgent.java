package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProcessingVisualAgent extends VisualAgent{

  private static List<ProcessingVisualAgent> visualAgents = new ArrayList<>();

  public ProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
    visualAgents.add(this);
  }

  /*package*/ void draw(ProcessingApplet applet){
    float x = (float) locatableAgent.location().X;
    float y = (float) locatableAgent.location().Y;
    applet.ellipse(x-1,y-1,x+1,y+1);
  }

  /*package*/ static List<ProcessingVisualAgent> getAgents(){
    /*If dead print animation or not print*/
    return visualAgents;
  }
}
