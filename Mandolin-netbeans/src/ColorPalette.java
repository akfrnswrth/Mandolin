

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * 256-color palette
 * @author ali
 */
public class ColorPalette {

    int[] cgradient;
    boolean[] udef;
    private static final int R_CH = 16;
    private static final int G_CH = 8;
    private static final int B_CH = 0;

    /**
     * Constructs an all-black palette with no setpoints.
     */
    public ColorPalette() {
        cgradient = new int[256];
        udef = new boolean[256];
        clearPalette();
    }

    /**
     * Clears all setpoints and sets the palette to black
     */
    private void clearPalette() {
        for(int i = 0; i < 256; i++) {
            cgradient[i] = 0x000000;
            udef[i] = false;
        }
    }

    /**
     * Adds 7 setpoints resembling the color temperature scale
     */
    public void setDefaultPalette() {
        addPoint(0, 0x000000);
        addPoint(1, 0xff0000);
        addPoint(51, 0xffff00);
        addPoint(102, 0xffffff);
        addPoint(153, 0x00ffff);
        addPoint(204, 0x0000ff);
        addPoint(255, 0x000000);
    }

    /**
     * Takes a number from 0 to 255 (inclusive) and finds the corresponding
     * color
     * @param v the color's index in the palette
     * @return the RGB value of the color
     */
    public int toRGB(int v) {
        if (v < 0) {
            v = 0;
        }
        if (v >= 256) {
            v = 255;
        }
        return cgradient[v];
    }

    /**
     * Adds a setpoint of the given color at the given index
     * @param i the index of the color to set
     * @param color the RGB color to set
     */
    public void addPoint(int i, int color) {
        int lpa = i;
        cgradient[i] = color;
        udef[i] = true;

        rcpts(lpa);
    }

    /**
     * Gets the number of setpoints in this palette
     * @return the number of setpoints in this palette
     */
    public int getNumPoints() {
        int count = 0;
        for(boolean b : udef) {
            if(b) count++;
        }
        return count;
    }
    
    /**
     * 1-INDEXED!!!!
     * Finds the given palette index for the specified setpoint.  The setpoint's
     * number should be between 1 and getNumPoints() inclusive.
     * @param idx the 1-indexed number of the setpoint
     * @return the index of the setpoint
     */
    public int getPoint(int idx) {
        int count = 0;
        for(int i = 0; i < 256; i++) {
            if(udef[i]) {
                count++;
            }
            if(idx == count) {
                return cgradient[i];
            }
        }
        throw new ArrayIndexOutOfBoundsException("Invalid index in getPoint");
    }
    
    
    /**
     * 1-INDEXED!!!
     * Sets the color of the specified setpoint.  The setpoints number should be
     * between 1 and getNumPoints() inclusive.
     * @param idx the 1-indexed number of the setpoint
     * @param color the color to set the point
     */
    public void setPoint(int idx, int color) {
        int count = 0;
        for(int i = 0; i < 256; i++) {
            if(udef[i]) {
                count++;
            }
            if(idx == count) {
                cgradient[i] = color;
                rcpts(i);
                return;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Invalid index in getPoint");
    }

    /**
     * Recalculates the gradient for the points affected by changing the point
     * at the given index
     * @param lpa the palette index of the changed point
     */
    private void rcpts(int lpa) {
        int lipa = -1;
        int ripa = -1;

        // find left point of interpolation
        for (int i = lpa - 1; i >= 0; i--) {
            if (udef[i]) {
                lipa = i;
                i = -1;
            }
        }

        // fill in left edges
        if (lipa == -1) {
            for (int i = 0; i < lpa; i++) {
                cgradient[i] = cgradient[lpa];
            }
        } else {
            double rm = (double) (getGradientChannel(lpa, R_CH) - getGradientChannel(lipa, R_CH)) / (lpa - lipa);
            double gm = (double) (getGradientChannel(lpa, G_CH) - getGradientChannel(lipa, G_CH)) / (lpa - lipa);
            double bm = (double) (getGradientChannel(lpa, B_CH) - getGradientChannel(lipa, B_CH)) / (lpa - lipa);
            for (int i = lipa + 1; i < lpa; i++) {
                cgradient[i] = (((int) (rm * (i - lpa) + getGradientChannel(lpa, R_CH)) << R_CH)
                        | ((int) (gm * (i - lpa) + getGradientChannel(lpa, G_CH)) << G_CH)
                        | ((int) (bm * (i - lpa) + getGradientChannel(lpa, B_CH))));
            }
        }

        // Find right point of interpolation
        for (int i = lpa + 1; i < 256; i++) {
            if (udef[i]) {
                ripa = i;
                i = 256;
            }
        }

        //fill in right edges
        if (ripa == -1) {
            for (int i = lpa + 1; i < 256; i++) {
                cgradient[i] = cgradient[lpa];
            }
        } else {
            double rm = (double) (getGradientChannel(lpa, R_CH) - getGradientChannel(ripa, R_CH)) / (lpa - ripa);
            double gm = (double) (getGradientChannel(lpa, G_CH) - getGradientChannel(ripa, G_CH)) / (lpa - ripa);
            double bm = (double) (getGradientChannel(lpa, B_CH) - getGradientChannel(ripa, B_CH)) / (lpa - ripa);
            for (int i = lpa + 1; i < ripa; i++) {
                cgradient[i] = (((int) (rm * (i - lpa) + getGradientChannel(lpa, R_CH)) << R_CH)
                        | ((int) (gm * (i - lpa) + getGradientChannel(lpa, G_CH)) << G_CH)
                        | ((int) (bm * (i - lpa) + getGradientChannel(lpa, B_CH))));
            }
        }
    }

    /**
     * Gets the R, G, or B channel of the point at the given index
     * @param i the palette index
     * @param channel the channel to get (R_CH, G_CH, or B_CH)
     * @return the 0-255 value of the color
     */
    private int getGradientChannel(int i, int channel) {
        return (cgradient[i] >> channel) & (0xff);
    }
}
