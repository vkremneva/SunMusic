package com.sunradio.math;

import com.external.Complex;
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
    private int size; //size od 'data' array
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

    public void setData(Complex[] newData) {
        data = newData;
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
        Complex c_buffer, exp_degree;
        data = new Complex[size];

        if (size == 0) throw new IllegalArgumentException("Size of a buffer cannot be < 1.\n size = " + size);

        maxAmplitude = Double.MIN_VALUE;
        minAmplitude = Double.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            data[i] = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                c_buffer = new Complex(buffer[j]);
                exp_degree = new Complex(0, -2 * PI * j * i / size);
                data[i] = data[i].add(c_buffer.mult(exp_degree.exp()));
            }

            realAmplitude = data[i].abs() / size;
            if (realAmplitude > maxAmplitude) maxAmplitude = realAmplitude;
            if (realAmplitude < minAmplitude) minAmplitude = realAmplitude;
        }

        isTransformed = true;
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
