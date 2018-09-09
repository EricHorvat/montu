package ar.edu.itba.montu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ar.edu.itba.montu.interfaces.IKingdom;
import ar.edu.itba.montu.war.kingdom.KingdomBuilder;
import ar.edu.itba.montu.war.kingdom.KingdomCharacteristics;
import ar.edu.itba.montu.war.scene.WarScene;

/**
 * Hello world!
 *
 */
public class App {
	public static void main( String[] args ) {
		
		
		final List<IKingdom> kingdoms = Arrays.asList(
				KingdomBuilder
					.withName("Kingdom1")
					.withKingdomCharacteristics(
						KingdomCharacteristics.withSpeedLifespanAndAttack(0.4, 0.6, 0.1)
					)
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
