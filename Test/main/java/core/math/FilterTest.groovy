package main.java.core.math

import static java.lang.Math.PI
import static java.lang.Math.sin

class FilterTest extends GroovyTestCase {
    final ITERATIONS = 100
    final SPLIT = 80
    final EPS = 0.00001

    double[] buffer = new double[ITERATIONS]
    double[] winFunc = new double[ITERATIONS]
    double[] applied = new double[ITERATIONS]

    void testApply() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(2 * PI * i / SPLIT)

        winFunc = Filter.BlackmanNuttall(ITERATIONS)
        applied = Filter.apply(buffer, winFunc)

        for (int i = 0; i < ITERATIONS; i++)
            assert applied[i] - (buffer[i] * winFunc[i]) < EPS
    }

    void testGetOutputFilter() {
        double[] inputWindowFunc = Filter.BlackmanNuttall(ITERATIONS)
        double[] outputWindowFunc = Filter.getOutputWindowFunc(inputWindowFunc)
        double sum = 0.0

        for (int i = 0; i < ITERATIONS; i++)
            sum += inputWindowFunc[i]*outputWindowFunc[i]

        assertEquals(1.0, sum)
    }
}
