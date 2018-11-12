package ar.edu.itba.montu.visual;

import java.util.UUID;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.people.Warrior;

/*package*/ class WarriorProcessingVisualAgent extends ProcessingVisualAgent{
  private static final float R = 0.35f;

  /*package*/ WarriorProcessingVisualAgent(UUID uid, LocatableAgent locatableAgent) {
    super(uid, locatableAgent);
  }

  @Override
  void draw(ProcessingApplet applet) {
    super.draw(applet);
    final float scale = (((Warrior)locatableAgent).isSuper() ? 1.5f : 1);
    final float Rx = zoomLinearMapX(R) * scale;
    final float Ry = zoomLinearMapY(R) * scale;
  
    if (((Warrior)locatableAgent).isDefender()){
      applet.ellipse(x, y, 2 * Rx, 2 * Ry);
    }else if (((Warrior)locatableAgent).isAttacker()) {
      applet.triangle(x-Rx, y-Ry, x + Rx, y, x-Rx, y + Ry);
    }else{
      //WARN: Should never get here
      return;
    }
  
    applet.noFill();
    if (ProcessingApplet.drawDistance) {
    	final Warrior w = (Warrior) locatableAgent;
      final float viewDistance = (float)w.characteristics().viewDistance();
      final float attackDistance = (float)w.characteristics().attackDistance();
      final int color = w.kingdom().color(); 
      
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
