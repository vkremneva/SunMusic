package com.sunradio.math;

/**
 * Amplitude modulation
 * @author V.Kremneva
 */
public class AM {

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
        return modulate(values, conditions, -0.65);
    }

    /**
     * Modulate values according to conditions with the coefficient of modulation -0.65.
     * Assume 'values' as amplitude values therefore perform an Amplitude Modulation.
     *
     * @param values an array with amplitude values to modulate
     * @param condition is a condition for modulation for every single amplitude value
     * @return a double array of modulated values of amplitudes
     * @throws IllegalArgumentException if amount of conditions is less than amount of values
     */
    public static double[] modulate(double[] values, double condition) {
        double[] conditions;
        Double[] conditionsInDouble = new Double[values.length];

        for (int i = 0; i < values.length; i++)
            conditionsInDouble[i] = condition;

        conditions = Scale.run(conditionsInDouble, 0, 1);

        return modulate(values, conditions, -0.65);
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

        double maxCond;
        maxCond = conditions[0];
        for (int i = 1; i < values.length; i++)
            if (conditions[i] > maxCond) maxCond = conditions[i];

        for (int i = 0; i < values.length; i++) {
            modulated[i] = values[i] * (1 + modulationCoeff * conditions[i] / Math.abs(maxCond));
            if (values[i] != 0)
                System.out.println(values[i] + " " + modulated[i]);
        }

        return modulated;
    }
}
