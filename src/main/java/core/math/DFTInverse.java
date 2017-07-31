package main.java.core.math;

import main.java.core.external.Complex;

import static java.lang.Math.PI;

/**
 * Inverse discrete Fourier transform.
 * @author V.Kremneva
 */
public class DFTInverse {

    /**
     * Restore frequency complex components from just one half.
     *
     * @param dataToRestore array of data to restore
     * @return full spectrum Complex array
     */
    private static Complex[] restoreData(Complex[] dataToRestore) {

        int oldSize = dataToRestore.length;
        int newSize = oldSize * 2 - 2;
        Complex[] result = new Complex[newSize];

        System.arraycopy(dataToRestore, 0, result, 0, oldSize);

        for (int i = oldSize; i < newSize; i++)
            result[i] = dataToRestore[newSize - i].conj();

        return result;
    }

    /** Run transform.
     *
     * @param transformed a Complex array which contains amplitude and phase values
     * @return an array of amplitudes
     */
    public static double[] run(Complex[] transformed){

        Complex[] data;
        data = restoreData(transformed);

        int size = data.length;
        Complex exp_degree, magnitude;
        double[] result = new double[size];

        for (int i = 0; i < size; i++) {
            magnitude = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                exp_degree = new Complex(0, 2 * PI * j * i / size);
                magnitude = magnitude.add(data[j].mult(exp_degree.exp()));
            }

            result[i] = magnitude.re() / size;
        }

        return result;
    }
}
