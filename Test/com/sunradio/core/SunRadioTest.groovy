package com.sunradio.core

import com.external.WavFile
import com.sunradio.math.AM
import com.sunradio.math.DFTInverse
import com.sunradio.math.DFTStraight
import com.sunradio.math.Filter

class SunRadioTest extends GroovyTestCase {

    void testRun() {
        def inputPath = "C:\\Users\\LEV\\IdeaProjects\\SunRadio\\launch.wav"
        def outputPath = "C:\\Users\\LEV\\IdeaProjects\\SunRadio\\new1.wav"

        String[] args = new String[2]
        args[0] = inputPath
        args[1] = outputPath

        SunRadio.main(args)
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
