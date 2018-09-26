package ar.edu.itba.montu;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.edu.itba.montu.war.environment.WarEnviromentGenerator;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.utils.RandomUtil;

/**
 * Hello world!
 *
 */
public class App {
	
	private static final Logger logger = LogManager.getLogger(App.class);
	
	public static void main(String[] args) throws Exception {

		final long seed = 1;
		
		logger.info("Initializing RandomUtil with seed={}", seed);
		
		/*TODO GET SEED*/
		RandomUtil.initializeWithSeed(6);

		logger.info("Generating war environment");
		
		//TODO ADD ARGUMENTS
    WarEnviromentGenerator.generate();

		final WarEnvironment warEnvironment = WarEnvironment.getInstance();

		if (warEnvironment == null) {
			throw new Exception("Run WarEnviromentGenerator.generate before getting an instance of WarEnvironment");
		}
		
		final long time = 5000;
		
		logger.info("Starting war environment for {} minutes", time);
		
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
