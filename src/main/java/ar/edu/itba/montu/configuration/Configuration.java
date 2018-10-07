package ar.edu.itba.montu.configuration;

import java.util.List;

public final class Configuration {

	public static final class EnvironmentConfiguration {
		private int size;
		private long time;
		private long seed;
		public int getSize() {
			return size;
		}
		public long getTime() {
			return time;
		}
		public long getSeed() {
			return seed;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public void setSeed(long seed) {
			this.seed = seed;
		}
		@Override
		public String toString() {
			return "EnvironmentConfiguration [size=" + size + ", time=" + time + ", seed=" + seed + "]";
		}
	}
	
	public static final class CastleCharacteristicConfiguration {
		private int viewDistance;
		private int attackDistance;
		private int healthPoints;
		private int attack;
		public int getViewDistance() {
			return viewDistance;
		}
		public int getAttackDistance() {
			return attackDistance;
		}
		public int getHealthPoints() {
			return healthPoints;
		}
		public int getAttack() {
			return attack;
		}
		public void setViewDistance(int viewDistance) {
			this.viewDistance = viewDistance;
		}
		public void setAttackDistance(int attackDistance) {
			this.attackDistance = attackDistance;
		}
		public void setHealthPoints(int healthPoints) {
			this.healthPoints = healthPoints;
		}
		public void setAttack(int attack) {
			this.attack = attack;
		}
		@Override
		public String toString() {
			return "CastleCharacteristics [viewDistance=" + viewDistance + ", attackDistance=" + attackDistance
					+ ", healthPoints=" + healthPoints + ", attack=" + attack + "]";
		}
	}
	
	public static final class Coordinate {
		private int lat;
		private int lng;
		public int getLat() {
			return lat;
		}
		public int getLng() {
			return lng;
		}
		public void setLat(int lat) {
			this.lat = lat;
		}
		public void setLng(int lng) {
			this.lng = lng;
		}
		@Override
		public String toString() {
			return "[lat=" + lat + ", lng=" + lng + "]";
		}
	}
	
	public static final class CastleConfiguration {
		private CastleCharacteristicConfiguration characteristics;
		private Coordinate location;
		public CastleCharacteristicConfiguration getCharacteristics() {
			return characteristics;
		}
		public void setCharacteristics(CastleCharacteristicConfiguration characteristics) {
			this.characteristics = characteristics;
		}
		public Coordinate getLocation() {
			return location;
		}
		public void setLocation(Coordinate location) {
			this.location = location;
		}
		@Override
		public String toString() {
			return "CastleConfiguration [characteristics=" + characteristics + ", location=" + location + "]";
		}
	}
	
	public static final class KingdomConfiguration {
		public String name;
		private int offenseCapacity;
		private List<CastleConfiguration> castles;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getOffenseCapacity() {
			return offenseCapacity;
		}
		public void setOffenseCapacity(int offenseCapacity) {
			this.offenseCapacity = offenseCapacity;
		}
		public List<CastleConfiguration> getCastles() {
			return castles;
		}
		public void setCastles(List<CastleConfiguration> castles) {
			this.castles = castles;
		}
		@Override
		public String toString() {
			return new StringBuilder()
					.append(String.format("name: %s\n", name))
					.append(String.format("offenseCapacity: %d", offenseCapacity))
					.append(String.format("castles: %s\n", castles))
					.toString();
		}
	}
	
	public final static class ViewportConfiguration {
		private int width;
		private int height;
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		@Override
		public String toString() {
			return "ViewportConfiguration [width=" + width + ", height=" + height + "]";
		}
	}
	
	
	
	private EnvironmentConfiguration environment;
	private ViewportConfiguration viewport;
	private List<KingdomConfiguration> kingdoms;
	public EnvironmentConfiguration getEnvironment() {
		return environment;
	}
	public void setEnvironment(EnvironmentConfiguration environment) {
		this.environment = environment;
	}
	
	public List<KingdomConfiguration> getKingdoms() {
		return kingdoms;
	}
	public void setKingdoms(List<KingdomConfiguration> kingdoms) {
		this.kingdoms = kingdoms;
	}
	public ViewportConfiguration getViewport() {
		return viewport;
	}
	public void setViewport(ViewportConfiguration viewport) {
		this.viewport = viewport;
	}
	@Override
	public String toString() {
		return new StringBuilder()
				.append(String.format("Environment: %s\n", environment))
				.append(String.format("Viewport: %s\n", viewport))
				.append(String.format("Kingdoms: %s\n", kingdoms))
				.toString();
	}
}
