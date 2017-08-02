package soundModulation.math;

import static java.lang.Math.*;

/**
 * Filter functions
 * @author V.Kremneva
 */
public class Filter {

    /**
     * Window function of Blackman-Nuttall
     *
     * @param size is the length of array to be windowed
     * @return an array of values of this window
     */
    public static double[] BlackmanNuttall(int size) {
        double[] result = new double[size];

        //constants from formula of Blackman–Nuttall window
        final double A0 = 0.3635819;
        final double A1 = 0.4891775;
        final double A2 = 0.1365995;
        final double A3 = 0.0106411;

        double firstCos, secondCos, thirdCos;
        for (int i = 0; i < size; i++) {
            firstCos = A1 * cos((2 * PI * i) / (size - 1));
            secondCos = A2 * cos((4 * PI * i) / (size - 1));
            thirdCos = A3 * cos((6 * PI * i) / (size - 1));

            result[i] = A0 - firstCos + secondCos - thirdCos;
        }

        return result;
    }

    /**
     * Window function of Blackman-Nuttall
     *
     * @param n point where value of this function is needed
     * @param size is the length of array to be windowed
     * @return value of this window function in 'n'
     */
    public static double BlackmanNuttall(int n, int size) {
        //constants from formula of Blackman–Nuttall window
        final double A0 = 0.3635819;
        final double A1 = 0.4891775;
        final double A2 = 0.1365995;
        final double A3 = 0.0106411;

        double firstCos, secondCos, thirdCos;
        firstCos = A1 * cos((2 * PI * n) / (size - 1));
        secondCos = A2 * cos((4 * PI * n) / (size - 1));
        thirdCos = A3 * cos((6 * PI * n) / (size - 1));

        return A0 - firstCos + secondCos - thirdCos;
    }

    /**
     * Apply window function to an array of values
     *
     * @param amplitudes an array to be windowed
     * @param windowFunc an array with values of window function
     * @return an array with transformed 'amplitudes' according to 'windowFunc'
     * @throws IllegalArgumentException if length of 'windowFunc' is less than length of 'amplitudes'
     */
    public static double[] apply(double[] amplitudes, double[] windowFunc)
            throws IllegalArgumentException {

        if (amplitudes.length > windowFunc.length) throw new IllegalArgumentException("Length of window function is too small\n" +
                "Size of amplitudes array: " + amplitudes.length + ". Size of windowFunc: " + windowFunc.length);

        double[] result = new double[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            result[i] = amplitudes[i] * windowFunc[i];
        }
        return result;
    }

    /**
     * Remove noise from amplitudes array
     *
     * @param toDenoise array to remove noise from
     * @return sort of clean array
     */
    public static double[] denoise(double[] toDenoise) {
        final double DENOISE_COEFF = 0.1; //got it by trial and error
        double maxAmplitude, eps;

        maxAmplitude = toDenoise[0];
        for (double val: toDenoise)
            if(val > maxAmplitude) maxAmplitude = val;

        eps = maxAmplitude * DENOISE_COEFF;
        for (int i = 0; i < toDenoise.length; i++)
            if (abs(toDenoise[i]) < eps) toDenoise[i] = 0.0;

        return toDenoise;
    }

    /**
     * Get function to filter output data before writing to file
     *
     * @param inputWindowFunc function used to filter input data
     * @return function to filter output data
     */
    public static double[] getOutputWindowFunc(double[] inputWindowFunc) {
        double[] outputWindowFunc = new double[inputWindowFunc.length];
        double sum = 0.0;

        for (double val: inputWindowFunc)
            sum += val * val;

        for (int i = 0; i < outputWindowFunc.length; i++)
            outputWindowFunc[i] = inputWindowFunc[i] / sum;

        return outputWindowFunc;
    }
}



