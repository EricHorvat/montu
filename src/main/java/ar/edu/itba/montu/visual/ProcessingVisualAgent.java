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
    float r = 10;
    applet.color(KingdomColorGetter.getHueValue(locatableAgent.kingdom()),100,100);
    applet.ellipse(x-r,y-r,2*r,2*r);
  }

  /*package*/ static List<ProcessingVisualAgent> getAgents(){
    /*If dead print animation or not print*/
    return visualAgents;
  }


}
