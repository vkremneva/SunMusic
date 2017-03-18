package com.sunradio.math;

import com.external.Complex;

/**
 * Tone modulation.
 *
 * @author V.Kremneva
 */
public class ToneModulation {
    private double[] previousPhases; //values of previous phases
    private double[] previousAmplitudes; //values of previous amplitudes
    private double[] currentPhases; //values of current phases
    private double[] currentAmplitudes; //values of current amplitudes

    private int length; //length of all of the arrays

    private boolean previousIsSet; //indicates whether previous data was set

    public ToneModulation() {
        previousIsSet = false;
    }

    public ToneModulation(int size) {
        length = size;

        previousPhases = new double[size];
        previousAmplitudes = new double[size];

        previousIsSet = true;
    }

    public void setPreviousData(DFTStraight prevData) {
        previousAmplitudes = prevData.getAmplitudes();
        previousPhases = prevData.getPhases();

        previousIsSet = true;
    }

    /**
     * Stretch data in N times
     *
     * @param coefficient in how many times to stretch
     * @param data is Complex data to stretch
     * @return stretched data
     * @throws ToneModulationException if previous data wasn't set
     */
    public Complex[] stretch(int coefficient, Complex[] data) throws ToneModulationException {
        if (!previousIsSet)
            throw new ToneModulationException("Previous data must be set");

        if (coefficient == 1)
            return data;

        Complex[] result = new Complex[length * coefficient];
        double[] newPhases = new double[length * coefficient];
        double[] newAmplitudes = new double[length * coefficient];
        double[] velocity = new double[length];
        int i;

        currentAmplitudes = DFTStraight.getAmplitudes(data);
        currentPhases = DFTStraight.getPhases(data);

        for (i = 0; i < length; i++)
            velocity[i] = currentPhases[i] - previousPhases[i];

        for (i = 0; i < length; i++)
            for (int j = 0; j < coefficient; j++)
                newPhases[i * coefficient + j] = currentPhases[i] + velocity[i];

        for (i = 0; i < length; i++)
            for (int j = 0; j < coefficient; j++)
                newAmplitudes[i * coefficient + j] = Interpolation.linearByX(0, previousAmplitudes[i],
                        coefficient, currentAmplitudes[i], j);

        result = DFTStraight.applyNewAmplitudes(newAmplitudes, result);
        result = DFTStraight.applyNewPhases(newPhases, result);

        return result;
    }
}