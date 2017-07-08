package com.sunradio.core;

class LightLevelThread extends Thread {
    private volatile int lightLevel;
    private int currentDelay;

    LightLevelThread(int delay) {
        lightLevel = LightLevel.MAX / 2;
        currentDelay = delay;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    @Override
    public void run() {
        try {
            int storageSize = currentDelay / 1000;
            int[] storage = new int[storageSize];

            for (int i = 0; i < storageSize - 1; i++) {
                storage[i] = LightLevel.get();
                Thread.sleep(1000);
            }

            while (!Thread.interrupted()) {
                storage[storageSize - 1] = LightLevel.get();

                int averageLightLevel = 0;
                for (int i = 0; i < storageSize; i++)
                    averageLightLevel += storage[i];
                averageLightLevel /= storageSize;

                lightLevel = averageLightLevel;

                System.arraycopy(storage, 1, storage, 0, storageSize - 1);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }
    }
}
