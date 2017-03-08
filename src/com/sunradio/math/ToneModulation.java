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
    private boolean currentIsSet; //indicates whether current data was set

    public ToneModulation() {
        previousIsSet = false;
        currentIsSet = false;
    }

    public ToneModulation(int size) {
        length = size;

        previousPhases = new double[size];
        previousAmplitudes = new double[size];

        previousIsSet = true;
        currentIsSet = false;
    }

    public void setPreviousData(DFTStraight prevData) {
        previousAmplitudes = prevData.getAmplitudes();
        previousPhases = prevData.getPhases();

        previousIsSet = true;
    }

    public void setCurrentData(DFTStraight currentData) {
        currentAmplitudes = currentData.getAmplitudes();
        currentPhases = currentData.getPhases();

        currentIsSet = true;
    }

    /**
     * Stretch data in N times
     *
     * @param coefficient in how many times to stretch
     * @return stretched data
     */
    public Complex[] stretch(int coefficient) throws ToneModulationException {
        if (!previousIsSet || !currentIsSet)
            throw new ToneModulationException("Previous and current data must be set");

        int newSize = length * coefficient;
        Complex[] result = new Complex[newSize];
        double[] newPhases = new double[newSize];
        double[] newAmplitudes = new double[newSize];
        double[] velocity = new double[length];

        for (int i = 0; i < length; i++)
            velocity[i] = currentPhases[i] - previousPhases[i];

        for (int i = 0; i < length; i++)
            for (int j = 0; j < coefficient; j++)
                newPhases[i + j] = currentPhases[i] + velocity[i];

        result = DFTStraight.applyNewPhases(newPhases, result);

        for (int i = 0; i < length; i++)
            for (int j = 0; j < coefficient; j++)
                newAmplitudes[i + j] = Interpolation.linearByX(0, previousAmplitudes[i], coefficient, currentAmplitudes[i], j);

        result = DFTStraight.applyNewAmplitudes(newAmplitudes, result);

        return result;
    }
}