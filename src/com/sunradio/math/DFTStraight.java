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
     * @return an array of magnitudes values at frequencies
     */
    public static double[] run(double[] buffer) throws IllegalArgumentException {

        int size = buffer.length;
        Complex c_buffer, exp_degree, magnitude;
        double[] result = new double[size];

        if (size == 0) throw new IllegalArgumentException("Size of a buffer cannot be < 1.\n size = " + size);

        for (int i = 0; i < size; i++) {
            magnitude = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                c_buffer = new Complex(buffer[j]);
                exp_degree = new Complex(0, -2 * PI * j * i / size);
                magnitude = magnitude.add(c_buffer.mult(exp_degree.exp()));
            }

            result[i] = magnitude.abs() / size;
        }

        return result;
    }
}
