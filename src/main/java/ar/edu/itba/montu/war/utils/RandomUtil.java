package ar.edu.itba.montu.war.utils;

import java.util.Random;

public final class RandomUtil {

    private static Random rnd;

    public static Random getRandom(){
        return rnd;
    }

    public static void setRandom(long seed){
        if (rnd == null) {
            rnd = new Random(seed);
        }
    }

    private RandomUtil(){}
}
