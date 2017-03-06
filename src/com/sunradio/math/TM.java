package com.sunradio.math;

import com.external.Complex;

/**
 * Tone modulation.
 *
 * @author V.Kremneva
 */
public class TM {
    private static double[] getVelocity(double[] previousPhases, double[] currentPhases) {
        double[] result = new double[previousPhases.length];

        for (int i = 0; i < previousPhases.length; i++)
            result[i] = currentPhases[i] - previousPhases[i];


        return result;
    }

    public static DFTStraight modulate(double[] prevPhases, Complex[] data, double coefficient) {
        DFTStraight result = new DFTStraight();
        double[] currentPhases = DFTStraight.getPhases(data);
        result.setData(data);
        //todo: get velocity
        double[] velocity;
        velocity = getVelocity(prevPhases, currentPhases);

        //todo: add to prev phase
        double[] newPhases = new double[prevPhases.length];
        for (int i = 0; i < prevPhases.length; i++)
            newPhases[i] = currentPhases[i] + velocity[i];
        result.applyNewPhases(newPhases);

        //todo: interpolate amplitudes
        double[] newAmplitudes = new double[prevPhases.length];
        result.applyNewAmplitudes(newAmplitudes);

        return result;
    }
}
