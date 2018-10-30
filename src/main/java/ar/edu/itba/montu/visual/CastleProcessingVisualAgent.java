package ar.edu.itba.montu.visual;

import java.util.UUID;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;

/*package*/ class CastleProcessingVisualAgent extends ProcessingVisualAgent{
  private static final float R = 0.7f;

  /*package*/ CastleProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    final float Rx = zoomLinearMapX(R);
    final float Ry = zoomLinearMapY(R);
    applet.rect(x - Rx, y - Ry, 2 * Rx, 2 * Ry);
    applet.noFill();
    if (ProcessingApplet.drawDistance) {
    	final Castle castle = (Castle)locatableAgent; 
      final float viewDistance = (float)castle.characteristics().viewDistance();
      final float attackDistance = (float)castle.characteristics().attackDistance();
      final int color = castle.kingdom().color(); 
      
      int r = (color & 0xff0000) >> 16;
      int g = (color & 0x00ff00) >> 8;
      int b = color & 0x0000ff;
      
      applet.stroke(r, g, b);
      applet.ellipse(x, y, zoomLinearMapX(2 * viewDistance), zoomLinearMapY(2 * viewDistance));
      
      applet.stroke(r, g, b);
      applet.ellipse(x, y, zoomLinearMapX(2 * attackDistance), zoomLinearMapY(2 * attackDistance));
      
      applet.stroke(0x000000);
    }
  }
}
