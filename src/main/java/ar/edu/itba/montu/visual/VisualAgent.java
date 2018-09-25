package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.abstraction.LocatableAgent;

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
    return new ProcessingVisualAgent(uid, locatableAgent);
  }

  /*package*/ abstract void draw(ProcessingApplet applet);

}
