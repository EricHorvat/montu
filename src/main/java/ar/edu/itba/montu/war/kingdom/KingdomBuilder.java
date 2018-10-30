package ar.edu.itba.montu.war.kingdom;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.montu.war.castle.CastleBuilder;

public class KingdomBuilder {
	
	private final String name;
	
	private KingdomCharacteristics kingdomCharacteristics;
	
	private final List<CastleBuilder> castles = new ArrayList<>();
	
	private int color = 0xffffff;
	
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
	
	public KingdomBuilder andCastles(final List<CastleBuilder> castles) {
		this.castles.addAll(castles);
		return this;
	}
	
	public KingdomBuilder withColor(final int color) {
		this.color = color;
		return this;
	}
	
	public Kingdom build() {
		final Kingdom k = new Kingdom(name, kingdomCharacteristics, castles);
		k.changeColor(this.color);
		return k;
	}
}
