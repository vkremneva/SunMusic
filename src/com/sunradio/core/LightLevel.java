package com.sunradio.core;

import java.util.Random;

/**
 * LightLevel describes how we get an array of data with current light level
 * @author V.Kremneva
 */
public class LightLevel {

    /**
     * Scale raw data to [0;1]
     *
     * @param rawLightLevel an array of the measured values of the light level
     * @return an array of data scaled to [0;1]
     * @throws IllegalArgumentException if an array of raw data is empty
     */
    private static double[] scale(int[] rawLightLevel) throws IllegalArgumentException {

        int size = rawLightLevel.length;
        if (size < 1) throw new IllegalArgumentException("Array of the raw light values cannot be empty");

        int maxLevel = rawLightLevel[0];
        int minLevel = rawLightLevel[0];
        for (int i = 1; i < size; i++) {
            if (rawLightLevel[i] > maxLevel) maxLevel = rawLightLevel[i];
            else if (rawLightLevel[i] < minLevel) minLevel = rawLightLevel[i];
        }

        double step = 1.0 / (double)(maxLevel - minLevel);

        double[] scaledLightLevel = new double[size];
        for (int i = 0; i < size; i++)
            scaledLightLevel[i] = (rawLightLevel[i] - minLevel) * step;

        return scaledLightLevel;
    }

    /**
     * Get light level from Arduino //TODO: specify
     *
     * @param amount of how many measurement of light level we want
     * @return scaled to [0;1] array of light values
     */
    static double[] getLightLevel(int amount) {

        //fake it 'till you make it
        Random r = new Random();
        int[] lightLevel = new int[amount];
        for (int i = 0; i < amount; i++)
            lightLevel[i] = r.nextInt();

        return scale(lightLevel);
    }
}
