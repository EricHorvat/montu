package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.scene.WarScene;

public interface WarAgent extends Agent {

    void loop(WarScene warScene);

    Kingdom getKingdom();

}
