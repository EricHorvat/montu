package ar.edu.itba.montu.abstraction;

import ar.edu.itba.montu.war.kingdom.Kingdom;

public interface IWarAgent extends IAgent {

    void loop();

    Kingdom getKingdom();

}
