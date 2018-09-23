package ar.edu.itba.montu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.castle.CastleBuilder;
import ar.edu.itba.montu.war.castle.CastleCharacteristics;
import ar.edu.itba.montu.war.environment.WarEnviromentGenerator;
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

		//TODO ADD ARGUMENTS
    WarEnviromentGenerator.generate();

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
