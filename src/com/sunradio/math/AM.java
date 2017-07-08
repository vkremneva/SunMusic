package com.sunradio.math;

import com.sunradio.core.LightLevel;

/**
 * Amplitude modulation
 * @author V.Kremneva
 */
public class AM {

    private static double COEFF = 0.65;

    /**
     * Modulate values according to conditions with the coefficient of modulation -0.65.
     * Assume 'values' as amplitude values therefore perform an Amplitude Modulation.
     *
     * @param values an array with amplitude values to modulate
     * @param conditions an array of modulation conditions for each amplitude value
     * @return a double array of modulated values of amplitudes
     * @throws IllegalArgumentException if amount of conditions is less than amount of values
     */
    public static double[] modulate(double[] values, double[] conditions) {
        return modulate(values, conditions, COEFF);
    }

    /**
     * Modulate values according to conditions with the coefficient of modulation 0.65.
     * Assume 'values' as amplitude values therefore perform an Amplitude Modulation.
     *
     * @param values an array with amplitude values to modulate
     * @param condition is a condition for modulation for every single amplitude value
     * @return a double array of modulated values of amplitudes
     * @throws IllegalArgumentException if amount of conditions is less than amount of values
     */
    public static double[] modulate(double[] values, double condition) {
        double[] conditionsInDouble = new double[values.length];
        double scaledCondition;

        scaledCondition = Scale.run(condition, -1, 1, LightLevel.MAX);

        for (int i = 0; i < values.length; i++)
            conditionsInDouble[i] = scaledCondition;

        return modulate(values, conditionsInDouble, COEFF);
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
    private static double[] modulate(double[] values, double[] conditions, double modulationCoeff)
            throws IllegalArgumentException {

        if (values.length > conditions.length) {
            throw new IllegalArgumentException("A size of an array with conditions " +
                    "should be equal or more than size of an array of values.\n" +
                    "Conditions size = " + conditions.length + ". Values size = " + values.length);
        }

        double[] modulated;
        modulated = new double[values.length];

        for (int i = 0; i < values.length; i++)
            modulated[i] = values[i] * (1 + modulationCoeff * conditions[i]);

        return modulated;
    }
}
