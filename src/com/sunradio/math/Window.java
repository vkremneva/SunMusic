package com.sunradio.math;

import org.jetbrains.annotations.Contract;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

/**
 * Window functions
 * @author V.Kremneva
 */
public class Window {
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

    public static double[] apply(double[] amplitudes, double[] windowFunc) {
        return AM.modulate(amplitudes, windowFunc);
    }
}
