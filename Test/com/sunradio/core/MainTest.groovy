package com.sunradio.core

import com.external.WavFile
import com.sunradio.math.AM
import com.sunradio.math.DFTInverse
import com.sunradio.math.DFTStraight
import com.sunradio.math.Filter

class MainTest extends GroovyTestCase {
    final FRAMES = 100
    final EPS = 0.00001
    DFTStraight transformable = new DFTStraight()

    int numChannels, indAmount, framesRead
    double[] buffer, lightLevel, modulated, amplitudes


    void testMain() {
        try {
            WavFile wavInput = WavFile.openWavFile(new File("C:\\Users\\Merveilleuse\\IdeaProjects\\SunRadio\\launch.wav"))

            numChannels = wavInput.getNumChannels()
            indAmount = FRAMES * numChannels
            buffer = new double[indAmount]
            def flag = false
            while (!flag) {
                //get initial amplitudes
                framesRead = wavInput.readFrames(buffer, FRAMES)
                for (int i = 0; i < indAmount; i++)
                    if (buffer[i] != 0) flag = true

                if (flag) {
                    //Filter
                    buffer = Filter.apply(buffer, Filter.BlackmanNuttall(indAmount))

                    //Fourier transform
                    transformable.run(buffer)
                    amplitudes = transformable.getAmplitudes()
                    int size = amplitudes.length
                    //for (int i = 0; i < size; i++) System.out.println(i)
                    //System.out.println("\nfourier amplitudes")
                    //for (int i = 0; i < size; i++) System.out.println(amplitudes[i])

                    //get light level
                    lightLevel = LightLevel.getLightLevel(size,
                            transformable.getMinAmplitude(), transformable.getMaxAmplitude())
                    //System.out.println("\nlight level")
                    //for (int i = 0; i < size; i++) System.out.println(lightLevel[i])

                    //Use amplitude modulation
                    modulated = AM.modulate(amplitudes, lightLevel)
                    //System.out.println("\nmodulated")
                    //for (int i = 0; i < size; i++) System.out.println(modulated[i])

                    //Apply modulation
                    transformable.setData(AM.applyModulationToComplex(transformable.getData(), modulated))
                    amplitudes = transformable.getAmplitudes()
                    for (int i = 0; i < size; i++)
                        assert (modulated[i] - amplitudes[i]) < EPS
                    //System.out.println("\namplitudes")
                    //for (int i = 0; i < size; i++) System.out.println(amplitudes[i])

                    //Inverse transform
                    buffer = DFTInverse.run(transformable.getData())
                    //System.out.println("\ninverse transformes amplitudes")
                    //for (int i = 0; i < size; i++) System.out.println(buffer[i])

                    //Denoise
                    buffer = Filter.denoise(buffer)
                    //Apply window function
                    //buffer = Filter.apply(buffer, Filter.BlackmanNuttall(indAmount))
                    //System.out.println("\ndenoised")
                    //for (int i = 0; i < size; i++) System.out.println(buffer[i])

                }
            }

            wavInput.close()
        } catch (Exception e) {
            System.err.println(e.toString())
        }
    }

    void testMove() {
        int offset = 3
        double[] before = [1, 2, 3, 4, 5, 6, 7]
        double[] afterExpected = [4, 5, 6, 7, 0, 0, 0]
        double[] afterActual

        afterActual = Main.move(before, offset)

        for (int i = 0; i < before.length; i++)
            assertEquals(afterExpected[i], afterActual[i])
    }
}
