package com.sunradio.math

import static java.lang.Math.PI
import static java.lang.Math.sin
import com.external.Complex;

class DFTInverseTest extends GroovyTestCase {
    final EPS = 0.00001
    final ITERATIONS = 100
    final SPLIT = 100

    double[] income = new double[ITERATIONS]
    Complex[] outcome_straight = new Complex[ITERATIONS]
    double[] outcome_inverse = new double[ITERATIONS]

    void testSin() {
        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            income[i] = sin(2 * PI * i / SPLIT)

        outcome_straight = DFTStraight.run(income)
        outcome_inverse = DFTInverse.run(outcome_straight)

        double difference;
        int amount = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            difference = outcome_inverse[i] - income[i]
            if (difference > EPS) amount++
        }

        assertEquals(0, amount)
    }
}
