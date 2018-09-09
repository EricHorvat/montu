package ar.edu.itba.montu.war.kingdom;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.montu.interfaces.ICastle;
import ar.edu.itba.montu.interfaces.IKingdom;

public class KingdomBuilder {
	
	private final String name;
	
	private KingdomCharacteristics kingdomCharacteristics;
	
	private final List<ICastle> castles = new ArrayList<>();
	
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
	
	public KingdomBuilder andCastles(final List<ICastle> castles) {
		this.castles.addAll(castles);
		return this;
	}
	
	public IKingdom build() {
		return new Kingdom(name, kingdomCharacteristics, castles);
	}
}
