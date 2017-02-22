package com.sunradio.core;

import com.sunradio.math.Scale;

import java.util.Random;

/**
 * LightLevel describes how we get an array of data with current light level
 * @author V.Kremneva
 */
public class LightLevel {

    /**
     * Get randomised light level
     *
     * @param amount of how many measurement of light level we want
     * @return scaled to [0;1] array of light values
     */
    public static double[] getFakeLightLevel(int amount) {

        //fake it 'till you make it
        Random r = new Random();
        Integer[] lightLevel = new Integer[amount];
        for (int i = 0; i < amount; i++)
            lightLevel[i] = r.nextInt();

        return Scale.run(lightLevel, 0, 1);
    }

    /**
     * Get light level from Arduino //TODO: specify
     *
     * @param amount of how many measurement of light level we want
     * @return scaled to [0;1] array of light values
     */
    static double[] getLightLevel(int amount) {
        return getFakeLightLevel(amount);
    }
}
