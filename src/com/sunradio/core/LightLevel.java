package com.sunradio.core;

import com.sunradio.math.Scale;
import static java.lang.Math.*;
import java.util.Random;

/**
 * LightLevel describes how we get an array of data with current light level
 * @author V.Kremneva
 */
public class LightLevel {

    public static int MAX = 1024;

    /**
     * Get randomised light level
     *
     * @param amount of how many measurement of light level we want
     * @return scaled to [0;1] array of light values
     */
    private static double[] getFakeLightLevel(int amount, Double minAmplitude, Double maxAmplitude) {

        //fake it 'till you make it
        Integer[] lightLevel = new Integer[amount];
        for (int i = 0; i < amount; i++)
            lightLevel[i] = (int)(sin(2 * PI * i / 100)*1000); //for smoothness


        return Scale.run(lightLevel, minAmplitude, maxAmplitude);
    }

    /**
     * Get light level from Arduino //TODO: specify
     *
     * @param amount of how many measurement of light level we want
     * @return scaled to [0;1] array of light values
     */
    static double[] getLightLevel(int amount, Double minAmplitude, Double maxAmplitude) {
        return getFakeLightLevel(amount, minAmplitude, maxAmplitude);
    }

    /**
     * Get light level
     * @param values an array to whom get light level
     * @return light level for 'values'
     */
    public static double[] getLightLevel(double[] values) {
        double maxVal, minVal;
        maxVal = Double.MIN_VALUE; minVal = Double.MAX_VALUE;
        for (double val: values) {
            if (maxVal > val) maxVal = val;
            if (maxVal < val) minVal = val;
        }

        return getLightLevel(values.length, minVal, maxVal);
    }

    private static int getAverageFakeLevel(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    static int getAverageLightLevel(double[] values) {
        int MAX_LIGHT_LEVEL = 10;
        int lightLevel = getAverageFakeLevel(MAX_LIGHT_LEVEL);

        //light level should be one of 2's degree
        lightLevel = (int)Math.pow(2, lightLevel);

        return lightLevel;
    }
}
