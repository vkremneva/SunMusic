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
     * @param transformed an array of the magnitude value at frequencies
     * @return an array of magnitudes
     */
    public static double[] run(Complex[] transformed){

        int size = transformed.length;
        Complex c_transformed, exp_degree, magnitude;
        double[] result = new double[size];


        for (int i = 0; i < size; i++) {
            magnitude = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                //c_transformed = new Complex(transformed[i]);

                exp_degree = new Complex(0, 2 * PI * j * i / size);
                magnitude = magnitude.add(transformed[i].mult(exp_degree.exp()));
            }
            magnitude = magnitude.div(size);
            result[i] = magnitude.re();
        }

        return result;
    }
}
