package ar.edu.itba.montu.war.kingdom;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.montu.abstraction.WarAgent;
import ar.edu.itba.montu.interfaces.IScene;
import ar.edu.itba.montu.war.castle.Castle;
import ar.edu.itba.montu.war.scene.WarStrategy;

public class Kingdom implements WarAgent {
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	
	private Optional<WarStrategy> strategy;
	
	private KingdomStatus status = KingdomStatus.ALIVE;
	
	private final List<Castle> castles;

	/* package */protected Kingdom(final String name, final KingdomCharacteristics kingdomCharacteristics, final List<Castle> castles) {
		this.name = name;
		this.characteristics = kingdomCharacteristics;
		this.castles = castles;
	}

	public void enforceStrategy(final WarStrategy strategy) {
		this.strategy = Optional.of(strategy);
	}

	public KingdomStatus getCurrentStatus() {
		return status;
	}

	public void actOnTurn(final long timeEllapsed, final IScene scene, final List<Kingdom> otherKingdoms) {
		
	}
	
	
	
//    private List<Castle> castles;
//    public String id;
//
//    public Kingdom(String name) {
//        this.id = (int)(Math.random()*256);
//        this.name = name;
//        this.castles = new ArrayList<>();
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void addCastle(Castle castle){
//        castle.setKingdom(this);
//        castles.add(castle);
//    }
//
//    public List<Castle> getCastles(){
//        return castles;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Kingdom kingdom = (Kingdom) o;
//        return Objects.equals(name, kingdom.name) &&
//                Objects.equals(castles, kingdom.castles);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
}
