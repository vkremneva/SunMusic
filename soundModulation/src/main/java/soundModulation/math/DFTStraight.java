package soundModulation.math;

import soundModulation.external.Complex;

import java.util.Arrays;
import static java.lang.Math.*;

/**
 * Straight discrete Fourier transform.
 *
 * @author V.Kremneva
 */
public class DFTStraight {

    private Complex[] data; //Complex data
    private double maxAmplitude; //maximum value of real amplitude in 'data' array
    private double minAmplitude; //minimum value of real amplitude in 'data' array
    private int size; //size of 'data' array
    private boolean isTransformed; //flag whether was 'data' transformed by DFT or not

    public DFTStraight() {
        maxAmplitude = 0.0;
        minAmplitude = 0.0;
        size = 0;
        isTransformed = false;
    }

    public double getMaxAmplitude() {
        return maxAmplitude;
    }

    public double getMinAmplitude() {
        return minAmplitude;
    }

    public boolean isTransformed() {
        return isTransformed;
    }

    public Complex[] getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    /** Get a double phases from 'data' array.
     *
     * @return a double array which contains phase values
     */
    double[] getPhases() {
        double[] phases = new double[size];
        double allowance;
        for (int i = 0; i < size; i++) {

            if (data[i].re() > 0) allowance = 0;
            else if (data[i].im() > 0) allowance = PI;
            else allowance = -PI;

            phases[i] = allowance + atan(data[i].im() / data[i].re());
        }

        return phases;
    }

    /** Get a phase value on specific harmonic
     *
     * @param n frequency value of harmonic
     * @return double value of phase of harmonic
     */
    public double getPhase(int n) {
        double allowance;
        if (data[n].re() > 0) allowance = 0;
        else if (data[n].im() > 0) allowance = PI;
        else allowance = -PI;

        return allowance + atan(data[n].im() / data[n].re());
    }

    private static double getPhase(Complex[] data, int n) {
        double allowance;
        if (data[n].re() > 0) allowance = 0;
        else if (data[n].im() > 0) allowance = PI;
        else allowance = -PI;

        return allowance + atan(data[n].im() / data[n].re());
    }

    /** Get a double amplitudes from 'data' array.
     *
     * @return a double array which contains amplitude values
     */
    public double[] getAmplitudes() {
        double[] amplitudes = new double[size];

        for (int i = 0; i < size; i++)
            amplitudes[i] = data[i].abs() / ((size - 1) * 2); // '-1)*2' due to cutting in half

        return amplitudes;
    }

    static double[] getAmplitudes(Complex[] data) {
        double[] amplitudes = new double[data.length];

        for (int i = 0; i < data.length; i++)
            amplitudes[i] = data[i].abs() / ((data.length - 1) * 2); // '-1)*2' due to cutting in half

        return amplitudes;
    }

    static double[] getPhases(Complex[] data) {
        double[] phases = new double[data.length];
        double allowance;
        for (int i = 0; i < phases.length; i++) {

            if (data[i].re() > 0) allowance = 0;
            else if (data[i].im() > 0) allowance = PI;
            else allowance = -PI;

            phases[i] = allowance + atan(data[i].im() / data[i].re());
        }

        return phases;
    }

    /** Get an amplitude value on specific harmonic
     *
     * @param n frequency value of harmonic
     * @return double value of amplitude of harmonic
     */
    public double getAmplitude(int n) {
        return data[n].abs() / ((size - 1) * 2);
        // '-1)*2' due to cutting in half
    }

    public void setData(Complex[] newData) {
        size = newData.length;
        System.arraycopy(newData, 0, data, 0, size);

        double max, min;
        max = Double.MIN_VALUE; min = Double.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (this.getAmplitude(i) > max) max = this.getAmplitude(i);
            if (this.getAmplitude(i) < min) min = this.getAmplitude(i);
        }
        maxAmplitude = max; minAmplitude = min;
    }

    /**
     * Cut Complex array in two pieces.
     * We need this because the periods of the input data become split into "positive"
     * and "negative" frequency complex components. As a result, only half of array
     * contains data we are interested in and the rest of array is just a reflection with
     * opposite sign.
     *
     * @param dataToCut Complex data we want to be cut
     */
    private void cutDataInHalf(Complex[] dataToCut) {
        size = size / 2 + 1; //'+1' to include center value

        data = new Complex[size];
        System.arraycopy(dataToCut, 0, data, 0, size);
    }

    /** Run transform with search of max and min value of amplitude.
     *
     * @param buffer an array of the magnitudes
     * @return a Complex array which contains amplitude and phase values
     * @throws IllegalArgumentException if buffer is empty
     */
    public Complex[] run(double[] buffer) throws IllegalArgumentException {

        size = buffer.length;
        Double realAmplitude;
        Complex cBuffer, expDegree, tempData[];
        tempData = new Complex[size];

        if (size == 0) throw new IllegalArgumentException("Size of a buffer cannot be < 1.\n size = " + size);

        maxAmplitude = Double.MIN_VALUE;
        minAmplitude = Double.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            tempData[i] = new Complex(0, 0);

            for (int j = 0; j < size; j++) {
                cBuffer = new Complex(buffer[j]);
                expDegree = new Complex(0, -2 * PI * j * i / size);
                tempData[i] = tempData[i].add(cBuffer.mult(expDegree.exp()));
            }

            realAmplitude = tempData[i].abs() / size;
            if (realAmplitude > maxAmplitude) maxAmplitude = realAmplitude;
            if (realAmplitude < minAmplitude) minAmplitude = realAmplitude;
        }

        isTransformed = true;

        cutDataInHalf(tempData);

        return data;
    }

    /**
     * Change phase values in 'data' without changing amplitudes
     *
     * @param newPhases an array of phase values we want to apply
     */
    void applyNewPhases(double[] newPhases) {
        double a, b, allowance;
        for (int i = 0; i < size; i++) {
            if (data[i].re() > 0) allowance = 0;
            else if (data[i].im() > 0) allowance = -PI;
            else allowance = PI;

            a = data[i].abs() / sqrt(1 + pow(tan(newPhases[i] + allowance), 2.0));
            b = a * tan(newPhases[i] + allowance);

            //we get 'b' from equation for phase and 'a' from my condition:
            //i want the real amplitudes be the same

            data[i] = new Complex(a, b);
        }
    }

    /**
     *  Applying new phases to chosen Complex array
     *
     * @param newPhases new phases to set
     * @param oldData old data to set new phases to
     * @return Complex array with new phases and untouched amplitudes
     * @throws IllegalArgumentException if old data contains null
     */
    static Complex[] applyNewPhases(double[] newPhases, Complex[] oldData) throws IllegalArgumentException {
        if (newPhases.length != oldData.length)
            throw new IllegalArgumentException("Size of data to apply should be equal to old data size." +
                    "\nSize of data to apply: " + newPhases.length +
                    ". Size of old data: " + oldData.length + ".\n");

        double a, b, allowance;
        Complex[] result = new Complex[newPhases.length];

        for (int i = 0; i < newPhases.length; i++) {
            if (oldData[i] == null) {
                throw new IllegalArgumentException("When applying new phases old data must be set.");

            } else {
                if (oldData[i].re() > 0) allowance = 0;
                else if (oldData[i].im() > 0) allowance = -PI;
                else allowance = PI;

                a = oldData[i].abs() / sqrt(1 + pow(tan(newPhases[i] + allowance), 2.0));
                b = a * tan(newPhases[i] + allowance);

                //we get 'b' from equation for phase and 'a' from my condition:
                //i want the real amplitudes be the same

                result[i] = new Complex(a, b);
            }
        }
        return result;
    }

    /**
     * Change amplitude values in 'data' without changing phases
     *
     * @param newAmplitudes an array of amplitude values we want to apply
     */
    void applyNewAmplitudes(double[] newAmplitudes) {
         int sign;
         double a, b;
         for (int i = 0; i < size; i++) {
             if (this.getPhase(i) < 0) sign = -1;
             else sign = 1;
             b = pow(data[i].im(), 2.0) * pow(newAmplitudes[i], 2.0) * pow(size, 2.0);
             b = b / (pow(data[i].re(), 2.0) + pow(data[i].im(), 2.0));
             b = sqrt(b);

             a = sign * sqrt(pow(newAmplitudes[i], 2.0) * pow(size, 2.0) - pow(b, 2.0));

            //we get 'a' from equation for real amplitude and 'b' from my condition:
            //i want the real phase be the same

            data[i] = new Complex(a, b);
         }
    }

    static Complex[] applyNewAmplitudes(double[] newAmplitudes, Complex[] oldData) {
        int sign;
        double a, b;
        Complex[] result = new Complex[newAmplitudes.length];
        for (int i = 0; i < newAmplitudes.length; i++) {
            if (oldData[i] == null) {
                result[i] = new Complex(newAmplitudes[i], 0);
            } else {
                if (getPhase(oldData, i) < 0) sign = -1;
                else sign = 1;
                b = pow(oldData[i].im(), 2.0) * pow(newAmplitudes[i], 2.0) * pow(newAmplitudes.length, 2.0);
                b = b / (pow(oldData[i].re(), 2.0) + pow(oldData[i].im(), 2.0));
                b = sqrt(b);

                a = sign * sqrt(pow(newAmplitudes[i], 2.0) * pow(newAmplitudes.length, 2.0) - pow(b, 2.0));

                //we get 'a' from equation for real amplitude and 'b' from my condition:
                //i want the real phase be the same

                result[i] = new Complex(a, b);
            }
        }

        return result;
    }
}
