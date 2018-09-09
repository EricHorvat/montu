package ar.edu.itba.montu.war.castle;

import ar.edu.itba.montu.interfaces.ICastle;
import ar.edu.itba.montu.war.utils.Coordinate;

public class CastleBuilder {
	
	private final String name;
	private final Coordinate coords;
	
	private CastleCharacteristics characteristics;
	
	private CastleBuilder(final String name, final Coordinate coords) {
		this.name = name;
		this.coords = coords;
	}
	
	public static CastleBuilder withName(final String name, final Coordinate coords) {
		final CastleBuilder builder = new CastleBuilder(name, coords);
		return builder;
	}
	
	public CastleBuilder withCastleCharacteristics(final CastleCharacteristics characteristics) {
		this.characteristics = characteristics;
		return this;
	}
	
	public ICastle build() {
		if (characteristics == null) {
			characteristics = CastleCharacteristics.standardCharacteristics();
		}
		return new Castle(name, characteristics, coords);
	}
}
