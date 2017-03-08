package com.sunradio.core;

import com.external.WavFile;
import com.sunradio.math.DFTInverse;
import com.sunradio.math.DFTStraight;
import com.sunradio.math.Filter;
import com.sunradio.math.ToneModulation;

import java.io.File;
/*
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
*/

/**
 * @author V.Kremneva
 */
public class SunRadio {
    private final int FRAMES = 2048; //amount of frames to read
    private final int OVERLAP = 16; //coefficient of overlap

    private WavFile wavInput; //input file
    private WavFile wavOutput; //output file

    private int bufferIndAmount; //amount of indexes in 'buffer' array needed to read data to
    private int overlapIndAmount; //amount of indexes to work with overlap
    private int offset; //amount of new frames read in each step of cycle
    private int outputBufferIndAmount; //amount of indexes in am array to write
    private long wholeIndAmount; //amount of pieces to read in whole file

    /**
     * Open file and set some fields depending on it
     *
     * @param inputPath path to file to open
     */
    private void openWavFile(String inputPath) {
        try {

            wavInput = WavFile.openWavFile(new File(inputPath));

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        int numChannels = wavInput.getNumChannels();
        bufferIndAmount = FRAMES * numChannels;
        overlapIndAmount = OVERLAP * numChannels;
        offset = FRAMES / OVERLAP;
        outputBufferIndAmount = bufferIndAmount + overlapIndAmount;
        wholeIndAmount = wavInput.getNumFrames() * numChannels;
    }

    /**
     * Create empty output file like input file but stretched
     *
     * @param outputPath path where to create output file
     */
    private void createStretchedOutputFile(String outputPath, int stretch) {
        try {

            wavOutput = WavFile.newWavFile(new File(outputPath),
                    wavInput.getNumChannels(), wavInput.getNumFrames() * stretch,
                    wavInput.getValidBits(), wavInput.getSampleRate());

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void run() {
        double[] buffer = new double[bufferIndAmount];
        double[] outputBuffer = new double[outputBufferIndAmount];
        double[] outputWindowFunction;
        int lightLevel, frames_read;
        DFTStraight transformable;
        ToneModulation toneModulation;

        transformable = new DFTStraight();
        toneModulation = new ToneModulation(bufferIndAmount);

        int counter = 0;
        try {
            do {
                //read next 'FRAMES' into buffer -- amplitudes(t)
                frames_read = wavInput.readFramesWithOverlap(buffer, FRAMES, OVERLAP);

                //get current level of light
                lightLevel = LightLevel.getAverageLightLevel(buffer);

                for (int i = 0; i < overlapIndAmount; i++) {
                    //apply window filter. first and last 'offset' goes without filter
                    if (!(wavInput.getFrameCounter() == offset) && !(wavInput.getFrameCounter() == (wholeIndAmount - offset))) {
                        buffer = Filter.apply(buffer, Filter.BlackmanNuttall(bufferIndAmount));
                    }

                    //run Fourier transform
                    transformable.run(buffer);

                    //stretch in 'light level' times
                    toneModulation.setCurrentData(transformable);
                    transformable.setData(toneModulation.stretch(lightLevel));

                    //run inverse Fourier transform
                    buffer = DFTInverse.run(transformable.getData());

                    //apply output window function
                    outputWindowFunction = Filter.getOutputWindowFunc(Filter.BlackmanNuttall(bufferIndAmount));
                    buffer = Filter.apply(buffer, outputWindowFunction);

                    for (int j = 0; j < bufferIndAmount; j++)
                        outputBuffer[j + i] += buffer[j];

                    //read next 'FRAMES' into buffer -- amplitudes(t)
                    frames_read = wavInput.readFramesWithOverlap(buffer, FRAMES, OVERLAP);
                }

                //write data to new .waw file
                wavOutput.writeFrames(outputBuffer, FRAMES);

                //move data for overlap
                outputBuffer = move(outputBuffer, overlapIndAmount);

                toneModulation.setPreviousData(transformable);
                counter++;
            } while (frames_read != 0);

            //todo: adjust volume
            //todo: fasten velocity of playback
            //play(outputPath);


        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void closeFiles() {
        try {

            wavInput.close();
            wavOutput.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     *  Move data to the left with filling with 0
     * @param data data to move
     * @param offset amount of steps to move
     * @return array with nulls in the end and 'data' values moved on offset
     */
    public static double[] move(double[] data, int offset) {
        double[] result = new double[data.length];

        System.arraycopy(data, offset, result, 0, data.length - offset);

        return result;
    }

    /*private static void play(String pathname) {
        try {
            Clip c = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(pathname));

            c.open(ais);
            c.loop(0);

            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }*/

    public static void main(String[] args) {

       if (args.length < 2) throw new IllegalArgumentException("As the arguments of the program " +
                "input path and output path are needed.");

        SunRadio radio = new SunRadio();

        radio.openWavFile(args[0]);

        radio.run();

        radio.closeFiles();
    }
}
