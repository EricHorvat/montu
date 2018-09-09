package ar.edu.itba.montu.war.kingdom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ar.edu.itba.montu.WarStrategy;
import ar.edu.itba.montu.interfaces.ICastle;
import ar.edu.itba.montu.interfaces.IKingdom;
import ar.edu.itba.montu.interfaces.IScene;
import ar.edu.itba.montu.war.castle.Castle;

public class Kingdom implements IKingdom {
	
	private final String name;
	private final KingdomCharacteristics characteristics;
	
	private Optional<IScene> scene;
	private Optional<WarStrategy> strategy;
	
	private final List<ICastle> castles;

	/* package */Kingdom(final String name, final KingdomCharacteristics kingdomCharacteristics, final List<ICastle> castles) {
		this.name = name;
		this.characteristics = kingdomCharacteristics;
		this.castles = castles;
	}
	
	@Override
	public void attachToSceneIfNotAttached(final IScene _scene) {
		if (!this.scene.isPresent()) {
			this.scene = Optional.of(_scene);
		}
	}

	@Override
	public void enforceStrategy(final WarStrategy strategy) {
		this.strategy = Optional.of(strategy);
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
