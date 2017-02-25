package com.sunradio.math;

import com.external.Complex;

import java.util.Arrays;

import static java.lang.Math.PI;
import static java.lang.Math.max;

/**
 * Straight discrete Fourier transform.
 *
 * @author V.Kremneva
 */
public class DFTStraight {

    private Complex[] data; //Complex data
    private double maxAmplitude; //maximum value of real amplitude in 'data' array
    private double minAmplitude; //minimum value of real amplitude in 'data' array
    private int size; //size of 'data' array TODO: length
    private boolean isTransformed; //flag whether was 'data' transformed by DFT or not

    public DFTStraight() {
        maxAmplitude = 0.0;
        minAmplitude = 0.0;
        size = 0;
        isTransformed = false;
    }

    public double getMaxAmplitude() {
        return maxAmplitude;
    }

    public double getMinAmplitude() {
        return minAmplitude;
    }

    public boolean isTransformed() {
        return isTransformed;
    }

    public Complex[] getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    public void setData(Complex[] newData) {
        data = newData;
    }

    /**
     * Cut Complex array in two pieces.
     * We need this because the periods of the input data become split into "positive"
     * and "negative" frequency complex components. As a result, only half of array
     * contains data we are interested in and the rest of array is just a reflection with
     * opposite sign.
     *
     * @param dataToCut Complex data we want to be cut
     */
    private void cutDataInHalf(Complex[] dataToCut) {
        size = size / 2 + 1; //'+1' to include center value

        data = new Complex[size];
        data = Arrays.copyOf(dataToCut, size);
       /* for (int i = 0; i < size; i++) {
            data[i] = dataToCut[i];
        }*/
    }

    /** Run transform with search of max and min value of amplitude.
     *
     * @param buffer an array of the magnitudes
     * @return a Complex array which contains amplitude and phase values
     * @throws IllegalArgumentException if buffer is empty
     */
    public Complex[] run(double[] buffer) throws IllegalArgumentException {

        size = buffer.length;
        Double realAmplitude;
        Complex cBuffer, expDegree, tempData[];
        tempData = new Complex[size];


        if (size == 0) throw new IllegalArgumentException("Size of a buffer cannot be < 1.\n size = " + size);

        maxAmplitude = Double.MIN_VALUE;
        minAmplitude = Double.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            tempData[i] = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                cBuffer = new Complex(buffer[j]);
                expDegree = new Complex(0, -2 * PI * j * i / size);
                tempData[i] = tempData[i].add(cBuffer.mult(expDegree.exp()));
            }

            realAmplitude = tempData[i].abs() / size;
            if (realAmplitude > maxAmplitude) maxAmplitude = realAmplitude;
            if (realAmplitude < minAmplitude) minAmplitude = realAmplitude;
        }

        isTransformed = true;

        cutDataInHalf(tempData);

        return data;
    }

    /** Get a double amplitudes from some Complex array.
     *
     * @param complex a complex array
     * @return a double array which contains amplitude values
     */
    public static double[] getAmplitudes(Complex[] complex) {

        int size = complex.length;
        double[] amplitudes = new double[size];

        for (int i = 0; i < size; i++)
            amplitudes[i] = complex[i].abs() / size;

        return amplitudes;
    }

    /** Get a double amplitudes from 'data' array.
     *
     * @return a double array which contains amplitude values
     */
    public double[] getAmplitudes() {
        double[] amplitudes = new double[size];

        for (int i = 0; i < size; i++)
            amplitudes[i] = data[i].abs() / size;

        return amplitudes;
    }
}
