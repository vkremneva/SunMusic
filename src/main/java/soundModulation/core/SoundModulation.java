package soundModulation.core;

import soundModulation.external.WavFile;

/**
 * @author V.Kremneva
 */
public class SoundModulation implements SoundModulationAPI {

    @Override
    public WavFile modulateAmplitudes(String fileName) {
        SunRadio radio = new SunRadio();

        radio.openWavFile(fileName);
        radio.adjustVolumeAccordingToLightLevel();
        radio.closeFile();

        return radio.getWavOutput();
    }
}
