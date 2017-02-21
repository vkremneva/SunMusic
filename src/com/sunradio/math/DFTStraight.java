package com.sunradio.math;

import com.external.Complex;
import static java.lang.Math.PI;

/**
 * Straight discrete Fourier transform.
 * @author V.Kremneva
 */
public class DFTStraight {

    /** Run transform.
     *
     * @param buffer an array of the magnitudes
     * @return a Complex array which contains amplitude and phase values
     * @throws IllegalArgumentException if buffer is empty
     */
    public static Complex[] run(double[] buffer) throws IllegalArgumentException {

        int size = buffer.length;
        Complex c_buffer, exp_degree;
        Complex[] magnitude = new Complex[size];

        if (size == 0) throw new IllegalArgumentException("Size of a buffer cannot be < 1.\n size = " + size);

        for (int i = 0; i < size; i++) {
            magnitude[i] = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                c_buffer = new Complex(buffer[j]);
                exp_degree = new Complex(0, -2 * PI * j * i / size);
                magnitude[i] = magnitude[i].add(c_buffer.mult(exp_degree.exp()));
            }
        }
        return magnitude;
    }

    /** Get a double amplitudes.
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
}
