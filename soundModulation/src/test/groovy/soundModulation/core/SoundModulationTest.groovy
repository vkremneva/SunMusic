package soundModulation.core

import soundModulation.external.Sound

class SoundModulationTest extends GroovyTestCase {
    void testModulateAmplitudes() {
        String inputFile = "C:\\Users\\LEV\\IdeaProjects\\SunRadioo\\launch.wav"

        SoundModulation action = new SoundModulation()
        action.modulateAmplitudes(inputFile)

        Sound.playSound(inputFile).join()
    }
}
