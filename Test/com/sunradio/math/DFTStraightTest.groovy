package com.sunradio.math

import static java.lang.Math.*

class DFTStraightTest extends GroovyTestCase {

    final EPS = 0.00001
    final ITERATIONS = 100
    final SPLIT = 100
    final NUMBER = 80.0

    double[] buffer = new double[ITERATIONS]
    double[] result = new double[ITERATIONS]
    DFTStraight dftStraight = new DFTStraight()

    //f(t) = sin(t)
    void testSin() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(2 * PI * i / SPLIT)

        dftStraight.run(buffer)
        result = dftStraight.getAmplitudes()

        int amount = 0, size = dftStraight.getSize()
        for (int i = 0; i < size; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = NUMBER*sin(t)
    //Test passes with any number
    void testConstMultSin() {

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = NUMBER * sin(2 * PI * i / SPLIT)

        dftStraight.run(buffer)
        result = dftStraight.getAmplitudes()

        int amount = 0, size = dftStraight.getSize()
        for (int i = 0; i < size; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = sin(NUMBER*t)
    void testSinMultConst() throws IllegalArgumentException{

        if ((NUMBER > SPLIT) || (NUMBER < SPLIT / 2))
            throw new IllegalArgumentException("Number should be less than SPLIT and more than SPLIT/2 due to sinus period")

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(NUMBER * 2 * PI * i / SPLIT)

        dftStraight.run(buffer)
        result = dftStraight.getAmplitudes()

        int amount = 0, size = dftStraight.getSize()
        for (int i = 0; i < size; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(1, amount)
    }

    //f(t) = NUMBER*sin(t) + sin(Number*t)
    void testTwoSinuses() {

        if ((NUMBER > SPLIT) || (NUMBER < SPLIT / 2))
            throw new IllegalArgumentException("Number should be less than SPLIT and more than SPLIT/2 due to sinus period")

        //Split the sinus period in SPLIT pieces and take the sinus value in each of them
        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = NUMBER*sin(2 * PI * i / SPLIT) + sin(NUMBER * 2 * PI * i / SPLIT)

        dftStraight.run(buffer)
        result = dftStraight.getAmplitudes()

        int amount = 0, size = dftStraight.getSize()
        for (int i = 0; i < size; i++)
            if (result[i] > EPS)
                amount++

        assertEquals(2, amount)
    }

    void testApplyNewAmplitudes() {
        double[] modulation
        DFTStraight dftStraightApplied = new DFTStraight()

        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(2 * PI * i / SPLIT)

        dftStraight.run(buffer)

        modulation = new double[dftStraight.size]
        Random random = new Random()
        for (int i = 0; i < dftStraight.size; i++)
            modulation[i] = abs(random.nextDouble())

        dftStraightApplied.setData(dftStraight.getData())
        dftStraightApplied.applyNewAmplitudes(modulation)

        double oldPhase, newPhase
        for (int i = 0; i < dftStraight.size; i++) {
            oldPhase = dftStraight.getPhase(i)
            newPhase = dftStraightApplied.getPhase(i)

            assert abs(oldPhase - newPhase)  < EPS
        }
    }

    void testApplyNewPhases() {
        double[] modulation
        DFTStraight dftStraightApplied = new DFTStraight()

        for (int i = 0; i < ITERATIONS; i++)
            buffer[i] = sin(2 * PI * i / SPLIT)

        dftStraight.run(buffer)

        modulation = new double[dftStraight.size]
        Random random = new Random()
        for (int i = 0; i < dftStraight.size; i++)
            modulation[i] = abs(random.nextDouble())

        dftStraightApplied.setData(dftStraight.getData())
        dftStraightApplied.applyNewPhases(modulation)

        double oldAmplitude, newAmplitude
        for (int i = 0; i < dftStraight.size; i++) {
            oldAmplitude = dftStraight.getAmplitude(i)
            newAmplitude = dftStraightApplied.getAmplitude(i)

            assert abs(oldAmplitude - newAmplitude) < EPS
       }
    }
}

