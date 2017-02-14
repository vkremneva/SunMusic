package com.sunradio.math;

import com.external.Complex;
import static java.lang.Math.PI;

/**
 * Discrete Fourier transform.
 * @author V.Kremneva
 */
public class DFT {

    /** Fourier Transform.
     *
     * @param f frequency in Hz
     * @param buffer an array of the values of the amplitudes
     * @param size size of the buffer
     * @return an amplitude value at frequency f
     */
    public double apply_DFT(int f, double[] buffer, int size){
        Complex amplitude, c_buffer, exp_degree;
        amplitude = new Complex(0, 0);

        for (int k = 0; k < size; k++) {
            c_buffer = new Complex(buffer[k]);
            exp_degree = new Complex(0, -2*PI*k* f /size);
            amplitude = amplitude.add(c_buffer.mult(exp_degree.exp()));
        }

        return amplitude.re();
    }
}
