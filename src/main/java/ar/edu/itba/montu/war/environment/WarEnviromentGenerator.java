package ar.edu.itba.montu.war.environment;

import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.kingdom.KingdomBuilder;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WarEnviromentGenerator {
  public static void generate(){
    Random random = RandomUtil.getRandom();
    double L = 500;

    long kingdomCount = RandomUtil.getIntExponentialDistribution(0.5) + 2;
    List<Kingdom> kingdoms = new ArrayList<>();

    for(int i = 0; i < kingdomCount; i++){
      String kname = random.nextGaussian() + "";
      List<Castle> castles = new ArrayList<>();
      long castleCount = RandomUtil.getIntExponentialDistribution(1) + 1;
      for(int j = 0; j < castleCount; j++){
        Coordinate coordinate = new Coordinate(random.nextDouble() * L, random.nextDouble() * L);
        castles.add(
            CastleBuilder.withName(kname + " " + j, coordinate)
                .withCastleCharacteristics(CastleCharacteristics.standardCharacteristics())
                .build()
        );
      }
      kingdoms.add(
          KingdomBuilder
              .withName(kname)
              .withKingdomCharacteristics(KingdomCharacteristics.withSpeedLifespanAndAttack(random.nextDouble(),random.nextDouble(),random.nextDouble()))
              .andCastles(castles)
              .build()
      );
    }

    WarEnvironment.withKingdomsAndStrategy(WarStrategy.CAPTURE_THE_FLAG, kingdoms);
  }
}
