package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.war.people.Warrior;
import processing.core.PImage;

import static ar.edu.itba.montu.visual.ProcessingApplet.drawDistance;
import static ar.edu.itba.montu.visual.ProcessingVisualAgent.*;

public class Background{

	PImage image;
	public Background(ProcessingApplet processingApplet){
		image = processingApplet.loadImage("balcan.png");
	}
	
	public void draw(ProcessingApplet applet){
		final float Rx = zoomLinearMapX(800);
		final float Ry = zoomLinearMapY(800);
		
		final float x = zoomLinearMapXfromOrigin(0);
		final float y = zoomLinearMapYfromOrigin(0);
		
		applet.image(image,x,y,Rx,Ry);
		
		
	}
}
