package com.sunradio.core;

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
                frames_read = wavInput.readFrames(buffer, FRAMES);

                transformable.run(buffer);
                System.out.println(transformable.getMaxAmplitude()+" "+transformable.getMinAmplitude());
                lightLevel = LightLevel.getLightLevel(indAmount,
                        transformable.getMinAmplitude(), transformable.getMaxAmplitude());

                modulated = AM.modulate(transformable.getAmplitudes(), lightLevel);
                transformable.setData(AM.applyModulationToComplex(transformable.getData(), modulated));

                buffer = DFTInverse.run(transformable.getData());

                wavOutput.writeFrames(buffer, FRAMES);
            } while (frames_read != 0);

            wavInput.close(); wavOutput.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
