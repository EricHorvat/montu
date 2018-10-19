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
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import ar.edu.itba.montu.war.environment.WarEnvironment;

public class Streamer {

	private static final long SEND_EVERY = 60; 
	private static final Logger logger = LogManager.getLogger(Streamer.class);
	private static Streamer instance;
	
	private WebSocket ws;
	
	private void init() {
		try {
			ws = new WebSocketFactory().createSocket("ws://localhost:1337/");
		} catch (IOException e) {
			ws = null;
			return;
		}
    
    try {
    	ws.connect();
    } catch (WebSocketException e) {
    	ws = null;
    	return;
    }
	}
	
	public static Streamer currentStreamer() {
		if (instance == null) {
			instance = new Streamer();
			instance.init();
		}
		return instance;
	}
	
	public void streamOnTick(final long timeEllapsed) {
		if (ws == null) return;
		
		if (timeEllapsed % SEND_EVERY != 0) return;
		
		Gson gson = new GsonBuilder().create();
		Map<String, Object> transferObject = new HashMap<String, Object>();
		
		transferObject.put("time", timeEllapsed);
		transferObject.put("kingdoms", WarEnvironment.getInstance().kingdoms().stream().map(k -> {
			Map<String, Object> kingdom = new HashMap<String, Object>();
			kingdom.put("id", k.uid().toString());
			kingdom.put("name", k.name());
			kingdom.put("offense_capcity", k.characteristics().offenseCapacity());
			kingdom.put("warrior_speed", k.characteristics().warriorSpeed());
			return kingdom;
		}).collect(Collectors.toList()));
    
		transferObject.put("castles", WarEnvironment.getInstance().kingdoms().stream().flatMap(k -> k.castles().stream()).map(c -> {
			Map<String, Object> castle = new HashMap<String, Object>();
			castle.put("id", c.uid().toString());
			castle.put("kingdom", c.kingdom().uid().toString());
			castle.put("name", c.name());
			castle.put("view_distance", c.characteristics().viewDistance());
			castle.put("health_points", c.characteristics().healthPoints());
			castle.put("max_health_points", c.characteristics().maxHealthPoints());
			castle.put("attack_distance", c.characteristics().attackDistance());
			castle.put("attack_harm", c.characteristics().attackHarm());
			castle.put("offense_capacity", c.characteristics().offenseCapacity());
			castle.put("gas", c.characteristics().gas());
			castle.put("max_gas", c.characteristics().maxGas());
			return castle;
		}).collect(Collectors.toList()));

    ws.sendText(gson.toJson(transferObject));
	}
	
}
