package ar.edu.itba.montu.visual;

import java.util.List;

import processing.core.PApplet;

public class ProcessingApplet extends PApplet {

  int W, H, L;
	
	private static ProcessingApplet instance;
	public static boolean drawDistance = false;
	public static int zoomLevel = 0;
  
  @Override
  public void setup() {
    super.setup();
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
    colorMode(HSB, 360, 100, 100, 100);
    List<ProcessingVisualAgent> a = ProcessingVisualAgent.getAgents();
    for (int i = a.size() - 1; i >= 0; i--) {
      a.get(i).draw(this);
    }
    fill(100,100,100);
    rect(0,0,W/getZoomLevel(),H/getZoomLevel());
    noFill();
    noLoop();
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
	
	public static float getZoomLevel() {
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
