package ar.edu.itba.montu.visual;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class ProcessingApplet extends PApplet {
	
	int W, H, L;
	float originX = 0, originY = 0;
	
	private static ProcessingApplet instance;
	private static Background background;
	public static boolean drawDistance = false;
	public static boolean drawLabels = false;
	public static boolean paused = false;
	int zoomLevel = 0;
  
  @Override
  public void setup() {
    super.setup();
	  background = new Background(this);
  }
  
  public static ProcessingApplet instance() {
  	if (instance == null) {
  		throw new RuntimeException("instance is not initialized");
  	}
  	return instance;
  }
  
  public static ProcessingApplet init(int W, int H, int L) {
  	if (instance == null) {
  		instance = new ProcessingApplet(W, H, L);
  	}
  	return instance;
  }

  private ProcessingApplet(int W, int H, int L) {
    this.W = W;
    this.H = H;
    this.L = L;
    setSize(W, H);
  }

  @Override
  public void draw() {
    clear();
    colorMode(RGB);
    
    background.draw(this);
    
//    colorMode(HSB, 360, 100, 100, 100);
    List<ProcessingVisualAgent> a = ProcessingVisualAgent.getAgents();
    for (int i = a.size() - 1; i >= 0; i--) {
      a.get(i).draw(this);
    }
    updateOrigin();
    noLoop();
  }
  
  private void updateOrigin(){
	  if (mouseX < getW() * 0.1 || (keyPressed && key == CODED && keyCode == LEFT)){
		  originX -= ProcessingVisualAgent.inverseZoomLinearMapX(50);
	  }
	  if (mouseX > getW() * 0.9 || (keyPressed && key == CODED && keyCode == RIGHT)){
		  originX += ProcessingVisualAgent.inverseZoomLinearMapX(50);
	  }
	  if (mouseY < getH() * 0.1 || (keyPressed && key == CODED && keyCode == UP)){
		  originY -= ProcessingVisualAgent.inverseZoomLinearMapY(50);
	  }
	  if (mouseY > getH() * 0.9 || (keyPressed && key == CODED && keyCode == DOWN)){
		  originY += ProcessingVisualAgent.inverseZoomLinearMapY(50);
	  }
	  originX = originX < 0 ? 0 : originX;
	  originY = originY < 0 ? 0 : originY;
	  originX = getL() - originX < ProcessingVisualAgent.inverseZoomLinearMapX(getW()) ? getL() - ProcessingVisualAgent.inverseZoomLinearMapX(getW()) : originX;
	  originY = getL() - originY < ProcessingVisualAgent.inverseZoomLinearMapY(getH()) ? getL() - ProcessingVisualAgent.inverseZoomLinearMapY(getH()) : originY;
  }
	
	@Override
	public void mouseClicked() {
		super.mouseClicked();
		switch (mouseButton ){
			case RIGHT:
				if (zoomLevel != 10){
					zoomLevel += 1;
				}
				break;
			case LEFT:
				if (zoomLevel != 0){
					zoomLevel -= 1;
				}
				break;
		}
	}
	
	public float getZoomLevel() {
		return (float) Math.pow(2,zoomLevel);
	}
	
	@Override
	public void keyPressed() {
		super.keyPressed();
		switch (key){
			case 'd':
			case 'D':
				drawDistance = true;
				break;
			case 'v':
			case 'V':
				drawDistance = !drawDistance;
				break;
			case 'l':
			case 'L':
				drawLabels = !drawLabels;
				break;
			case 'p':
			case 'P':
				paused = !paused;
				break;
		}
	}
	
	public int getW() {
		return W;
	}

	public int getH() {
		return H;
	}

	public int getL() {
		return L;
	}
  
}
