package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*package*/ abstract class ProcessingVisualAgent extends VisualAgent{

  private static List<ProcessingVisualAgent> visualAgents = new ArrayList<>();
  private static final float MAX_R = 10;
  float x;
  float y;

  /*package*/ ProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
    visualAgents.add(this);
  }

  /*package*/ void draw(ProcessingApplet applet){

    x = (float) locatableAgent.location().X+MAX_R;
    y = (float) locatableAgent.location().Y+MAX_R;
    int sat = !locatableAgent.isAlive() ? 0 : (25 + locatableAgent.getHealthPointPercentage() * 75 / 100);
    int c = applet.color(KingdomColorGetter.getHueValue(locatableAgent.kingdom()),100,sat);
    applet.fill(c);
  }

  /*package*/ static List<ProcessingVisualAgent> getAgents(){
    /*If dead print animation or not print*/
    return visualAgents;
  }


}
