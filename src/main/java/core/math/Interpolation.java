package main.java.core.math;

/**
 * Class to interpolate.
 *
 * @author V.Kremneva
 */
class Interpolation {
    /**
     * Perform linear interpolation by two points
     * @param x0 x from the first point
     * @param y0 f(x) from the first point
     * @param x1 x from the second point
     * @param y1 f(x) from the second point
     * @param x x from the point we are interested in
     * @return f(x) from the point we are interested in
     */
    static double linearByX(double x0, double y0, double x1, double y1, double x) {
        return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
    }

    /**
     * Perform linear interpolation by two points
     * @param x0 x from the first point
     * @param y0 f(x) from the first point
     * @param x1 x from the second point
     * @param y1 f(x) from the second point
     * @param y f(x) from the point we are interested in
     * @return x from the point we are interested in
     */
    static double linearByY(double x0, double y0, double x1, double y1, double y) {
        return x0 + (y - y0) * (x1 - x0) / (y1 - y0);
    }
}
