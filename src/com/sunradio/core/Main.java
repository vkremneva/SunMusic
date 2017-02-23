package com.sunradio.core;

import com.external.Complex;
import com.external.WavFile;
import com.sunradio.math.AM;
import com.sunradio.math.DFTInverse;
import com.sunradio.math.DFTStraight;

import java.io.File;

/**
 * @author V.Kremneva
 */
public class Main {

    public static void main(String[] args) {
        try {
            final int FRAMES = 100;
            WavFile wavInput = WavFile.openWavFile(new File("C:\\Users\\Merveilleuse\\IdeaProjects\\SunRadio\\bells.wav"));

            WavFile wavOutput = WavFile.newWavFile(new File("C:\\Users\\Merveilleuse\\IdeaProjects\\SunRadio\\new.wav"),
                    wavInput.getNumChannels(), wavInput.getNumFrames(),
                    wavInput.getValidBits(), wavInput.getSampleRate());

            int numChannels = wavInput.getNumChannels();
            int indAmount = FRAMES * numChannels;
            double[] buffer = new double[indAmount];
            double[] lightLevel, modulated;

            int frames_read;
            Complex[] transformedByDFT;
            do {
                frames_read = wavInput.readFrames(buffer, FRAMES);

                transformedByDFT = DFTStraight.run(buffer);
                lightLevel = LightLevel.getLightLevel(indAmount);

                modulated = AM.modulate(DFTStraight.getAmplitudes(transformedByDFT), lightLevel);
                transformedByDFT = AM.applyModulationToComplex(transformedByDFT, modulated);

                buffer = DFTInverse.run(transformedByDFT);

                wavOutput.writeFrames(buffer, FRAMES);
            } while (frames_read != 0);

            wavInput.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
