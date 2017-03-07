package com.sunradio.math;

import com.external.Complex;

/**
 * Tone modulation.
 *
 * @author V.Kremneva
 */
public class TM {

    /**
     *  Stretch data in N times
     * @param previousData data from previous iteration
     * @param currentData data from current iteration
     * @param coefficient in how many times to stretch
     * @return stretched data
     */
    public static DFTStraight stretch(Complex[] previousData, Complex[] currentData, int coefficient) {

        int size = previousData.length;
        int newSize = size * coefficient;
        DFTStraight result = new DFTStraight(newSize);
        double[] currentPhases = DFTStraight.getPhases(currentData);
        double[] previousPhases = DFTStraight.getPhases(previousData);
        double[] currentAmplitude = DFTStraight.getAmplitudes(currentData);
        double[] previousAmplitude = DFTStraight.getAmplitudes(previousData);
        double[] newPhases = new double[newSize];
        double[] newAmplitudes = new double[newSize];
        double[] velocity = new double[size];

        for (int i = 0; i < size; i++)
            velocity[i] = currentPhases[i] - previousPhases[i];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < coefficient; j++)
                newPhases[i + j] = currentPhases[i] + velocity[i];

        result.applyNewPhases(newPhases);

        for (int i = 0; i < size; i++)
            for (int j = 0; j < coefficient; j++)
                newAmplitudes[i + j] = Interpolation.linearByX(0, previousAmplitude[i], coefficient, currentAmplitude[i], j);

        result.applyNewAmplitudes(newAmplitudes);

        return result;
    }
}