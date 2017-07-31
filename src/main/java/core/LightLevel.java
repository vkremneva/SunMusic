package main.java.core;

import java.util.Random;

/**
 * LightLevel describes how we get an array of data with current light level
 * @author V.Kremneva
 */
public class LightLevel {

    public static int MAX = 10;

    private static int getFakeLevel(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    static int get() {
        return getFakeLevel(MAX);
    }

}
