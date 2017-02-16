package com.sunradio.math

import static java.lang.Math.*

class DFTStraightTest extends GroovyTestCase {

    final double EPS = 0.00001
    final int ITERATIONS = 100

    // As a result of the above relation, a periodic function will contain transformed peaks in not one,
    // but two places. This happens because the periods of the input data become split into "positive"
    // and "negative" frequency complex components.
    // http://mathworld.wolfram.com/DiscreteFourierTransform.html
    final int meaningful = ITERATIONS/2

    //f(t) = sin(t)
    public void testSin() {

        double[] buffer = new double[ITERATIONS]
        //Split the sinus period in ITERATIONS pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++) buffer[i] = sin(2 * PI * i / ITERATIONS)

        double[] result = new double[ITERATIONS]
        result = DFTStraight.run(buffer)


        int amount = 0
        for (int i = 0; i < meaningful; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }
}
