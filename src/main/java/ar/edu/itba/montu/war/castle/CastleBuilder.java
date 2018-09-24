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
	private double height = CastleHeight.GROUND_LEVEL.height();
	
	private CastleBuilder(final String name, final Coordinate coordinate) {
		this.name = name;
		this.coordinate = coordinate;
	}
	
	public static CastleBuilder defenseCastle(final String name, final Coordinate coordinate) {
		return new CastleBuilder(name, coordinate).withCastleCharacteristics(CastleCharacteristics.defenseCharacteristics());
//		return new Castle(null, name, CastleCharacteristics.defenseCharacteristics(), coordinate, 0, 0, CastleHeight.GROUND_LEVEL.height());
	}
	
	public static CastleBuilder defenseCastle(final String name, final Coordinate coordinate, final int warriors, final int healers) {
		return new CastleBuilder(name, coordinate).warriors(warriors).healers(healers).withCastleCharacteristics(CastleCharacteristics.defenseCharacteristics());
//		return new Castle(null, name, CastleCharacteristics.defenseCharacteristics(), coordinate, warriors, healers, CastleHeight.GROUND_LEVEL.height());
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
	
	public CastleBuilder height(final double height) {
		this.height = height;
		return this;
	}
	
	public CastleBuilder kingdom(final Kingdom kingdom) {
		this.kingdom = kingdom;
		return this;
	}
	
	public Castle build() {
		if (characteristics == null) {
			characteristics = CastleCharacteristics.standardCharacteristics();
		}
		return new Castle(kingdom, name, characteristics, coordinate, warriors, healers, height);
	}
}
