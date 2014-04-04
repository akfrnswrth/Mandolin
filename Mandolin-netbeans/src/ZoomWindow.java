

import java.io.Serializable;

/**
 * Defines a rectangular window on the Cartesian plane
 * @author ali.kocaturk.1
 */
public class ZoomWindow implements Serializable {
    private double tlx, tly, brx, bry;
    
    /**
     * Creates a ZoomWindow centered on the origin with size zero
     */
    public ZoomWindow() {
        tlx = tly = brx = bry = 0;
    }

    /**
     * Constructs a new ZoomWindow from the given one.
     * @param ozm The ZoomWindow to copy from
     */
    public ZoomWindow(ZoomWindow ozm) {
        tlx = ozm.tlx;
        tly = ozm.tly;
        brx = ozm.brx;
        bry = ozm.bry;
    }
    
    /**
     * Constructs a new ZoomWindow whose corners are defined by the two Complex 
     * numbers
     * @param tl the top left corner
     * @param br the bottom right corner
     */
    public ZoomWindow(Complex tl, Complex br) {
        tlx = tl.getReal();
        tly = tl.getImag();
        brx = br.getReal();
        bry = br.getImag();
    }
    
    /**
     * Constructs a new ZoomWindow whose corners are defined by the given XY
     * coordinates.
     * @param tlx The top left X-coordinate
     * @param tly The top left Y-coordinate
     * @param brx The bottom right X-coordinate
     * @param bry The bottom right Y-coordinate
     */
    public ZoomWindow(double tlx, double tly, double brx, double bry) {
        this.tlx = tlx;
        this.tly = tly;
        this.brx = brx;
        this.bry = bry;
    }
    
    /**
     * Gets the top left x-coordinate
     * @return the top left x-coordinate
     */
    public double getTlx() {
        return tlx;
    }
    
    /**
     * Gets the top left y-coordinate
     * @return the top left y-coordinate
     */
    public double getTly() {
        return tly;
    }
    
    /**
     * Gets the bottom left x-coordinate
     * @return the bottom left x-coordinate
     */
    public double getBrx() {
        return brx;
    }
    
    /**
     * Gets the bottom right y-coordinate
     * @return the bottom right y-coordinate
     */
    public double getBry() {
        return bry;
    }
    
    /**
     * Gets the Complex number representing the top left corner
     * @return the top left corner
     */
    public Complex getTl() {
        return new Complex(tlx, tly);
    }
    
    /**
     * Gets the Complex number representing the bottom right corner
     * @return the bottom right corner
     */
    public Complex getBr() {
        return new Complex(brx, bry);
    }
    
    /**
     * Sets the window according to the given Complex numbers
     * @param tl the top left corner
     * @param br the bottom right corner
     */
    public void setCoords(Complex tl, Complex br) {
        tlx = tl.getReal();
        tly = tl.getImag();
        brx = br.getReal();
        bry = br.getImag();
    }

    
    /**
     * Gets the String representation of this ZoomWindow
     * @return the String representation of this ZoomWindow
     */
    @Override
    public String toString() {
        return getTl().toString() + ", " + getBr().toString();
    }
}
