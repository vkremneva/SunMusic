package com.sunradio.core;

import com.external.WavFile;
import com.sunradio.math.*;

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
        overlapIndAmount = OVERLAP * numChannels;
        offset = FRAMES / OVERLAP;
        outputBufferIndAmount = bufferIndAmount + overlapIndAmount;
        wholeIndAmount = wavInput.getNumFrames() * numChannels;
        outputPath = args[1];
    }

    /**
     * Create empty output file like input file but stretched
     *
     * @param stretch in how many times we want to stretch
     * @return empty stretched wav file
     */
    private WavFile createStretchedOutputFile(int stretch) {
        try {
            return WavFile.newWavFile(new File(outputPath),
                    wavInput.getNumChannels(), wavInput.getNumFrames() * stretch,
                    wavInput.getValidBits(), wavInput.getSampleRate());

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return new WavFile();
    }

    private WavFile createOutputFile() {
        return createStretchedOutputFile(1);
    }

    /**
     * Create 'wavOutput' file as the sum of another wav files
     * @param outputPath initial path to another wav files
     * @param amount amount and in thi case step between wav files
     */
    private void assembleWavFiles(String outputPath, int amount) { //todo: universalize
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
    }

    /**
     * Run the process of modulation and play the result
     */
    private void run() {
        /*
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
            lightLevel = LightLevel.getAverageLightLevel(buffer);

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

                    stretchedOutputFile = createStretchedOutputFile(outputPath + count, lightLevel);

                    //write data to new .waw file
                    stretchedOutputFile.writeFrames(outputBuffer, FRAMES);

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
        */

        double[] buffer = new double[bufferIndAmount * 2];
        int lightLevel, framesRead;

        wavOutput = createOutputFile();

        try {
            framesRead = wavInput.readFrames(buffer, FRAMES);
            if (framesRead != 0) {
                do {
                    //lightLevel = LightLevel.getAverageLightLevel(buffer);
                    lightLevel = 1024;

                    framesRead = wavInput.readFrames(buffer, FRAMES);

                    buffer = AM.modulate(buffer, lightLevel);

                    wavOutput.writeFrames(buffer, FRAMES);
                }
                while (framesRead != 0);
            }
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
        SunRadio radio = new SunRadio();

        radio.openWavFile(args);

        radio.run();

        radio.closeFiles();
    }
}