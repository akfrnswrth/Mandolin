/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 * The Burning Ship fractal: (|Re(z)| + i|Im(z)|)^2 + c
 * @author ali
 */
public class BurningShip implements Formula {

    /**
     * Calculates according the given values
     * @param z
     * @param c
     * @return (|Re(z)| + i|Im(z)|)^2 + c
     */
    public Complex calc(Complex z, Complex c) {
        Complex t = new Complex(Math.abs(z.getReal()), Math.abs(z.getImag()));
        return t.mult(t).add(c);
    }

}
