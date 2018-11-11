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
  
  private static double linearMap(double number, double in_min, double in_max, double out_min, double out_max) {
  	return (number - in_min) / (in_max - in_min) * (out_max - out_min) + out_min;
  }
  
  private static float zoomLinearMapX(double value, boolean fromOrigin) {
    ProcessingApplet applet = ProcessingApplet.instance();
    double w = applet.W * applet.getZoomLevel();
    float mappedValue = (float)linearMap(value, 0, applet.getL(), 0, w);
    float mappedOrigin = fromOrigin ? (float)linearMap(applet.originX, 0, applet.getL(), 0, w) : 0;
    return mappedValue-mappedOrigin;
  }
	
	private static float inverseZoomLinearMapX(double value, boolean fromOrigin) {
		ProcessingApplet applet = ProcessingApplet.instance();
		double w = applet.W * applet.getZoomLevel();
		float mappedOrigin = fromOrigin ? (float)linearMap(applet.originX, 0, applet.getL(), 0, w) : 0;
		return (float)linearMap(value + mappedOrigin, 0, w, 0, applet.getL());
	}
  
  /*package*/ static float zoomLinearMapX(double value) {
    return zoomLinearMapX(value,false);
  }
  
  /*package*/ static float zoomLinearMapXfromOrigin(double value) {
    return zoomLinearMapX(value,true);
  }
	
	/*package*/ static float inverseZoomLinearMapX(double value) {
		return inverseZoomLinearMapX(value,false);
	}
	
	/*package*/ static float inverseZoomLinearMapYfromOriginX(double value) {
		return inverseZoomLinearMapX(value,true);
	}
  
  private static float zoomLinearMapY(double value, boolean fromOrigin) {
    ProcessingApplet applet = ProcessingApplet.instance();
    double h = applet.H * applet.getZoomLevel();
    float mappedValue = (float)linearMap(value, 0, applet.getL(), 0, h);
    float mappedOrigin = fromOrigin ? (float)linearMap(applet.originY, 0, applet.getL(), 0, h) : 0;
    return mappedValue-mappedOrigin;
  }
  
  private static float inverseZoomLinearMapY(double value, boolean fromOrigin) {
    ProcessingApplet applet = ProcessingApplet.instance();
    double h = applet.H * applet.getZoomLevel();
    float mappedOrigin = fromOrigin ? (float)linearMap(applet.originY, 0, applet.getL(), 0, h) : 0;
    return (float)linearMap(value + mappedOrigin, 0, h, 0, applet.getL());
  }
  
  /*package*/ static float zoomLinearMapY(double value) {
    return zoomLinearMapY(value,false);
  }
  
  /*package*/ static float zoomLinearMapYfromOrigin(double value) {
    return zoomLinearMapY(value,true);
  }
  
  /*package*/ static float inverseZoomLinearMapY(double value) {
    return inverseZoomLinearMapY(value,false);
  }
  
  /*package*/ static float inverseZoomLinearMapYfromOrigin(double value) {
    return inverseZoomLinearMapY(value,true);
  }
  
  private static String toHumanReadable(final Long time) {
  	final long days = TimeUnit.MINUTES.toDays(time);
    return String.format("%d days", days);
  }

  /*package*/ void draw(ProcessingApplet applet) {
    x = zoomLinearMapXfromOrigin(locatableAgent.location().X);
    y = zoomLinearMapYfromOrigin(locatableAgent.location().Y);
    		
    		//(float) locatableAgent.location().X + MAX_R;
//    y = (float) locatableAgent.location().Y + MAX_R;
  
    if (DEBUG) {
    	applet.fill(applet.color(255));
    	if (locatableAgent instanceof Castle) {
    		applet.text(locatableAgent.toString(), x - 20, y + 20);
    	}
      
      applet.text(toHumanReadable(WarEnvironment.getInstance().time()), 10, 10);
    }
    
    int color = locatableAgent.kingdom().color();
    int r = (color & 0xff0000) >> 16;
    int g = (color & 0x00ff00) >> 8;
    int b = color & 0x0000ff;
    float a = (float)(Math.max(locatableAgent.getHealthPointPercentage(), 25)*2.55);
    
    applet.fill(r, g, b, a);
  
  }

  /*package*/ static List<ProcessingVisualAgent> getAgents(){
    /*If dead print animation or not print*/
    visualAgents.removeAll(visualAgents.stream().filter(va -> !va.locatableAgent.isAlive()).collect(Collectors.toList()));
    return visualAgents;
  }


}
