

import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Used to keep the chosen Formula object synchronized between MandelViewer,
 * Mandelbrot, and Julia.
 * @author ali.kocaturk.1
 */
public class FormulaContainer implements Serializable {
    Formula cfrm;
    
    /**
     * Constructs a FormulaContainer with the given Formula
     * @param nfrm the Formula to use
     */
    public FormulaContainer(Formula nfrm) {
        cfrm = nfrm;
    }
    
    /**
     * Returns the contained Formula
     * @return the Formula being used
     */
    public Formula getFormula() {
        return cfrm;
    }
    
    /**
     * Sets the Formula to use
     * @param nfrm the new Formula to use
     */
    public void setFormula(Formula nfrm) {
        cfrm = nfrm;
    }
    
    /**
     * Calls the calc() method of the Formula currently being used
     * @param z the Z value to pass to the Formula
     * @param c the C value to pass to the Formula
     * @return the result of calculation
     */
    public Complex calc(Complex z, Complex c) {
        return cfrm.calc(z, c);
    }
}
