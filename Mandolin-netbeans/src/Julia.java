

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Stack;
import javax.swing.JComponent;

/**
 * A JComponent consisting of a graph of the Julia set.
 * @author ali
 */
public class Julia extends JComponent {

    private static final int DEFAULT_MAX_ITER = 60;
    private static final double DEFAULT_BAILOUT = 4;
    private static final double DEFAULT_LB = -2.25;
    private static final double DEFAULT_RB = 2.25;
    private static final double DEFAULT_UB = 2.25;
    private static final double DEFAULT_DB = -2.25;
    private int max_iter;
    private double bailout;    // Note: actually stores square of bailout
    private int width;
    private int height;
    private ZoomWindow cZoomWindow;
    private Stack<ZoomWindow> zoomStack;
    private Complex c;
    private ColorPalette cgrad;
    private FormulaContainer frmengine;

    /**
     * Constructs a Julia JComponent with the given color palette and formula
     * @param cp the color palette to use
     * @param nfrm the FormulaContainer containing the formula to use
     */
    public Julia(ColorPalette cp, FormulaContainer nfrm) {
        super();
        addMouseListener(new FractalMouseListener());
        zoomStack = new Stack<ZoomWindow>();
        cZoomWindow = new ZoomWindow(DEFAULT_LB, DEFAULT_UB,
                DEFAULT_RB, DEFAULT_DB);
        max_iter = DEFAULT_MAX_ITER;
        bailout = DEFAULT_BAILOUT;
        c = new Complex(0, 0);
        cgrad = cp;
        frmengine = nfrm;
    }

    /**
     * Sets the maximum number of iterations, then repaints the fractal.
     * @param iterations the new number of iterations
     */
    public void setIter(int iterations) {
        max_iter = iterations;
        repaint();
    }

    /**
     * Gets the maximum number of iterations.
     * @return the current number of iterations
     */
    public int getIter() {
        return max_iter;
    }

    public void setBailout(double newBailout) {
        bailout = newBailout * newBailout;
    }

    public double getBailout() {
        return Math.sqrt(bailout);
    }

    /**
     * Sets the c value used in calcJu's z[n+1] = (z[n])^2 + c
     * @param c
     */
    public void setC(Complex c) {
        this.c = c;
        repaint();
    }

    public void setZoomWindow(Complex tl, Complex br) {
        zoomStack.push(new ZoomWindow(cZoomWindow));
        cZoomWindow.setCoords(tl, br);
        System.out.println("Julia Zoom " + cZoomWindow);
        repaint();
    }

    /**
     * Returns to previous zoom, or zooms out if there are no previous zooms
     */
    public void setPreviousZoomWindow() {
        if (!zoomStack.isEmpty()) {
            cZoomWindow = zoomStack.pop();
        } else {
            // Zoomout
            double tlx = 2 * cZoomWindow.getTlx();
            double tly = 2 * cZoomWindow.getTly();
            double brx = 2 * cZoomWindow.getBrx();
            double bry = 2 * cZoomWindow.getBry();
            cZoomWindow = new ZoomWindow(tlx, tly, brx, bry);
        }
        System.out.println("Zoomout " + cZoomWindow);
        repaint();
    }

    /**
     * Paints the Mandelbrot set in this component's bounds over re = [-2, 1]
     * im = [-1, 1]
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Shape clip = g2.getClip();
        width = getWidth();
        height = getHeight();
        BufferedImage img = renderFractal(clip);
        g2.drawImage(img, 0, 0, this);
    }

    /**
     * Renders a Julia fractal.  "zero" areas are in the set; "255" areas 
     * are not.  The returned image is only rendered inside the given clip.
     * @param clip the clip to render
     * @return an new Image of the Mandelbrot set with the dimensions of this
     * component
     */
    private BufferedImage renderFractal(Shape clip) {
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        int grayval;
        System.out.print("Rendering Julia... ");
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (clip == null || clip.contains(x, y)) {
                    grayval = calcJu(xy2cpx(x, y));
                    img.setRGB(x, y, cgrad.toRGB(grayval));
                }
            }
        }
        System.out.println("Finished " + new Date());
        return img;
    }

    /**
     * Calculates whether the given point is in the Julia prisoner set by
     * iterating z[n+1] = (z[n])² + c.
     * @param z the point to check
     * @return 0-255 values proportional to the rate of divergence
     */
    private short calcJu(Complex z) {
        for (int iter = 0; iter < max_iter; iter++) {
            z = frmengine.calc(z, c);
            if (z.abs2() > bailout) {
                return (short) (255 - 255 * iter / max_iter);
            }
        }
        return 0;
    }

    /**
     * Converts Graphics position to Cartesian complex number.
     * Specialized for re = [-2, 1], im = [-1, 1].
     *
     * @param x Graphics x-value
     * @param y Graphics y-value
     * @return Complex representing given position
     */
    private Complex xy2cpx(int x, int y) {
        double re = (cZoomWindow.getBrx() - cZoomWindow.getTlx()) * x / width + cZoomWindow.getTlx();
        double im = (height - y) * (cZoomWindow.getTly() - cZoomWindow.getBry()) / height + cZoomWindow.getBry();
        return new Complex(re, im);
    }

    /**
     * Handles clicks on this component
     */
    private class FractalMouseListener implements MouseListener {

        private int sx, sy;

        /**
         * Constructs a new FractalMouseListener
         */
        public FractalMouseListener() {
            sx = sy = 0;
        }

        /**
         * Records top left of mouse press for zoom-in.
         * @param e
         */
        public void mousePressed(MouseEvent e) {
            sx = e.getX();
            sy = e.getY();
        }

        /**
         * Zooms in to the drawn rectangle upon mouse release.  If the rectangle
         * has not been drawn down and to the right, it sets to previous zoom.
         * @param e
         */
        public void mouseReleased(MouseEvent e) {
            int ex = e.getX();
            int ey = e.getY();
            if (sx > ex || sy > ey) {
                setPreviousZoomWindow();
            } else if (sx != ex && sy != ey) {
                Complex tl = xy2cpx(sx, sy);
                Complex br = xy2cpx(e.getX(), e.getY());
                double sqsz = Math.max(br.getReal() - tl.getReal(),
                        tl.getImag() - br.getImag());
                Complex tls = new Complex((tl.getReal() + br.getReal() - sqsz) / 2,
                        (tl.getImag() + br.getImag() + sqsz) / 2);
                Complex brs = new Complex((tl.getReal() + br.getReal() + sqsz) / 2,
                        (tl.getImag() + br.getImag() - sqsz) / 2);
                setZoomWindow(tls, brs);
            }
        }

        /**
         * Bogus method to satisfy MouseListener.
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
        }

        /**
         * Bogus method to satisfy MouseListener.
         * @param e
         */
        public void mouseExited(MouseEvent e) {
        }

        /**
         * Bogus method to satisfy MouseListener.
         * @param e
         */
        public void mouseEntered(MouseEvent e) {
        }
    }
}
