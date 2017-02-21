package com.sunradio.math

import static java.lang.Math.*

class DFTStraightTest extends GroovyTestCase {

    final EPS = 0.00001
    final ITERATIONS = 100
    final SPLIT = 100
    final NUMBER = 80.0

    // As a result, a periodic function will contain transformed peaks in not one,
    // but two places. This happens because the periods of the input data become split into "positive"
    // and "negative" frequency complex components.
    // http://mathworld.wolfram.com/DiscreteFourierTransform.html
    final int meaningful = ITERATIONS/2

    double[] buffer = new double[ITERATIONS]
    double[] result = new double[ITERATIONS]

    //f(t) = sin(t)
    public void testSin() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(2 * PI * i / SPLIT)

        result = DFTStraight.getAmplitudes(DFTStraight.run(buffer))

        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = NUMBER*sin(t)
    //Test passes with any number
    public void testConstMultSin() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = NUMBER * sin(2 * PI * i / SPLIT)

        result = DFTStraight.getAmplitudes(DFTStraight.run(buffer))

        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = sin(NUMBER*t)
    public void testSinMultConst() throws IllegalArgumentException{
        //TODO: fix those restrictions
        if ((NUMBER > SPLIT) || (NUMBER < SPLIT / 2))
            throw new IllegalArgumentException("Number should be less than SPLIT and more than SPLIT/2 due to sinus period");

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(NUMBER * 2 * PI * i / SPLIT)

        result = DFTStraight.getAmplitudes(DFTStraight.run(buffer))

        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = NUMBER*sin(t) + sin(Number*t)
    public void testTwoSinuses() {
        //TODO: fix those restrictions
        if ((NUMBER > SPLIT) || (NUMBER < SPLIT / 2))
            throw new IllegalArgumentException("Number should be less than SPLIT and more than SPLIT/2 due to sinus period");

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = NUMBER*sin(2 * PI * i / SPLIT) + sin(NUMBER * 2 * PI * i / SPLIT)

        result = DFTStraight.getAmplitudes(DFTStraight.run(buffer))

        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(2, amount)
    }
}

