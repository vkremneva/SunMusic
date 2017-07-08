package com.sunradio.core

class LightLevelThreadTest extends GroovyTestCase {

    void testGetLightLevel() {
        LightLevelThread value = new LightLevelThread(10000)

        value.start()

        try {
            for (int i = 0; i < 20; i++) {
                System.out.println(value.getLightLevel())

                Thread.sleep(1000)
            }
        } catch (InterruptedException e) {}

        value.interrupt()
    }
}
