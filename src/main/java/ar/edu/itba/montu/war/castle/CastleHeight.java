package ar.edu.itba.montu.war.castle;

public enum CastleHeight {
	SEA_LEVEL(0),
	GROUND_LEVEL(20),
	MEDIUM_LEVEL(100),
	HIGH_LEVEL(200);
	
	private double height;
	
	CastleHeight(final double height) {
		this.height = height;
	}
	
	public double height() {
		return height;
	}
}
