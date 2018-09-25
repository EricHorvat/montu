package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VisualAgent {

  private static List<VisualAgent> visualAgents = new ArrayList<>();
  UUID uid;
  //Shape
  //Color
  LocatableAgent locatableAgent;

  public VisualAgent(UUID uid, LocatableAgent locatableAgent) {
    this.uid = uid;
    this.locatableAgent = locatableAgent;
    visualAgents.add(this);
  }

  //     Shape?Coordinate?
  public void tick(){
    //updColor?
    /*upd coordinate*/ locatableAgent.location();
  }

  /*package*/ static List<VisualAgent> getAgents(){
    /*If dead print animation or not print*/
    return visualAgents;
  }

  /*package*/ void draw(ProcessingApplet applet){
    float x = (float) locatableAgent.location().X;
    float y = (float) locatableAgent.location().Y;
    applet.ellipse(x-1,y-1,x+1,y+1);
  }

}
