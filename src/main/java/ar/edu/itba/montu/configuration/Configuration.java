package ar.edu.itba.montu.configuration;

import java.util.List;

public final class Configuration {
	
	public static double CASTLE_POWER_COEF = 100;
	public static double HP_POWER_COEF = 100;
	public static double WARRIOR_POWER_COEF = 100;
	public static double FRIEND_PERCENTAGE = 0.2;
	public static double RIVAL_PERCENTAGE = 0.6;
	public static int UPDATE_NEGOTATION_TICKS = 100;
	public static int FRIENDSHIP_TICKS = 100;
	public static double RIVAL_PRIORITY_COEF = 2.5;
	public static double FRIEND_PRIORITY_COEF = 0.4;
	public static boolean FRIEND_WEAKERS = true;
	public static final int RESOURCES_PER_MINUTE = 1;
	
	
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
		private double viewDistance;
		private double attackDistance;
		private int healthPoints;
		private int resources;
		private double spawnProbability;
		public double getViewDistance() {
			return viewDistance;
		}
		public double getAttackDistance() {
			return attackDistance;
		}
		public int getHealthPoints() {
			return healthPoints;
		}
		public int getResources() {
			return resources;
		}
		public void setViewDistance(double viewDistance) {
			this.viewDistance = viewDistance;
		}
		public void setAttackDistance(double attackDistance) {
			this.attackDistance = attackDistance;
		}
		public void setHealthPoints(int healthPoints) {
			this.healthPoints = healthPoints;
		}
		public void setResources(int resources) {
			this.resources = resources;
		}
		public double getSpawnProbability() {
			return spawnProbability;
		}
		public void setSpawnProbability(double spawnProbability) {
			this.spawnProbability = spawnProbability;
		}
		@Override
		public String toString() {
			return "CastleCharacteristics [viewDistance=" + viewDistance + ", attackDistance=" + attackDistance
					+ ", healthPoints=" + healthPoints + "]";
		}
	}
	
	public static final class Coordinate {
		private double lat;
		private double lng;
		public double getLat() {
			return lat;
		}
		public double getLng() {
			return lng;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
		@Override
		public String toString() {
			return "[lat=" + lat + ", lng=" + lng + "]";
		}
	}
	
	public static final class CastleConfiguration {
		private String name;
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
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "CastleConfiguration [characteristics=" + characteristics + ", location=" + location + "]";
		}
	}
	
	public static final class KingdomConfiguration {
		private String name;
		private int offenseCapacity;
		private double warriorSpeed;
		private List<CastleConfiguration> castles;
		private int color;
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
		public double getWarriorSpeed() {
			return warriorSpeed;
		}
		public void setWarriorSpeed(double warriorSpeed) {
			this.warriorSpeed = warriorSpeed;
		}
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
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
	
	private double maxPriority;
	private double minPriorityDistance;
	private double superWarriorProbability;
	private double healthOffensiveRollCoefficient;
	
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
	public double getMaxPriority() {
		return maxPriority;
	}
	public void setMaxPriority(double maxPriority) {
		this.maxPriority = maxPriority;
	}
	public double getMinPriorityDistance() {
		return minPriorityDistance;
	}
	public void setMinPriorityDistance(double minPriorityDistance) {
		this.minPriorityDistance = minPriorityDistance;
	}
	public double getSuperWarriorProbability() {
		return superWarriorProbability;
	}
	public void setSuperWarriorProbability(double superWarriorProbability) {
		this.superWarriorProbability = superWarriorProbability;
	}
	public double getHealthOffensiveRollCoefficient() {
		return healthOffensiveRollCoefficient;
	}
	public void setHealthOffensiveRollCoefficient(double healthOffensiveRollCoefficient) {
		this.healthOffensiveRollCoefficient = healthOffensiveRollCoefficient;
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
