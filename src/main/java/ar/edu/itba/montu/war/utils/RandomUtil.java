package ar.edu.itba.montu.war.utils;

import java.util.Random;

public final class RandomUtil {

    private static Random rnd;

    public static Random getRandom(){
        return rnd;
    }

    public static double getNormalDistribution(double mean, double std) {
        return rnd.nextGaussian() * std + mean;
    }

    public static long getIntNormalDistribution(int mean, int std) {
        return Math.round(getNormalDistribution(mean,std));
    }

    public static double getExponentialDistribution(double lambda) {
        return Math.log(1 - rnd.nextDouble()) / -lambda;
    }

    public static long getIntExponentialDistribution(double lambda) {
        return Math.round(getExponentialDistribution(lambda));
    }

    public static void initializeWithSeed(long seed){
        if (rnd == null) {
            rnd = new Random(seed);
        }
    }

    private RandomUtil(){}
}
