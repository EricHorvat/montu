package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;
import ar.edu.itba.montu.war.castle.Castle;

import java.util.UUID;

public abstract class VisualAgent {

  UUID uid;
  //Shape
  //Color
  LocatableAgent locatableAgent;

  /*package*/ VisualAgent(UUID uid, LocatableAgent locatableAgent) {
    this.uid = uid;
    this.locatableAgent = locatableAgent;
  }

  //     Shape?Coordinate?
  public void tick(){
    //updColor?
    /*upd coordinate*/ locatableAgent.location();
  }

  public static VisualAgent buildNew(UUID uid, LocatableAgent locatableAgent){
    if(locatableAgent.getClass().equals(Castle.class)){
      return new CastleProcessingVisualAgent(uid, locatableAgent);
    } else{
      return new WarriorProcessingVisualAgent(uid, locatableAgent);
    }
  }

  /*package*/ abstract void draw(ProcessingApplet applet);

}
