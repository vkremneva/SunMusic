package com.sunradio.core;

import com.external.WavFile;
import com.sunradio.math.AM;
import com.sunradio.math.DFTInverse;
import com.sunradio.math.DFTStraight;
import com.sunradio.math.Filter;

import java.io.File;

/**
 * @author V.Kremneva
 */
public class Main {

    public static void main(String[] args) {
        try {
            final int FRAMES = 100;
            WavFile wavInput = WavFile.openWavFile(new File("C:\\Users\\Merveilleuse\\IdeaProjects\\SunRadio\\launch.wav"));

            WavFile wavOutput = WavFile.newWavFile(new File("C:\\Users\\Merveilleuse\\IdeaProjects\\SunRadio\\new1.wav"),
                    wavInput.getNumChannels(), wavInput.getNumFrames(),
                    wavInput.getValidBits(), wavInput.getSampleRate());

            int numChannels = wavInput.getNumChannels();
            int indAmount = FRAMES * numChannels;
            double[] buffer = new double[indAmount];
            double[] lightLevel, modulated;

            int frames_read;
            DFTStraight transformable;
            transformable = new DFTStraight();
            do {
                //read next 'FRAMES' into buffer
                frames_read = wavInput.readFrames(buffer, FRAMES);

                //apply filter to soften the edges
                buffer = Filter.apply(buffer, Filter.BlackmanNuttall(indAmount));

                //run Fourier transform
                transformable.run(buffer);

                //get current level of light
                lightLevel = LightLevel.getLightLevel(transformable.getSize(),
                        transformable.getMinAmplitude(), transformable.getMaxAmplitude());

                //apply amplitude modulation
                modulated = AM.modulate(transformable.getAmplitudes(), lightLevel);

                //set modulated data to 'transformable'
                transformable.setData(AM.applyModulationToComplex(transformable.getData(), modulated));

                //run inverse Fourier transform
                buffer = DFTInverse.run(transformable.getData());

                //filter the noise
                buffer = Filter.denoise(buffer);

                //write data to new .waw file
                wavOutput.writeFrames(buffer, FRAMES);
            } while (frames_read != 0);

            wavInput.close(); wavOutput.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
