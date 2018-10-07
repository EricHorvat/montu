package ar.edu.itba.montu.visual;

import ar.edu.itba.montu.war.kingdom.Kingdom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KingdomColorGetter {

  static Map<Kingdom,Integer> kingdomOrder = new HashMap<>();
  static int last = 0;

  public static void putKingdoms(List<Kingdom> kingdoms){
    kingdoms.forEach(kingdom -> kingdomOrder.put(kingdom, last++));
  }

  static int getHueValue(Kingdom kingdom){
    return kingdomOrder.get(kingdom) * 360 / kingdomOrder.size();
  }

}
