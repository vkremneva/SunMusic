package com.sunradio.math

import static java.lang.Math.PI
import static java.lang.Math.sin

class DFTInverseTest extends GroovyTestCase {
    final EPS = 0.00001
    final ITERATIONS = 100
    final SPLIT = 100
    final NUMBER = 80.0

    double[] income = new double[ITERATIONS]
    DFTStraight outcome_straight = new DFTStraight()
    double[] outcome_inverse = new double[ITERATIONS]

    // f(t) = sin(t)
    public void testSin() {
        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            income[i] = sin(2 * PI * i / SPLIT)

        outcome_straight.run(income)
        outcome_inverse = DFTInverse.run(outcome_straight.getData())

        double difference;
        int amount = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            difference = outcome_inverse[i] - income[i]
            if (difference > EPS) amount++
        }

        assertEquals(0, amount)
    }

    //f(t) = NUMBER*sin(t) + sin(Number*t)
    public void testTwoSin() throws IllegalArgumentException {
        if ((NUMBER > SPLIT) || (NUMBER < SPLIT / 2))
            throw new IllegalArgumentException("Number should be less than SPLIT and more than SPLIT/2 due to sinus period")

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            income[i] = NUMBER*sin(2 * PI * i / SPLIT) + sin(NUMBER * 2 * PI * i / SPLIT)

        outcome_straight.run(income)
        outcome_inverse = DFTInverse.run(outcome_straight.getData())

        double difference;
        int amount = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            difference = outcome_inverse[i] - income[i]
            if (difference > EPS) amount++
        }

        assertEquals(0, amount)
    }
}
