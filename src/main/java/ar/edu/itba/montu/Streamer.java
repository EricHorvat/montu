package ar.edu.itba.montu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import ar.edu.itba.montu.visual.ProcessingApplet;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.kingdom.Kingdom;

public class Streamer {

	private static final Logger logger = LogManager.getLogger(Streamer.class);
	private static Streamer instance;
	
	private WebSocket ws;
	
	private void init(String host, int port) {
		String url = String.format("ws://%s:%d/", host, port);
		try {
			logger.info("creating socket to url {}", url);
			
			ws = new WebSocketFactory().createSocket(url);
		} catch (IOException e) {
			ws = null;
			return;
		}
    
    try {
	    logger.info("connecting to socket url {}", url);
    	ws.connect();
    } catch (WebSocketException e) {
    	ws = null;
    	return;
    }
    ws.sendText("init");
	}
	
	public static Streamer currentStreamer() {
		if (instance == null) {
			instance = new Streamer();
			instance.init("127.0.0.1", 1337);
		}
		return instance;
	}
	
	public static Streamer currentStreamer(String host, int port) {
		if (instance == null) {
			instance = new Streamer();
			instance.init(host, port);
		}
		return instance;
	}
	
	public synchronized void streamNow(final long timeEllapsed) {
		
		if (ws == null) return;
		
		Gson gson = new GsonBuilder().create();
		Map<String, Object> transferObject = new HashMap<String, Object>();
		
		transferObject.put("time", timeEllapsed);
		transferObject.put("kingdoms", WarEnvironment.getInstance().kingdoms().stream().map(k -> {
			Map<String, Object> kingdom = new HashMap<String, Object>();
			kingdom.put("id", k.uid().toString());
			kingdom.put("name", k.name());
			kingdom.put("offense_capacity", k.characteristics().offenseCapacity());
			kingdom.put("warrior_speed", k.characteristics().warriorSpeed());
			kingdom.put("color", "#" + Integer.toHexString(k.color()));
			kingdom.put("friends", k.friends().stream().map(Kingdom::uid).collect(Collectors.toList()));
			kingdom.put("rivals", k.rivals().stream().map(Kingdom::uid).collect(Collectors.toList()));
			kingdom.put("enemies", k.enemies().stream().map(Kingdom::uid).collect(Collectors.toList()));
			kingdom.put("power", k.power());
			return kingdom;
		}).collect(Collectors.toList()));
    
		transferObject.put("castles", WarEnvironment.getInstance().kingdoms().stream().flatMap(k -> k.castles().stream()).map(c -> {
			Map<String, Object> castle = new HashMap<String, Object>();
			castle.put("id", c.uid().toString());
			castle.put("kingdom", c.kingdom().uid().toString());
			castle.put("country", c.kingdom().name());
			castle.put("name", c.name());
			castle.put("view_distance", c.characteristics().viewDistance());
			castle.put("health_points", c.characteristics().healthPoints());
			castle.put("max_health_points", c.characteristics().maxHealthPoints());
			castle.put("attack_distance", c.characteristics().attackDistance());
			castle.put("attack_harm", c.characteristics().attackHarm());
			castle.put("offense_capacity", c.characteristics().offenseCapacity());
			castle.put("resources", c.characteristics().resources());
			castle.put("max_resources", c.characteristics().maxResources());
			castle.put("x", c.location().X);
			castle.put("y", App.getConfiguration().getEnvironment().getSize() - c.location().Y);
			castle.put("warriors", c.warriors().size());
			castle.put("available_warriors", c.availableWarriors().size());
			castle.put("attackers", c.attackers().size());
			castle.put("available_attackers", c.availableAttackers().size());
			castle.put("defenders", c.defenders().size());
			castle.put("available_defenders", c.availableDefenders().size());
			return castle;
		}).collect(Collectors.toList()));

    ws.sendText(gson.toJson(transferObject));
	}
	
	public void streamOnTick(final long timeEllapsed) {
		if (ws == null) return;
		
		if (timeEllapsed % App.getConfiguration().getStream().getEvery() != 0) return;
		
		streamNow(timeEllapsed);
	}
	
}
