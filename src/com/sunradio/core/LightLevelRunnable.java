package com.sunradio.core;

class LightLevelRunnable implements Runnable {
    private Thread thread;
    private volatile int lightLevel;
    private int currentDelay;

    LightLevelRunnable(int delay) {
        currentDelay = delay;
        thread = new Thread(this, "Current light level");
        thread.start();
    }

    public void run() {
        try {
            int storageSize = currentDelay / 1000;
            int[] storage = new int[storageSize];

            for (int i = 0; i < storageSize - 2; i++) {
                storage[i] = LightLevel.get();
                Thread.sleep(1000);
            }

            while (!thread.isInterrupted()) {
                storage[storageSize - 1] = LightLevel.get();

                int averageLightLevel = 0;
                for (int i = 0; i < storageSize - 1; i++)
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
