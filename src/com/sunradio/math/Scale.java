package com.sunradio.math;

/**
 * Helps to scale data
 * @author V.Kremneva
 */
public class Scale {

    /**
     * Scale numeric data
     *
     * @param arr is an array of data to scale
     * @param from is the lower bound of scaling
     * @param to is the upper bound of scaling
     * @return scaled array of data
     * @throws IllegalArgumentException if the data array is empty
     */
    public static<T extends java.lang.Number> double[] run(T[] arr, double from, double to)
            throws IllegalArgumentException {

        int size = arr.length;
        if (size < 1) throw new IllegalArgumentException("Array of the values cannot be empty");

        double maxLevel = arr[0].doubleValue();
        double minLevel = arr[0].doubleValue();
        double current;
        for (int i = 1; i < size; i++) {
            current = arr[i].doubleValue();

            if (current > maxLevel) maxLevel = current;
            else if (current < minLevel) minLevel = current;
        }

        double step = (to - from) / (maxLevel - minLevel);

        double [] scaledLightLevel = new double [size];
        for (int i = 0; i < size; i++)
            scaledLightLevel[i] = (arr[i].doubleValue() - minLevel) * step + from;

        return scaledLightLevel;
    }
}
