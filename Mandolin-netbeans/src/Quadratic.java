

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * The classic Mandelbrot formula: z^2 + c
 * @author ali.kocaturk.1
 */
public class Quadratic implements Formula {

    /**
     * Calculates according the given values
     * @param z
     * @param c
     * @return z^2 + c
     */
    public Complex calc(Complex z, Complex c) {
        return z.mult(z).add(c);
    }
}
