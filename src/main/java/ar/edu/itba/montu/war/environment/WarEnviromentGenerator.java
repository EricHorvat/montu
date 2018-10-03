package ar.edu.itba.montu.war.environment;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.App;
import ar.edu.itba.montu.visual.KingdomColorGetter;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.kingdom.KingdomBuilder;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarEnviromentGenerator {
	
	private static final Logger logger = LogManager.getLogger(WarEnviromentGenerator.class);
	
	public static void generate() {
		final Random random = RandomUtil.getRandom();
		final double MAP_SIZE = 500;
		
		final long kingdomCount = RandomUtil.getIntExponentialDistribution(0.5) + 2;
		logger.info("Creating {} kingdoms", kingdomCount);
		final List<Kingdom> kingdoms = LongStream.range(0, kingdomCount).mapToObj(i -> {
			final String kingdomName = String.format("Kingdom %d", random.nextInt());
			final long castleCount = RandomUtil.getIntExponentialDistribution(1) + 1;
			logger.info("Creating {} castles for KINGDOM={}", castleCount, kingdomName);
			final KingdomCharacteristics kingdomCharacteristics = KingdomCharacteristics.withOffenseCapacity(random.nextDouble() * 100);
			final List<CastleBuilder> castles = LongStream.range(0, castleCount).mapToObj(j -> {
				final Coordinate coordinate = Coordinate.at(random.nextDouble() * MAP_SIZE, random.nextDouble() * MAP_SIZE);
				return CastleBuilder
						.withName(String.format("%s %d", kingdomName, j), coordinate)
						.withCastleCharacteristics(CastleCharacteristics.standardCharacteristics(kingdomCharacteristics));
			}).collect(Collectors.toList());
			return KingdomBuilder
					.withName(kingdomName)
					.withKingdomCharacteristics(kingdomCharacteristics)
					.andCastles(castles)
					.build();
		}).collect(Collectors.toList());
		
		logger.info("Done creating environment");

    WarEnvironment.withKingdomsAndStrategy(WarStrategy.CAPTURE_THE_FLAG, kingdoms);
		KingdomColorGetter.putKingdoms(kingdoms);
  }
	
}
