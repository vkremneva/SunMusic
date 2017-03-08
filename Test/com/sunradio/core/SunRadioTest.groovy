package com.sunradio.core

import com.external.WavFile
import com.sunradio.math.AM
import com.sunradio.math.DFTInverse
import com.sunradio.math.DFTStraight
import com.sunradio.math.Filter

class SunRadioTest extends GroovyTestCase {
    final FRAMES = 100
    final EPS = 0.00001
    DFTStraight transformable = new DFTStraight()

    int numChannels, indAmount, framesRead
    double[] buffer, lightLevel, modulated, amplitudes


    void testRun() {
        try {
           // inputPath = "C:\\Users\\LEV\\IdeaProjects\\SunRadio\\launch.wav";
           // outputPath = "C:\\Users\\LEV\\IdeaProjects\\SunRadio\\new1.wav";

        } catch (Exception e) {
            System.err.println(e.toString())
        }
    }

    void testMove() {
        int offset = 3
        double[] before = [1, 2, 3, 4, 5, 6, 7]
        double[] afterExpected = [4, 5, 6, 7, 0, 0, 0]
        double[] afterActual

        afterActual = SunRadio.move(before, offset)

        for (int i = 0; i < before.length; i++)
            assertEquals(afterExpected[i], afterActual[i])
    }
}
