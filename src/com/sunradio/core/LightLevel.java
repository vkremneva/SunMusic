package com.sunradio.core;

import com.sunradio.math.Scale;

import static java.lang.Math.*;

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
    public static double[] getFakeLightLevel(int amount, Double minAmplitude, Double maxAmplitude) {

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
}
