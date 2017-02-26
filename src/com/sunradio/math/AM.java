package com.sunradio.math;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import com.external.Complex;

/**
 * Amplitude modulation
 * @author V.Kremneva
 */
public class AM {

    /**
     * Modulate values according to conditions with the coefficient of modulation 1.0.
     * Assume 'values' as amplitude values therefore perform an Amplitude Modulation.
     *
     * @param values an array with amplitude values to modulate
     * @param conditions an array of modulation conditions for each amplitude value
     * @return a double array of modulated values of amplitudes
     * @throws IllegalArgumentException if amount of conditions is less than amount of values
     */
    public static double[] modulate(double[] values, double[] conditions) {
        return modulate(values, conditions, 1.0);
    }

    /**
     * Modulate values according to conditions.
     * Assume 'values' as amplitude values therefore perform an Amplitude Modulation.
     *
     * @param values an array with amplitude values to modulate
     * @param conditions an array of modulation conditions for each amplitude value
     * @param modulationCoeff modulation coefficient which picks up by trial and error
     * @return a double array of modulated values of amplitudes
     * @throws IllegalArgumentException if amount of conditions is less than amount of values
     */
    public static double[] modulate(double[] values, double[] conditions, double modulationCoeff)
            throws IllegalArgumentException {

        int sizeCond = conditions.length;
        if (values.length > sizeCond) {
            throw new IllegalArgumentException("A size of an array with conditions " +
                    "should be equal or more than size of an array of values.\n" +
                    "Conditions size = " + sizeCond + ". Values size = " + values.length);
        }

        double[] modulated;
        modulated = new double[sizeCond];

        double maxCond;
        maxCond = conditions[0];
        for (int i = 1; i < sizeCond; i++)
            if (conditions[i] > maxCond) maxCond = conditions[i];

        for (int i = 0; i < sizeCond; i++)
            modulated[i] = values[i] * (1 + modulationCoeff * conditions[i] / Math.abs(maxCond));

        return modulated;
    }

    /**
     * Apply modulation to an array of complex amplitudes
     *
     * @param transformed an array of complex amplitudes -- result of DFT
     * @param modulated an array of modulated values of amplitude we want to apply
     * @return Complex array of modulated values of complex amplitudes
     */
    public static Complex[] applyModulationToComplex(Complex[] transformed, double[] modulated) {
        int size = transformed.length;
        if (size > modulated.length) {
            throw new IllegalArgumentException("Length of the 'transformed' should be equal or" +
                    "less than the length of 'modulated'\n" +
                    "transformed.length = " + size + " modulated.length = " + modulated.length);
        }

        Complex[] applied;
        applied = new Complex[size];

        double a, b;
        for (int i = 0; i < size; i++) {

            b = pow(transformed[i].im(), 2.0) * pow(modulated[i], 2.0) * pow(size, 2.0);
            b = b / (pow(transformed[i].re(), 2.0) + pow(transformed[i].im(), 2.0));
            b = sqrt(b);

            a = sqrt(pow(modulated[i], 2.0) * pow(size, 2.0) - pow(b, 2.0));

            //we get 'a' from equation for real amplitude and 'b' from my condition:
            //i want the real phase be the same so i need that ('a' / 'b') from 'transformed'
            //be the same as ('a' / 'b') from 'applied'

            applied[i] = new Complex(a, b);
        }

        return applied;
    }
}
