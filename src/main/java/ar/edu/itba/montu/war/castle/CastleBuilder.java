package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.Coordinate;

public class CastleBuilder {
	
	private final String name;
	private final Coordinate coordinate;
	
	private CastleCharacteristics characteristics;
	private Kingdom kingdom;
	private int warriors = 0;
	private int healers = 0;
	
	private CastleBuilder(final String name, final Coordinate coordinate) {
		this.name = name;
		this.coordinate = coordinate;
	}
	
	public static CastleBuilder withName(final String name, final Coordinate coordinate) {
		final CastleBuilder builder = new CastleBuilder(name, coordinate);
		return builder;
	}
	
	public CastleBuilder withCastleCharacteristics(final CastleCharacteristics characteristics) {
		this.characteristics = characteristics;
		return this;
	}
	
	public CastleBuilder warriors(final int warriors)  {
		this.warriors = warriors;
		return this;
	}
	
	public CastleBuilder healers(final int healers) {
		this.healers = healers;
		return this;
	}
	
	public CastleBuilder kingdom(final Kingdom kingdom) {
		this.kingdom = kingdom;
		return this;
	}
	
	public Castle build() {
		return new Castle(kingdom, name, characteristics, coordinate, warriors, healers);
	}
}
