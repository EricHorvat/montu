package ar.edu.itba.montu;

import java.util.Arrays;
import java.util.List;

import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.kingdom.KingdomBuilder;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.environment.WarStrategy;
import ar.edu.itba.montu.war.utils.Coordinate;
import ar.edu.itba.montu.war.utils.RandomUtil;

/**
 * Hello world!
 *
 */
public class App {
	public static void main( String[] args ) {

		/*TODO GET SEED*/
		RandomUtil.setRandom(1);

		final List<Castle> castles1 = Arrays.asList(
			CastleBuilder
				.withName("Castle1", new Coordinate(10, 10))
				.withCastleCharacteristics(CastleCharacteristics.standardCharacteristics())
				.build(),
			CastleBuilder.defenseCastle("DefenseCastle", new Coordinate(20, 30))
		);


		final List<Castle> castles2 = Arrays.asList(
			CastleBuilder
				.withName("Castle3", new Coordinate(40, 40))
				.withCastleCharacteristics(CastleCharacteristics.standardCharacteristics())
				.build()
		);
		
		final List<Kingdom> kingdoms = Arrays.asList(
				KingdomBuilder
					.withName("Kingdom1")
					.withKingdomCharacteristics(
						KingdomCharacteristics.withSpeedLifespanAndAttack(0.4, 0.6, 0.1)
					)
					.andCastles(castles1)
					.build(),
				KingdomBuilder
					.withName("Kingdom2")
					.withKingdomCharacteristics(
						KingdomCharacteristics.withSpeedLifespanAndAttack(0.4,0.6,0.1)
					)
					.andCastles(castles2)
					.build()
		);

		WarEnvironment.withKingdomsAndStrategy(WarStrategy.CAPTURE_THE_FLAG, kingdoms);
		final WarEnvironment warEnvironment = WarEnvironment.getInstance();

		final long time = 5000;
		
		warEnvironment.start(time);
		
//        System.out.println( "Hello World!" );
//        Test t = new Test();
//        for (int i = 0; i < 5000; i++)
//            t.turn(i);
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("./output/test.txt"));
//            writer.write(t.outfile.getValue());
//
//            writer.close();
//        }catch (IOException e){
//            e.getMessage();
//        }

    }
}
