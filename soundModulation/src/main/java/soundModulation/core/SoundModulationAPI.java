package soundModulation.core;

import soundModulation.external.WavFile;

/**
 * @author V.Kremneva
 */
public interface SoundModulationAPI {

    /**
     * Modulate the amplitude according to light level.
     *
     * @param fileName name of the wav file to modulate
     * @return modified wav file
     */
    WavFile modulateAmplitudes(String fileName);
}
