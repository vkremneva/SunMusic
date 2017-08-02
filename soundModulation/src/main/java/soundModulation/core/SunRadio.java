package soundModulation.core;

import soundModulation.external.WavFile;
import soundModulation.math.AM;

import java.io.File;

/**
 * @author V.Kremneva
 */
class SunRadio {
    private final int FRAMES = 4096; //amount of frames to read

    private WavFile wavInput; //input file
    private WavFile wavOutput; //output file

    private int bufferIndAmount; //amount of indexes in 'buffer' array needed to read data to
    private String outputPath; //path to save output file

    WavFile getWavOutput() {
        return wavOutput;
    }

    /**
     * Open file and set some fields depending on it
     *
     * @param name is a name of file to open
     */
    void openWavFile(String name) {

        try {
            wavInput = WavFile.openWavFile(new File(name));

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        int numChannels = wavInput.getNumChannels();
        bufferIndAmount = FRAMES * numChannels;
        outputPath = name + 1; //todo: place in resources
    }

    /**
     * Creates output file to play with the same parameters
     * as the input file.
     *
     * @return wav file
     */
    private WavFile createOutputFile() {
        try {
            return WavFile.newWavFile(new File(outputPath),
                    wavInput.getNumChannels(), wavInput.getNumFrames(),
                    wavInput.getValidBits(), wavInput.getSampleRate());

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return new WavFile();
    }

    /**
     * Modulate volume according to light level
     */
    void adjustVolumeAccordingToLightLevel() {
        double[] buffer = new double[bufferIndAmount * 2];
        int lightLevel, framesRead;

        wavOutput = createOutputFile();

        try {
            final int SPARSENESS = 4; //how much FRAMES we modulate with single light level
            final int SLEEP = 500; //how often we get value of light level

            //we got smooth values of light level when sleep in
            //'LightLevelThread' equals 'SLEEP' * 'SPARSENESS' mills
            LightLevelThread threadLightLevel = new LightLevelThread(SLEEP * SPARSENESS);
            threadLightLevel.start();

            framesRead = wavInput.readFrames(buffer, FRAMES);

            lightLevel = threadLightLevel.getLightLevel();

            if (framesRead != 0) {
                for (int i = 0; framesRead != 0; i++) {
                    if ((i % SPARSENESS) == 0)
                        lightLevel = threadLightLevel.getLightLevel();

                    System.out.println(lightLevel);

                    framesRead = wavInput.readFrames(buffer, FRAMES);

                    buffer = AM.modulate(buffer, lightLevel);

                    wavOutput.writeFrames(buffer, FRAMES);

                    Thread.sleep(SLEEP);
                }
            }

            threadLightLevel.interrupt();
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Close input and output files
     */
    void closeFile() {
        try {

            wavInput.close();
            wavOutput.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
