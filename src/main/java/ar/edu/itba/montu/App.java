package ar.edu.itba.montu;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import ar.edu.itba.montu.configuration.Configuration;
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

		if (args.length != 1) {
			System.out.println("Usage: <file.yml>");
			return;
		}
		
		Yaml yaml = new Yaml();  
    try (InputStream in = Files.newInputStream(Paths.get(args[0]))) {
    	final Configuration config = yaml.loadAs(in, Configuration.class);
    	System.out.println(config.toString());
    }
		
		final long seed = 78;//78;//1;6;
		
		logger.info("Initializing RandomUtil with seed={}", seed);
		
		/*TODO GET SEED*/
		RandomUtil.initializeWithSeed(seed);

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
	}
}
