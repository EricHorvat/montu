package ar.edu.itba.montu;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neovisionaries.ws.client.HostnameUnverifiedException;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import ar.edu.itba.montu.configuration.Configuration;
import ar.edu.itba.montu.visual.ProcessingApplet;
import ar.edu.itba.montu.war.environment.WarEnviromentGenerator;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.utils.RandomUtil;

/**
 * Hello world!
 *
 */
public class App {
	
	private static final Logger logger = LogManager.getLogger(App.class);
	
	private static Configuration configuration;
	
	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.out.println("Usage: <file.yml>");
			return;
		}
		
		final Yaml yaml = new Yaml();
		final Configuration config;
    try (InputStream in = Files.newInputStream(Paths.get(args[0]))) {
    	config = yaml.loadAs(in, Configuration.class);
    	logger.info("Loaded configuration from {}", args[0]);
    }
    
    // initialize streamer
    Streamer.currentStreamer();
		
//		final long seed = 1;//78;//1;6;
//		logger.info("Initializing RandomUtil with seed={}", seed);
		logger.info("Initializing RandomUtil with seed={}", config.getEnvironment().getSeed());
//		RandomUtil.initializeWithSeed(seed);
		RandomUtil.initializeWithSeed(config.getEnvironment().getSeed());

		logger.info("Generating war environment");
		
    WarEnviromentGenerator.generateWithConfiguration(config);
    
    setConfiguration(config);

		final WarEnvironment warEnvironment = WarEnvironment.getInstance();

		if (warEnvironment == null) {
			throw new Exception("Run WarEnviromentGenerator.generate before getting an instance of WarEnvironment");
		}
		
		logger.info("Initializing visual environment");
		ProcessingApplet.init(config.getViewport().getWidth(), config.getViewport().getHeight(), config.getEnvironment().getSize());
		logger.info("Starting visual environment");
		String[] processingArgs = {"Montu"};
		ProcessingApplet.runSketch(processingArgs, ProcessingApplet.instance());
		
		logger.info("Starting war environment for {} minutes", config.getEnvironment().getTime());
		warEnvironment.start(config.getEnvironment().getTime());
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	private static void setConfiguration(Configuration configuration) {
		App.configuration = configuration;
	}
}
