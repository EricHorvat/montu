package ar.edu.itba.montu.war.kingdom;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.montu.war.castle.Castle;

public class KingdomBuilder {
	
	private final String name;
	
	private KingdomCharacteristics kingdomCharacteristics;
	
	private final List<Castle> castles = new ArrayList<>();
	
	private KingdomBuilder(final String name) {
		this.name = name;
	}
	
	public static KingdomBuilder withName(final String name) {
		final KingdomBuilder kingdomBuilder = new KingdomBuilder(name);
		return kingdomBuilder;
	}
	
	public KingdomBuilder withKingdomCharacteristics(final KingdomCharacteristics characteristics) {
		this.kingdomCharacteristics = characteristics;
		return this;
	}
	
	public KingdomBuilder andCastles(final List<Castle> castles) {
		this.castles.addAll(castles);
		return this;
	}
	
	public Kingdom build() {
		return new Kingdom(name, kingdomCharacteristics, castles);
	}
}
