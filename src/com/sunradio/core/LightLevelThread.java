package com.sunradio.core;

class LightLevelThread extends Thread {
    private volatile int lightLevel;
    private volatile int prevLightLevel;

    LightLevelThread() {
        lightLevel = LightLevel.MAX / 2;
        prevLightLevel = LightLevel.MAX / 2;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    private int smooth(int previous, int current) {
        int difference = Math.abs(current - previous);
        int result;

        if (current > previous) {
            if (difference < LightLevel.MAX / 2)
                result = (current + previous) / 2;
            else if (difference < (LightLevel.MAX - 1))
                result = (current + previous) / 2 - 1;
            else
                result = (current + previous) / 2 - 2;

        } else {
            if (difference < LightLevel.MAX / 2)
                result = (current + previous) / 2;
            else if (difference < (LightLevel.MAX - 1))
                result = (current + previous) / 2 + 1;
            else
                result = (current + previous) / 2 + 2;
        }

        return result;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {

                prevLightLevel = lightLevel;

                lightLevel = LightLevel.get();

                lightLevel = smooth(prevLightLevel, lightLevel);

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }
    }
}
