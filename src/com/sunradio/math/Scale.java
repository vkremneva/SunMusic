package com.sunradio.math;

/**
 * Helps to scale data
 * @author V.Kremneva
 */
public class Scale {

    /**
     * Scale number on specific range
     *
     * @param number is number to scale
     * @param from is the lower bound of scaling
     * @param to is the upper bound of scaling
     * @param maxLevel is an amount of estimated steps
     * @return scaled array of data
     */
    static double run(double number, double from, double to, double maxLevel)
            throws IllegalArgumentException {

        if (from > to) {
            double temp = from;
            from = to;
            to = temp;
        }

        double step = (to - from) / maxLevel;

        return number * step + from;
    }

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
        if (from > to) {
            double temp = from;
            from = to; to = temp;
        }

        double[] scaledLightLevel = new double[size];
        double maxLevel = arr[0].doubleValue();
        double minLevel = arr[0].doubleValue();
        double current;
        for (int i = 1; i < size; i++) {
            current = arr[i].doubleValue();

            if (current > maxLevel) maxLevel = current;
            else if (current < minLevel) minLevel = current;
        }

        if (maxLevel == minLevel) {
            minLevel = 0;
            maxLevel = 1024;
        }

        double step = (to - from) / (maxLevel - minLevel);

        for (int i = 0; i < size; i++)
            scaledLightLevel[i] = (arr[i].doubleValue() - minLevel) * step + from;


        return scaledLightLevel;
    }
}
