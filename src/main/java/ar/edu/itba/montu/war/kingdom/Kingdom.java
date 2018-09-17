package ar.edu.itba.montu.war.kingdom;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.montu.abstraction.WarAgent;
import ar.edu.itba.montu.interfaces.IScene;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.scene.WarStrategy;

public class Kingdom implements WarAgent {
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	
	private Optional<WarStrategy> strategy;
	
	private KingdomStatus status = KingdomStatus.ALIVE;
	
	private final List<Castle> castles;

	/* package */protected Kingdom(final String name, final KingdomCharacteristics kingdomCharacteristics, final List<Castle> castles) {
		this.name = name;
		this.characteristics = kingdomCharacteristics;
		this.castles = castles;
	}

	public void enforceStrategy(final WarStrategy strategy) {
		this.strategy = Optional.of(strategy);
	}

	public KingdomStatus getCurrentStatus() {
		return status;
	}

	public void actOnTurn(final long timeEllapsed, final IScene scene, final List<Kingdom> otherKingdoms) {
		
	}
	
}
