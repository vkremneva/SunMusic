package main.java.core;

import main.java.core.external.Sound;
import main.java.core.external.WavFile;
import main.java.core.math.AM;

import java.io.File;

/**
 * @author V.Kremneva
 */
public class SunRadio {
    private final int FRAMES = 4096; //amount of frames to read

    private WavFile wavInput; //input file
    private WavFile wavOutput; //output file

    private int bufferIndAmount; //amount of indexes in 'buffer' array needed to read data to
    //private int overlapIndAmount; //amount of indexes to work with overlap
    //private int offset; //amount of new frames read in each step of cycle
    //private int outputBufferIndAmount; //amount of indexes in am array to write
    //private long wholeIndAmount; //amount of pieces to read in whole file
    private String outputPath; //path to save output file

    /**
     * Open file and set some fields depending on it
     *
     * @param args is an array with paths to input and output files
     */
    private void openWavFile(String[] args) {
        if (args.length < 2) throw new IllegalArgumentException("As the arguments of the program " +
                "input path and output path are needed.");

        try {

            wavInput = WavFile.openWavFile(new File(args[0]));

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        int numChannels = wavInput.getNumChannels();
        bufferIndAmount = FRAMES * numChannels;
        //overlapIndAmount = OVERLAP * numChannels;
        //offset = FRAMES / OVERLAP;
        //outputBufferIndAmount = bufferIndAmount + overlapIndAmount;
        //wholeIndAmount = wavInput.getNumFrames() * numChannels;
        outputPath = args[1];
    }

    /*/**
     * Create empty output file like input file but stretched
     *
     * @param stretch in how many times we want to stretch
     * @return empty stretched wav file
     */
    /*private WavFile createStretchedOutputFile(int stretch) {
        try {
            return WavFile.newWavFile(new File(outputPath),
                    wavInput.getNumChannels(), wavInput.getNumFrames() * stretch,
                    wavInput.getValidBits(), wavInput.getSampleRate());

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return new WavFile();
    }*/

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

    /*/**
     * Create 'wavOutput' file as the sum of another wav files
     * @param outputPath initial path to another wav files
     * @param amount amount and in thi case step between wav files
     */
    /*private void assembleWavFiles(String outputPath, int amount) { //todo: universalize
        int framesAmount = 0;
        double[] buffer;
        WavFile temp;
        try {
            for (int i = 0; i < amount; i++) {
                temp = WavFile.openWavFile(new File(outputPath + i));
                framesAmount += temp.getFrameCounter();
                temp.close();
            }

            wavOutput = WavFile.newWavFile(new File(outputPath), wavInput.getNumChannels(),
                    framesAmount, wavInput.getValidBits(), wavInput.getSampleRate());

            buffer = new double[framesAmount];
            for (int i = 0; i < amount; i++) {
                temp = WavFile.openWavFile(new File(outputPath + i));
                temp.readFrames(buffer, (int)temp.getNumFrames()); //todo: correct for large frame amounts

                wavOutput.writeFrames(buffer, (int)temp.getNumFrames());

                temp.close();
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }*/

    /*private void adjustToneAccordingToLightLevel() {
        final int OVERLAP = 16; //coefficient of overlap
        WavFile stretchedOutputFile;
        double[] buffer = new double[bufferIndAmount * 2];
        double[] secondaryBuffer = new double[bufferIndAmount];
        double[] outputBuffer;
        double[] outputWindowFunction;
        int lightLevel, frames_read;
        DFTStraight transformable;
        ToneModulation toneModulation;

        transformable = new DFTStraight();
        toneModulation = new ToneModulation(bufferIndAmount / 2 + 1);
        //' / 2 + 1' due to cutting complex data in half when run DFT
        int count = 0;
        try {
            //read first 'FRAMES' frames
            frames_read = wavInput.readFrames(buffer, FRAMES);

            //get current level of light
            lightLevel = LightLevel.get();

            //stretching size of output buffer
            outputBufferIndAmount *= lightLevel;
            outputBuffer = new double[outputBufferIndAmount];

            if (frames_read != 0) {
                do {
                    //read next 'FRAMES' into buffer -- amplitudes(t)
                    frames_read = wavInput.readFrames(buffer, FRAMES);

                    for (int i = 0; i < overlapIndAmount; i++) {
                        //get next 'FRAMES' from buffer to work with
                        System.arraycopy(buffer, i * offset, secondaryBuffer, 0, bufferIndAmount);

                        //apply window filter. first and last 'offset' goes without filter
                        if (!(wavInput.getFrameCounter() == offset) && !(wavInput.getFrameCounter() == (wholeIndAmount - offset))) {
                            secondaryBuffer = Filter.apply(secondaryBuffer, Filter.BlackmanNuttall(secondaryBuffer.length));
                        }

                        //run Fourier transform
                        transformable.run(secondaryBuffer);

                        if (count == 0)
                            toneModulation.setPreviousData(transformable);
                        //stretch in 'light level' times
                        transformable.setData(toneModulation.stretch(lightLevel, transformable.getData()));

                        //run inverse Fourier transform
                        secondaryBuffer = DFTInverse.run(transformable.getData());

                        //apply output window function
                        outputWindowFunction = Filter.getOutputWindowFunc(Filter.BlackmanNuttall(secondaryBuffer.length));
                        secondaryBuffer = Filter.apply(secondaryBuffer, outputWindowFunction);

                        //add data to output buffer
                        for (int j = 0; j < bufferIndAmount; j++)
                            outputBuffer[j + i] += secondaryBuffer[j];

                        //move data for overlap
                        outputBuffer = move(outputBuffer, overlapIndAmount);
                    }

                    //TODO: correct parameters
                    //stretchedOutputFile = createStretchedOutputFile(outputPath + count, lightLevel);

                    //write data to new .waw file TODO: remove //
                    //stretchedOutputFile.writeFrames(outputBuffer, FRAMES);

                    //toneModulation.setPreviousData(transformable);
                    count++;
                } while (frames_read != 0);

                assembleWavFiles(outputPath, count);

                //todo: fasten velocity of playback
                //play(outputPath);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }*/

    private void adjustVolumeAccordingToLightLevel() {
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
     * Run the process of modulation and play the result
     */
    private void run() {
        //adjustToneAccordingToLightLevel();

        adjustVolumeAccordingToLightLevel();
    }

    /**
     * Close input and output files
     */
    private void closeFiles() {
        try {

            wavInput.close();
            wavOutput.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /*/**
     *  Move data to the left with filling with 0
     * @param data data to move
     * @param offset amount of steps to move
     * @return array with nulls in the end and 'data' values moved on offset
     */
    /*public static double[] move(double[] data, int offset) {
        double[] result = new double[data.length];

        System.arraycopy(data, offset, result, 0, data.length - offset);

        return result;
    }*/

    public static void main(String[] args) {
        SunRadio radio = new SunRadio();

        radio.openWavFile(args);

        radio.run();

        Sound.playSound(args[1]).join();

        radio.closeFiles();
    }
}
