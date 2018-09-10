package ar.edu.itba.montu.war.scene;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import ar.edu.itba.montu.interfaces.IScene;
import ar.edu.itba.montu.war.kingdom.Kingdom;
import ar.edu.itba.montu.war.utils.RandomUtil;

public class WarScene implements IScene {
	
	private final List<Kingdom> kingdoms;
	private final WarStrategy strategy;
	
	private WarScene(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		kingdoms.forEach(kingdom -> {
			kingdom.enforceStrategy(strategy);
		});
		this.kingdoms = kingdoms;
		this.strategy = strategy;
	}
	
	public static WarScene withKingdomsAndStrategy(final WarStrategy strategy, final List<Kingdom> kingdoms) {
		final WarScene warScene = new WarScene(strategy, kingdoms);
		return warScene;
	}
	
	private static List<Kingdom> shuffledKingdoms(final List<Kingdom> kingdoms) {
		final List<Integer> indexList = IntStream.range(0, kingdoms.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(indexList, RandomUtil.getRandom());
		return indexList.stream().map(index -> kingdoms.get(index)).collect(Collectors.toList());
	}
	
	public void start(final long time) {
		LongStream.rangeClosed(1, time).forEach(timeEllapsed -> {
			final List<Kingdom> shuffledKingdoms = shuffledKingdoms(kingdoms);
			shuffledKingdoms.forEach(kingdom -> {
				final List<Kingdom> otherKingdoms = this.kingdoms.stream().filter(k -> !k.equals(kingdom)).collect(Collectors.toList());
				kingdom.actOnTurn(timeEllapsed, this, otherKingdoms);
			});
		});
	}
}
