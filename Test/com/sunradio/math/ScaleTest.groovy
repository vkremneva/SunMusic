package com.sunradio.math

class ScaleTest extends GroovyTestCase {
    void testRun() {
        final EPS = 0.00000001
        def arr = [5, 8, 7, 2] as Integer[]
        def from = 0.0
        def to = 1.0

        def outputExpected = [0.5, 1.0, 5 / 6, 0.0] as double[]

        double[] outputValue = Scale.run(arr, from, to)
        for (int i = 0; i < 4; i++)
            assertTrue((outputExpected[i] - outputValue[i]) < EPS)
    }
}
