package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.App;
import processing.core.PImage;

import java.util.Optional;

import static ar.edu.itba.montu.visual.ProcessingVisualAgent.*;

/*package*/ class Background{

	private PImage image;
	/*package*/ Background(ProcessingApplet processingApplet){
		Optional.ofNullable(App.getConfiguration().getViewport().getBackgroundImage()).ifPresent(s -> image = processingApplet.loadImage(s));
	}
	
	/*package*/ void draw(ProcessingApplet applet){
		if(image != null) {
			final float Rx = zoomLinearMapX(800);
			final float Ry = zoomLinearMapY(800);
			
			final float x = zoomLinearMapXfromOrigin(0);
			final float y = zoomLinearMapYfromOrigin(0);
			applet.image(image, x, y, Rx, Ry);
		}
	}
}
