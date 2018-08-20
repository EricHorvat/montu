package ar.edu.itba.montu.war;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Kingdom implements IKingdom {
    private String name;
    private List<Castle> castles;
    public int id;

    public Kingdom(String name) {
        this.id = (int)(Math.random()*256);
        this.name = name;
        this.castles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addCastle(Castle castle){
        castle.setKingdom(this);
        castles.add(castle);
    }

    public List<Castle> getCastles(){
        return castles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kingdom kingdom = (Kingdom) o;
        return Objects.equals(name, kingdom.name) &&
                Objects.equals(castles, kingdom.castles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
