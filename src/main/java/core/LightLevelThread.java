package main.java.core;

class LightLevelThread extends Thread {
    private volatile int lightLevel;
    private int sleep;

    LightLevelThread() {
        lightLevel = LightLevel.MAX / 2;
        sleep = 500;
    }

    LightLevelThread(int sleepValue) {
        lightLevel = LightLevel.MAX / 2;
        sleep = sleepValue;
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
                int newLightLevel;

                newLightLevel = LightLevel.get();

                lightLevel = smooth(newLightLevel, lightLevel);

                Thread.sleep(sleep);
            }
        } catch (InterruptedException e) {}
    }
}
