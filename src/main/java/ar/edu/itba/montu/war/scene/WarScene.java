package ar.edu.itba.montu.war.scene;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import ar.edu.itba.montu.interfaces.IKingdom;
import ar.edu.itba.montu.interfaces.IScene;

public class WarScene implements IScene {
	
	private final List<IKingdom> kingdoms;
	private final WarStrategy strategy;
	
	private WarScene(final WarStrategy strategy, final List<IKingdom> kingdoms) {
		kingdoms.forEach(kingdom -> {
			kingdom.enforceStrategy(strategy);
		});
		this.kingdoms = kingdoms;
		this.strategy = strategy;
	}
	
	public static WarScene withKingdomsAndStrategy(final WarStrategy strategy, final List<IKingdom> kingdoms) {
		final WarScene warScene = new WarScene(strategy, kingdoms);
		return warScene;
	}
	
	private static List<IKingdom> shuffledKingdoms(final List<IKingdom> kingdoms) {
		final List<Integer> indexList = IntStream.range(0, kingdoms.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList);
		return indexList.stream().map(index -> kingdoms.get(index)).collect(Collectors.toList());
	}
	
	public void start(final long time) {
		LongStream.rangeClosed(1, time).forEach(timeEllapsed -> {
			final List<IKingdom> shuffledKingdoms = shuffledKingdoms(kingdoms);
			shuffledKingdoms.forEach(kingdom -> {
				final List<IKingdom> otherKingdoms = this.kingdoms.stream().filter(k -> !k.equals(kingdom)).collect(Collectors.toList());
				kingdom.actOnTurn(timeEllapsed, this, otherKingdoms);
			});
		});
	}
}
