package com.sunradio.math;

import com.external.Complex;
import static java.lang.Math.PI;

/**
 * Straight discrete Fourier transform.
 * @author V.Kremneva
 */
public class DFTstraight {

    /** Run transform.
     *
     * @param f frequency in Hz
     * @param buffer an array of the values of the magnitudes
     * @return a magnitude value at frequency f
     */
    public static double run(int f, double[] buffer) throws IllegalArgumentException {

        int size = buffer.length;
        Complex magnitude, c_buffer, exp_degree;
        magnitude = new Complex(0, 0);

        if (f < 0) throw new IllegalArgumentException("Value of frequency cannot be < 0.\n f = " + f);
        if (size == 0) throw new IllegalArgumentException("Size of a buffer cannot be = 0.\n size = " + size);

        for (int k = 0; k < size; k++) {
            c_buffer = new Complex(buffer[k]);
            exp_degree = new Complex(0, -2*PI*k*f /size);
            magnitude = magnitude.add(c_buffer.mult(exp_degree.exp()));
        }

        return magnitude.abs();
    }
}
