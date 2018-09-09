package ar.edu.itba.montu;

import java.util.List;

import ar.edu.itba.montu.interfaces.IKingdom;

public class WarScene {
	
	private final List<IKingdom> kingdoms;
	private final WarStrategy strategy;
	
	private WarScene(final WarStrategy strategy, final List<IKingdom> kingdoms) {
		this.kingdoms = kingdoms;
		this.strategy = strategy;
	}
	
	public static WarScene withKingdomsAndStrategy(final WarStrategy strategy, final List<IKingdom> kingdoms) {
		final WarScene warScene = new WarScene(strategy, kingdoms);
		return warScene;
	}
}
