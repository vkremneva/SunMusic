package com.sunradio.math

import static java.lang.Math.*

class DFTStraightTest extends GroovyTestCase {

    final double EPS = 0.00001
    final int ITERATIONS = 100
    final int SPLIT = 100
    final int NUMBER = 80

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

        result = DFTStraight.run(buffer)

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

        result = DFTStraight.run(buffer)

        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = sin(NUMBER*t)
    //Number should be less than SPLIT and more than SPLIT/2
    //because of the sinus period
    public void testSinMultConst() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(NUMBER * 2 * PI * i / SPLIT)

        result = DFTStraight.run(buffer)

        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = NUMBER*sin(t) + sin(Number*t)
    public void testTwoSinuses() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = NUMBER*sin(2 * PI * i / SPLIT) + sin(NUMBER * 2 * PI * i / SPLIT)

        result = DFTStraight.run(buffer)

        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(2, amount)
    }
}

