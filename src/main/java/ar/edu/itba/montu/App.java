package ar.edu.itba.montu;

import java.util.Arrays;
import java.util.List;

import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.kingdom.KingdomBuilder;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.scene.WarScene;
import ar.edu.itba.montu.war.scene.WarStrategy;
import ar.edu.itba.montu.war.utils.Coordinate;

/**
 * Hello world!
 *
 */
public class App {
	public static void main( String[] args ) {
		
		final List<Castle> castles = Arrays.asList(
			CastleBuilder
				.withName("Castle1", new Coordinate(10, 10))
				.withCastleCharacteristics(CastleCharacteristics.standardCharacteristics())
				.build(),
			CastleBuilder.defenseCastle("DefenseCastle", new Coordinate(20, 30))
		);
		
		final List<Kingdom> kingdoms = Arrays.asList(
				KingdomBuilder
					.withName("Kingdom1")
					.withKingdomCharacteristics(
						KingdomCharacteristics.withSpeedLifespanAndAttack(0.4, 0.6, 0.1)
					)
					.andCastles(castles)
					.build()
		);
		
		final WarScene warScene = WarScene.withKingdomsAndStrategy(WarStrategy.CAPTURE_THE_FLAG, kingdoms);
		
		final long time = 5000;
		
		warScene.start(time);
		
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
