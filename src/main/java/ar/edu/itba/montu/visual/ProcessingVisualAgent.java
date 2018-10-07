package ar.edu.itba.montu.visual;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ar.edu.itba.montu.abstraction.LocatableAgent;

/*package*/ abstract class ProcessingVisualAgent extends VisualAgent{

  private static List<ProcessingVisualAgent> visualAgents = new ArrayList<>();
  private static final float MAX_R = 10;
  float x;
  float y;
  
  private final static boolean DEBUG = false;

  /*package*/ ProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
    visualAgents.add(this);
  }
  
  private static double linearMap(double x, int a, int b, int c, int d) {
  	return (x - a) / (b - a) * (d - c) + c;
  }

  /*package*/ void draw(ProcessingApplet applet) {
    x = (float)linearMap(locatableAgent.location().X, 0, applet.getL(), 0, applet.getW());
    y = (float)linearMap(locatableAgent.location().Y, 0, applet.getL(), 0, applet.getH());
    		
    		//(float) locatableAgent.location().X + MAX_R;
//    y = (float) locatableAgent.location().Y + MAX_R;
  
    if (DEBUG) {
      applet.fill(applet.color(255));
      applet.text(locatableAgent.toString(), x, y);
    }
    int sat = !locatableAgent.isAlive() ? 0 : (25 + locatableAgent.getHealthPointPercentage() * 75 / 100);
    int c = applet.color(KingdomColorGetter.getHueValue(locatableAgent.kingdom()),100,sat);
    applet.fill(c);
  
  }

  /*package*/ static List<ProcessingVisualAgent> getAgents(){
    /*If dead print animation or not print*/
    visualAgents.removeAll(visualAgents.stream().filter(va -> !va.locatableAgent.isAlive()).collect(Collectors.toList()));
    return visualAgents;
  }


}
