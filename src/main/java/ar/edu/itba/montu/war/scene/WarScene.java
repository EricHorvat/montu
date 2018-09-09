package ar.edu.itba.montu.war.scene;

import java.util.List;

import ar.edu.itba.montu.WarStrategy;
import ar.edu.itba.montu.interfaces.IKingdom;
import ar.edu.itba.montu.interfaces.IScene;

public class WarScene implements IScene {
	
	private final List<IKingdom> kingdoms;
	private final WarStrategy strategy;
	
	private WarScene(final WarStrategy strategy, final List<IKingdom> kingdoms) {
		kingdoms.forEach(kingdom -> {
			kingdom.attachToSceneIfNotAttached(this);
			kingdom.enforceStrategy(strategy);
		});
		this.kingdoms = kingdoms;
		this.strategy = strategy;
	}
	
	public static WarScene withKingdomsAndStrategy(final WarStrategy strategy, final List<IKingdom> kingdoms) {
		final WarScene warScene = new WarScene(strategy, kingdoms);
		return warScene;
	}
	
	public void start(final long time) {
		
	}
}
