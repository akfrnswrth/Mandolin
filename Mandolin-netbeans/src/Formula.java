

import java.io.Serializable;

/**
 * To be used for custom formula interpreter
 * @author ali.kocaturk.1
 */
public interface Formula extends Serializable {
    /**
     * Evaluates the formula using the given z and c values
     * @param z
     * @param c
     * @return the result of calculation
     */
    public Complex calc(Complex z, Complex c);
}
