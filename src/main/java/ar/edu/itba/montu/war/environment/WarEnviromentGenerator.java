package ar.edu.itba.montu.war.environment;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.visual.KingdomColorGetter;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.kingdom.KingdomBuilder;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.Coordinate;

public class WarEnviromentGenerator {
	
	private static final Logger logger = LogManager.getLogger(WarEnviromentGenerator.class);
	
	public static void generateWithConfiguration(final Configuration config) {
		
		logger.info("Loading configuration to environment");
		
		final List<Kingdom> kingdoms = config.getKingdoms().stream().map(k -> {
			final KingdomCharacteristics kingdomCharacteristics = KingdomCharacteristics.withOffenseCapacity(k.getOffenseCapacity());
			return KingdomBuilder
					.withName(k.getName())
					.withKingdomCharacteristics(
							kingdomCharacteristics
					)
					.andCastles(
							k.getCastles().stream().map(c -> {
								return CastleBuilder
										.withName(
												c.getName(),
												Coordinate.at(c.getLocation().getLat(), c.getLocation().getLng())
										)
										.withCastleCharacteristics(
												CastleCharacteristics.standardCharacteristics(kingdomCharacteristics)
										);
							}).collect(Collectors.toList())
					)
					.build();
		}).collect(Collectors.toList());
		
		logger.info("Done creating environment");

    WarEnvironment.withKingdomsAndStrategy(WarStrategy.CAPTURE_THE_FLAG, kingdoms);
		KingdomColorGetter.putKingdoms(kingdoms);
  }
	
}
