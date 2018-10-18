package ar.edu.itba.montu.visual;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.environment.WarEnvironment;

/*package*/ abstract class ProcessingVisualAgent extends VisualAgent{

  private static List<ProcessingVisualAgent> visualAgents = new ArrayList<>();
  private static final float MAX_R = 10;
  float x;
  float y;
  
  private final static boolean DEBUG = true;

  /*package*/ ProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
    visualAgents.add(0,this);
  }
  
  private static double linearMap(double x, double a, double b, double c, double d) {
  	return (x - a) / (b - a) * (d - c) + c;
  }
  
  /*package*/ static float linearMapX(double value) {
    ProcessingApplet applet = ProcessingApplet.instance();
    return (float)linearMap(value, 0, applet.getL(), 0, applet.getW());
  }
  
  /*package*/ static float linearMapY(double value) {
    ProcessingApplet applet = ProcessingApplet.instance();
    return (float)linearMap(value, 0, applet.getL(), 0, applet.getH());
  }
  
  private static String toHumanReadable(final Long time) {
  	final long days = TimeUnit.MINUTES.toDays(time);
    return String.format("%d days", days);
  }

  /*package*/ void draw(ProcessingApplet applet) {
    x = (float)linearMap(locatableAgent.location().X, 0, applet.getL(), 0, applet.getW());
    y = (float)linearMap(locatableAgent.location().Y, 0, applet.getL(), 0, applet.getH());
    		
    		//(float) locatableAgent.location().X + MAX_R;
//    y = (float) locatableAgent.location().Y + MAX_R;
  
    if (DEBUG) {
    	applet.fill(applet.color(255));
    	if (locatableAgent instanceof Castle) {
    		applet.text(locatableAgent.toString(), x - 20, y + 20);
    	}
      
      applet.text(toHumanReadable(WarEnvironment.getInstance().time()), 10, 10);
    }
    int sat = !locatableAgent.isAlive() ? 0 : (25 + locatableAgent.getHealthPointPercentage() * 75 / 100);
    int c = applet.color(KingdomColorGetter.getHueValue(locatableAgent.kingdom()), 100, sat);
    applet.fill(c);
  
  }

  /*package*/ static List<ProcessingVisualAgent> getAgents(){
    /*If dead print animation or not print*/
    visualAgents.removeAll(visualAgents.stream().filter(va -> !va.locatableAgent.isAlive()).collect(Collectors.toList()));
    return visualAgents;
  }


}
