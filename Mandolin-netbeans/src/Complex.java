

import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Complex number class with trig functions
 * @author ali
 */
public class Complex implements Serializable {

    private double real;
    private double imag;
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex I = new Complex(0, 1);
    public static final Complex NEGI = new Complex(0, -1);
    public static final Complex TWO = new Complex(2, 0);
    public static final Complex HALF = new Complex(0.5, 0);

    /**
     * Constructs a <code>Complex</code> object with a value of 0 + 0i.
     */
    public Complex() {
        real = imag = 0;
    }

    /**
     * Constructs a <code>Complex</code> object with a value of
     * <code>re</code> + i * <code>im</code>.
     * @param re real part
     * @param im imaginary part
     */
    public Complex(double re, double im) {
        real = re;
        imag = im;
    }

    /**
     * Constructs a deep copy of another Complex number.
     * @param x the Complex number to copy
     */
    public Complex(Complex x) {
        real = x.real;
        imag = x.imag;
    }

    // simple binary operations
    /**
     * Multiplies this <code>Complex</code> object with the given
     * <code>Complex</code> object without modifying this object.
     * @param x the multiplicand
     * @return the product
     */
    public Complex mult(Complex x) {
        return new Complex(real * x.real - imag * x.imag,
                real * x.imag + imag * x.real);
    }

    /**
     * Adds this <code>Complex</code> object with the given <code>Complex</code>
     * object without modifying this object.
     * @param x the Complex to be added
     * @return the sum
     */
    public Complex add(Complex x) {
        return new Complex(real + x.real, imag + x.imag);
    }

    public Complex sub(Complex x) {
        return new Complex(real - x.real, imag - x.imag);
    }

    /**
     * Calculates <code>this</code>/<code>d</code>
     * @param d the denominator
     * @return the quotient of this and the denominator
     */
    public Complex div(Complex d) {
        double tdd = d.abs2();
        return new Complex((real * d.real + imag * d.imag)/tdd,
                (imag * d.real - real * d.imag)/tdd);
    }

    /**
     * Calculates <code>this</code>^y
     * @param y
     * @return e^(y log(<code>this</code>))
     */
    public Complex pow(Complex y) {
        if (abs2() < Math.ulp(abs2())) {
            return new Complex(0, 0);
        }
        if (imag == 0.0 && y.real > 0 && Math.floor(y.real)==y.real) {
            int p = (int)(y.real);
            Complex tips = this;
            for(int i = 1; i < p; i++) {
                tips = tips.mult(this);
            }
            return tips;
        }
        return log().mult(y).exp();
    }

    // unary operations 
    /**
     * Calculates the sine of this Complex number
     * @return the sine of this Complex number
     */
    public Complex sin() {
        return new Complex(Math.sin(real) * Math.cosh(imag),
                Math.cos(real) * Math.sinh(imag));
    }

    /**
     * Calculates the inverse sine of this Complex number 
     * @return -i ln(ix + sqrt(1-x^2))
     */
    public Complex asin() {
        return ONE.sub(mult(this)).sqrt().add(I.mult(this)).log().mult(new Complex(0, -1));

    }

    /**
     * Calculates the cosine of this Complex number
     * NOTE: Tests OK, XaoS wrong?
     * @return the cosine of this Complex number
     */
    public Complex cos() {
        return new Complex(Math.cos(real) * Math.cosh(imag),
                -Math.sin(real) * Math.sinh(imag));
        //return HALF.mult(I.mult(this).exp().add(NEGI.mult(this).exp()));
    }

    /**
     * Calculates the inverse cosine of this Complex number
     * @return -i ln(x + sqrt(1 - x^2))
     */
    public Complex acos() {
        return NEGI.mult(ONE.sub(this.mult(this)).sqrt().mult(I).add(this).log());
    }

    /**
     * Calculates the tangent of this Complex number by sin(x)/cos(x)
     * @return the tangent of this Complex number
     */
    public Complex tan() {
        return sin().div(cos());
    }

    /**
     * Calculates the arctangent of this Complex number
     * @return The arctangent of this Complex number
     */
    public Complex atan() {
        return new Complex(0, 0.5).mult(ONE.sub(I.mult(this)).log().sub(ONE.add(I.mult(this)).log()));
    }

    // WHAT IS THIS HYPERBOLIC SORCERY?
    /**
     * Calculates the hyperbolic sine of this Complex number.
     * Uses sinh(x) = (e^x - e^(-x))/2
     * @return sinh(this)
     */
    public Complex sinh() {
        return exp().sub(new Complex(-1, 0).mult(this).exp()).div(new Complex(2, 0));
    }

    /**
     * Calculates the inverse hyperbolic sine of this Complex number.
     * @return arcsinh(this)
     */
    public Complex asinh() {
        return add(mult(this).add(ONE).sqrt()).log();
    }

    /**
     * Calculates the hyperbolic cosine of this Complex number.
     * Uses cosh(x) = (e^x + e^(-x))/2
     * @return cosh(this)
     */
    public Complex cosh() {
        return exp().add(new Complex(-1, 0).mult(this).exp()).div(new Complex(2, 0));
    }

    /**
     * Calculates the inverse hyperbolic cosine of this Complex number.
     * @return arccosh(this)
     */
    public Complex acosh() {
        return add(mult(this).sub(ONE).sqrt()).log();
    }

    /**
     * Calculates the hyperbolic tangent of this Complex number.
     * @return tanh(this)
     */
    public Complex tanh() {
        return sinh().div(cosh());
    }

    /**
     * Calculates the inverse hyperbolic tangent of this Complex number.
     * @return arctanh(this)
     */
    public Complex atanh() {
        return ONE.add(this).div(ONE.sub(this)).log().div(new Complex(2, 0));
    }

    /**
     * Calculates the exponential function of this Complex number.
     * @return e^(<code>real</code>+<i>i</i>*<code>imag</code>)
     */
    public Complex exp() {
        return new Complex(Math.exp(real) * Math.cos(imag),
                Math.exp(real) * Math.sin(imag));
    }

    /**
     * Returns the argument (direction) of this Complex number.
     * @return <code>atan2(imag, real)</code>
     */
    public double arg() {
        return Math.atan2(imag, real);
    }

    /**
     * Calculates the natural logarithm of this Complex number
     * @return the natural logarithm of this Complex number.
     */
    public Complex log() {
        return new Complex(Math.log(abs()), arg());
    }

    /**
     * Gets the real part of this <code>Complex</code> object.
     * @return the real part of this <code>Complex</code> object.
     */
    public Double getReal() {
        return real;
    }

    /**
     * Gets the imaginary part of this <code>Complex</code> object.
     * @return the imaginary part of this <code>Complex</code> object.
     */
    public Double getImag() {
        return imag;
    }

    /**
     * Sets the real part of this Complex number.
     * @param r the new real part
     */
    public void setReal(double r) {
        real = r;
    }

    /**
     * Sets the imaginary part of this Complex number.
     * @param i the new imaginary part
     */
    public void setImag(double i) {
        imag = i;
    }

    /**
     * Calculates the square of the absolute value of this Complex number.  
     * NOTE: abs2() is much faster than abs().
     * @return |this|^2
     */
    public double abs2() {
        return real * real + imag * imag;
    }

    /**
     * Calculates the principal branch of the square root of this Complex number
     * @return sqrt(this)
     */
    public Complex sqrt() {
        double r = Math.sqrt(abs());
        double theta = arg() / 2;
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    /**
     * Calucates the absolute value of this Complex number
     * @return sqrt(real^2 + imag^2)
     */
    public double abs() {
        if(real == 0.0) {   // Imaginary number
            return Math.abs(imag);
        }
        if(imag == 0.0) {   // Real number
            return Math.abs(real);  
        }
        return Math.sqrt(abs2());
    }

    /**
     * Calculates the complex conjugate of this Complex number.
     * @return Conj(this)
     */
    public Complex conj() {
        return new Complex(real, -imag);
    }

    /**
     * Returns the string representation of this Complex number in the form
     * a + bi
     * @return a <code>String</code> representation of this object.
     */
    @Override
    public String toString() {
        return real + " + " + imag + "i";
    }
}
