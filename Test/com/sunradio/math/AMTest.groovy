package com.sunradio.math

import com.external.Complex
import com.sunradio.core.LightLevel

import static java.lang.Math.PI
import static java.lang.Math.sin

class AMTest extends GroovyTestCase {

    final ITERATIONS = 100
    final SPLIT = 80
    final EPS = 0.00001

    double[] input = new double[ITERATIONS]
    double[] amplitudes = new double[ITERATIONS]
    double[] lightLevels = new double[ITERATIONS]
    double[] modulation = new double[ITERATIONS]
    double[] reverseTransform = new double[ITERATIONS]
    Complex[] transformed = new Complex[ITERATIONS]
    Complex[] appliedModulation = new Complex[ITERATIONS]

    void test() {
        for (int i = 0; i < ITERATIONS; i++)
            input[i] = sin(2 * PI * i / SPLIT)

        transformed = DFTStraight.run(input)
        amplitudes = DFTStraight.getAmplitudes(transformed)
        lightLevels = LightLevel.getFakeLightLevel(ITERATIONS)
        modulation = AM.modulate(amplitudes, lightLevels, 1.0)
        appliedModulation = AM.applyModulationToComplex(transformed, modulation)
        reverseTransform = DFTInverse.run(appliedModulation)

        System.out.println("sinus                    lightLevels                    reverseTransform")
        for (int i = 0; i < ITERATIONS; i++)
            System.out.println(input[i]+"                    "+lightLevels[i]+"                   "+reverseTransform[i])

        double oldPhaseRatio, newPhaseRatio;
        for (int i = 0; i < ITERATIONS; i++) {
            oldPhaseRatio = transformed[i].im() / transformed[i].re()
            newPhaseRatio = appliedModulation[i].im() / appliedModulation[i].re()

            assert (oldPhaseRatio - newPhaseRatio) < EPS
        }
    }
}
