package ar.edu.itba.montu;

import ar.edu.itba.montu.abstraction.WarAgent;

public class Test implements WarAgent {

//    private List<Kingdom> kingdomList;
//    private List<WarObject> objects;
//    private List<WarObject> objectsToAdd;
//
//    public Test() {
//        objects = new ArrayList<>();
//        objectsToAdd = new ArrayList<>();
//        this.generate();
//
//    }
//
//    private void generate(){
//        kingdomList = new ArrayList<>();
//        kingdomList.add(new Kingdom("K1"));
//        kingdomList.add(new Kingdom("K2"));
//        kingdomList.get(0).addCastle(new Castle(this));
//        kingdomList.get(1).addCastle(new Castle(this));
//        kingdomList.get(1).getCastles().get(0).setRival(kingdomList.get(0).getCastles().get(0));
//        kingdomList.get(0).getCastles().get(0).setRival(kingdomList.get(1).getCastles().get(0));
//        objects.add(kingdomList.get(0).getCastles().get(0));
//        objects.add(kingdomList.get(1).getCastles().get(0));
//    }
//
//    public Outfile outfile = new Outfile();
//
//    public void turn(int i){
//        if (i%1000 == 0)
//            System.out.println(i);
//        for (WarObject wo: objects) {
//            wo.move();
//        }
//        objects.addAll(objectsToAdd);
//        objectsToAdd.clear();
//        outfile.addInit(objects.size(), i);
//        for (WarObject wo: objects) {
//            outfile.addLine(wo.outfileFormat());
//            if (wo.getHp() <= 0){
//                objectsToAdd.add(wo);
//            }
//        }
//        for(WarObject wo : objectsToAdd){
//            objects.remove(wo);
//        }
//        objectsToAdd.clear();
//    }
//
//    public void newWarObject(WarObject w, Kingdom k){
//        w.setKingdom(k);
//        objectsToAdd.add(w);
//    }
}
