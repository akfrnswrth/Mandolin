

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The Julia window
 * @author ali
 */
public class JuliaViewer extends JFrame {

    private Julia fractal;
    private JSlider iterations;
    private JLabel iterstat;

    /**
     * Constructs (but doesn't show) the JuliaViewer
     * @param cp the color palette to use
     * @param frmengine the FormulaContainer containing the formula to use
     */
    public JuliaViewer(ColorPalette cp, FormulaContainer frmengine) {
        super();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(300, 364);
        setTitle("Julia");
        fractal = new Julia(cp, frmengine);
        setLayout(new BorderLayout());
        add(fractal, BorderLayout.CENTER);
        addWindowListener(new JuliaWindowListener());
        fractal.setToolTipText("Drag down and to the left to zoom in, any other "
                + "direction to zoom out.");


        JPanel adjusters = new JPanel();
        adjusters.setLayout(new BoxLayout(adjusters, BoxLayout.Y_AXIS));

        iterstat = new JLabel(((Integer) fractal.getIter()).toString()
                + " iterations");
        iterstat.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        adjusters.add(iterstat);

        iterations = new JSlider(JSlider.HORIZONTAL, 0, 512, fractal.getIter());
        iterations.setMinorTickSpacing(8);
        iterations.setMajorTickSpacing(64);
        iterations.setPaintLabels(true);
        iterations.setPaintTicks(true);
        adjusters.add(iterations);
        iterations.addChangeListener(new itSliderListener());
        iterations.setToolTipText("Drag to adjust the maximum number of iterations");

        add(adjusters, BorderLayout.NORTH);

        // setVisible(true);
    }

    /**
     * Sets the C-value to use in calculation
     * @param c 
     */
    public void setC(Complex c) {
        if (isVisible()) {
            fractal.setC(c);
            setTitle("Julia " + c);
        }
    }

    /**
     * Listens for adjustment of the number of iterations
     */
    private class itSliderListener implements ChangeListener {

        /**
         * Sets number of iterations of <ode>fractal</code> then sets
         * the <code>iterstat</code> label.
         * @param e
         */
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                fractal.setIter(source.getValue());
            }
            iterstat.setText(((Integer) source.getValue()).toString()
                    + " iterations");
        }
    }

    /**
     * Handles closing of the window by disabling (not destroying) it.
     */
    private class JuliaWindowListener implements WindowListener {

        /**
         * Bogus method to satisfy WindowListener
         * @param e 
         */
        public void windowActivated(WindowEvent e) {
        }

        /**
         * Bogus method to satisfy WindowListener
         * @param e 
         */
        public void windowOpened(WindowEvent e) {
        }

        /**
         * Hides/disables the window when the close button is pressed
         * @param e 
         */
        public void windowClosing(WindowEvent e) {
            setVisible(false);
        }

        /**
         * Bogus method to satisfy WindowListener
         * @param e 
         */
        public void windowClosed(WindowEvent e) {
        }

        /**
         * Bogus method to satisfy WindowListener
         * @param e 
         */
        public void windowIconified(WindowEvent e) {
        }

        /**
         * Bogus method to satisfy WindowListener
         * @param e 
         */
        public void windowDeiconified(WindowEvent e) {
        }

        /**
         * Bogus method to satisfy WindowListener
         * @param e 
         */
        public void windowDeactivated(WindowEvent e) {
        }
    }
}
