package com.sunradio.core

class LightLevelTest extends GroovyTestCase {
    void testGetLightLevel() {

    }

    void testScale() {
        final EPS = 0.00000001
        int[] input = [5, 8, 7, 2]
        double[] outputExpected = new double[4]
        outputExpected[0] = 0.5
        outputExpected[1] = 1.0
        outputExpected[2] = 5 / 6 as double
        outputExpected[3] = 0.0

        double[] outputValue = LightLevel.scale(input)
        for (int i = 0; i < 4; i++)
            assertTrue((outputExpected[i] - outputValue[i]) < EPS)
    }
}
