package com.sunradio.math;

import com.external.Complex;
import static java.lang.Math.PI;

/**
 * Inverse discrete Fourier transform.
 * @author V.Kremneva
 */
public class DFTInverse {
    /** Run transform.
     *
     * @param transformed a Complex array which contains amplitude and phase values
     * @return an array of amplitudes
     */
    public static double[] run(Complex[] transformed){

        int size = transformed.length;
        Complex exp_degree, magnitude;
        double[] result = new double[size];

        for (int i = 0; i < size; i++) {
            magnitude = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                exp_degree = new Complex(0, 2 * PI * j * i / size);
                magnitude = magnitude.add(transformed[j].mult(exp_degree.exp()));
            }

            result[i] = magnitude.re() / size;
        }

        return result;
    }
}
