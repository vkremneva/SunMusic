package com.sunradio.math

import static java.lang.Math.PI
import static java.lang.Math.sin

class WindowTest extends GroovyTestCase {
    final ITERATIONS = 100
    final SPLIT = 80
    final EPS = 0.00001

    double[] buffer = new double[ITERATIONS]
    double[] winFunc = new double[ITERATIONS]
    double[] applied = new double[ITERATIONS]

    /*i used it for visual estimation of amplitude changes
      i thought it would be inappropriate to have println's in test
      so i added useless assert */
    void testApply() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(2 * PI * i / SPLIT)

        winFunc = Window.BlackmanNuttall(ITERATIONS)
        applied = Window.apply(buffer, winFunc)

        for (int i = 0; i < ITERATIONS; i++)
            assert applied[i] - (buffer[i] * winFunc[i]) < EPS
    }
}
