package ar.edu.itba.montu.war.environment;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.kingdom.KingdomBuilder;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarEnviromentGenerator {
	
	public static void generate() {
		final Random random = RandomUtil.getRandom();
		final double L = 500;
		
		final long kingdomCount = RandomUtil.getIntExponentialDistribution(0.5) + 2;
		final List<Kingdom> kingdoms = LongStream.range(0, kingdomCount).mapToObj(i -> {
			final String kindomName = String.format("Kingdom %d", random.nextGaussian());
			final long castleCount = RandomUtil.getIntExponentialDistribution(1) + 1;
			final List<CastleBuilder> castles = LongStream.range(0, castleCount).mapToObj(j -> {
				final Coordinate coordinate = Coordinate.at(random.nextDouble() * L, random.nextDouble() * L);
				return CastleBuilder
						.withName(String.format("%s %d", kindomName, j), coordinate)
						.withCastleCharacteristics(CastleCharacteristics.standardCharacteristics());
			}).collect(Collectors.toList());
			return KingdomBuilder
					.withName(kindomName)
					.withKingdomCharacteristics(KingdomCharacteristics.withSpeedLifespanAndAttack(random.nextDouble(), random.nextDouble(), random.nextDouble()))
					.andCastles(castles)
					.build();
		}).collect(Collectors.toList());

    WarEnvironment.withKingdomsAndStrategy(WarStrategy.CAPTURE_THE_FLAG, kingdoms);
  }
	
}
