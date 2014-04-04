/*
 * Name: Ali Kocaturk
 * Teacher: Ms. Gallatin
 * Period: 2
 * MandelViewer.java
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 * JFrame containing a Mandelbrot fractal and its associated menus.
 * @author ali
 */
public class MandelViewer extends JFrame {

    private String customExpression = "z re abs i z im abs * + 2 ^ c +";
    private Mandelbrot fractal;
    private JMenuItem juliaButton;
    private JMenuItem customPaletteButton;
    private ColorPalette colorpalette;
    private FormulaContainer frmengine;
    private JRadioButtonMenuItem formmndlbtn;
    private JRadioButtonMenuItem formshipbtn;
    private JRadioButtonMenuItem formcstmbtn;
    private JMenuItem bailoutButton;
    private JMenuItem iterationButton;
    private JMenuItem repaintButton;
    private JMenuItem loadZmSqButton;
    private JMenuItem saveZmSqButton;
    private JMenuItem saveZoomButton;

    /**
     * Sets up the JFrame and puts a <code>Mandelbrot</code> object in its
     * center.
     */
    public MandelViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 275);
        setTitle("Mandolin");

        // LAYOUT AND FRACTAL WINDOW
        colorpalette = new ColorPalette();
        colorpalette.setDefaultPalette();
        frmengine = new FormulaContainer(new Quadratic());
        fractal = new Mandelbrot(colorpalette, frmengine);
        fractal.setToolTipText("Drag down and to the left to zoom in, any other direction to zoom out.  " +
                "Click to set Julia c-value.");
        add(fractal);

        JMenuBar menubar = new JMenuBar();
        // MENUS
        // File
        FileMenuListener fml = new FileMenuListener();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menubar.add(fileMenu);
        fileMenu.add(loadZmSqButton = new JMenuItem("Load zoom/window"));
        loadZmSqButton.setMnemonic(KeyEvent.VK_O);
        loadZmSqButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                ActionEvent.CTRL_MASK));
        loadZmSqButton.setToolTipText("Play back a zoom sequence or window");
        loadZmSqButton.addActionListener(fml);
        fileMenu.add(saveZoomButton = new JMenuItem("Save window"));
        saveZoomButton.setMnemonic(KeyEvent.VK_S);
        saveZoomButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));
        saveZoomButton.setToolTipText("Save the current view");
        saveZoomButton.addActionListener(fml);
        fileMenu.add(saveZmSqButton = new JMenuItem("Save zoom sequence"));
        saveZmSqButton.setMnemonic(KeyEvent.VK_O);
        saveZmSqButton.setToolTipText("Save the history of zoom windows");
        saveZmSqButton.addActionListener(fml);


        // View
        ViewMenuListener vml = new ViewMenuListener();
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menubar.add(viewMenu);
        juliaButton = new JMenuItem("Julia window");
        juliaButton.setMnemonic(KeyEvent.VK_J);
        juliaButton.setToolTipText("Show the Julia window");
        juliaButton.addActionListener(vml);
        viewMenu.add(juliaButton);
        customPaletteButton = new JMenuItem("Edit palette");
        customPaletteButton.setMnemonic(KeyEvent.VK_P);
        customPaletteButton.setToolTipText("Edit the color palette");
        customPaletteButton.addActionListener(vml);
        viewMenu.add(customPaletteButton);
        repaintButton = new JMenuItem("Repaint");
        repaintButton.setMnemonic(KeyEvent.VK_R);
        repaintButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                ActionEvent.CTRL_MASK));
        repaintButton.setToolTipText("Repaints the fractal window");
        repaintButton.addActionListener(vml);
        viewMenu.add(repaintButton);

        // Calculation
        JMenu calcMenu = new JMenu("Calculation");
        calcMenu.setMnemonic(KeyEvent.VK_C);
        menubar.add(calcMenu);
        //Formulas
        JMenu formulaMenu = new JMenu("Formula");
        formulaMenu.setMnemonic(KeyEvent.VK_F);
        formulaMenu.setToolTipText("Choose a formula");
        ButtonGroup formulabuttons = new ButtonGroup();
        FormulaChoiceListener frmlistener = new FormulaChoiceListener();
        formulabuttons.add(formmndlbtn = new JRadioButtonMenuItem("Mandelbrot"));
        formmndlbtn.setMnemonic(KeyEvent.VK_M);
        formmndlbtn.setToolTipText("z² + c");
        formmndlbtn.setSelected(true);
        formulaMenu.add(formmndlbtn);
        formmndlbtn.addActionListener(frmlistener);
        formulabuttons.add(formshipbtn = new JRadioButtonMenuItem("Burning Ship"));
        formshipbtn.setMnemonic(KeyEvent.VK_S);
        formshipbtn.setToolTipText("(|Re(z)| + i|Im(z)|)² + c");
        formulaMenu.add(formshipbtn);
        formshipbtn.addActionListener(frmlistener);
        formulabuttons.add(formcstmbtn = new JRadioButtonMenuItem("Custom"));
        formcstmbtn.setMnemonic(KeyEvent.VK_C);
        formcstmbtn.setToolTipText("Specify a formula in postfix notation");
        formulaMenu.add(formcstmbtn);
        formcstmbtn.addActionListener(frmlistener);
        calcMenu.add(formulaMenu);
        //Bailout & Iteration Adjusters
        calcMenu.add(bailoutButton = new JMenuItem("Bailout"));
        bailoutButton.setMnemonic(KeyEvent.VK_B);
        bailoutButton.setToolTipText("Adjust the maximum distance from the origin");
        bailoutButton.addActionListener(new CalculationMenuListener());
        calcMenu.add(iterationButton = new JMenuItem("Iterations"));
        iterationButton.setMnemonic(KeyEvent.VK_I);
        iterationButton.setToolTipText("Adjust the maximum number of iterations");
        iterationButton.addActionListener(new CalculationMenuListener());

        setJMenuBar(menubar);

        setVisible(true);
    }

    /**
     * Handles formula selection
     */
    private class FormulaChoiceListener implements ActionListener {

        /**
         * Handles formula selection
         * @param e 
         */
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            Formula newFormula = new Quadratic();
            if (source == formshipbtn) {
                newFormula = new BurningShip();
            } else if (source == formcstmbtn) {
                String tce = (String) (JOptionPane.showInputDialog(MandelViewer.this,
                        "Custom formula (in postfix notation):", customExpression));
                if (tce != null) {
                    customExpression = tce;
                }
                try {
                    newFormula = new CustomFormula(customExpression);
                } catch (IllegalArgumentException frmexc) {
                    JOptionPane.showMessageDialog(MandelViewer.this,
                            "Expression interpretation error:\n" + frmexc.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    formmndlbtn.setSelected(true);
                }
            }
            frmengine.setFormula(newFormula);
            fractal.repaint();
        }
    }

    /**
     * Handles clicks in the View menu
     */
    private class ViewMenuListener implements ActionListener {

        /**
         * Handles clicks in the View menu
         * @param e 
         */
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == juliaButton) {
                fractal.showJulia();
            } else if (source == repaintButton) {
                fractal.repaint();
            } else if (source == customPaletteButton) {
                PaletteEditor unusedretval = new PaletteEditor(colorpalette);
            }
        }
    }

    /**
     * Handles clicks in the Calculation menu (excluding Formula)
     */
    private class CalculationMenuListener implements ActionListener {

        /**
         * Handles clicks in the Calculation menu (excluding Formula)
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bailoutButton) {
                String bloStr = JOptionPane.showInputDialog(MandelViewer.this,
                        "Bailout value", String.valueOf(fractal.getBailout()));
                if (bloStr != null) {
                    try {
                        double td = Double.parseDouble(bloStr);
                        fractal.setBailout(td);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "NumberFormatException - must be Double\n" + nfe.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (e.getSource() == iterationButton) {
                String bloStr = JOptionPane.showInputDialog(MandelViewer.this,
                        "Maximum iterations", String.valueOf(fractal.getIter()));
                if (bloStr != null) {
                    try {
                        int ti = Integer.parseInt(bloStr);
                        fractal.setIter(ti);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "NumberFormatException - must be Integer\n" + nfe.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Handles clicks in the File menu
     */
    private class FileMenuListener implements ActionListener {

        /**
         * Handles clicks in the File menu
         */
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == loadZmSqButton) {
                JFileChooser fch = new JFileChooser();
                int fchRetVal = fch.showOpenDialog(MandelViewer.this);
                if (fchRetVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        int delay = Integer.parseInt(
                                JOptionPane.showInputDialog(MandelViewer.this,
                                "Delay between zooms (in seconds): ", "0"));
                        formcstmbtn.setSelected(true);
                        ZoomJockey.runZoomSequence(fractal, fch.getSelectedFile(), delay);
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "FileNotFoundException\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "IOException\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "ClassNotFoundException - Not a zoom sequence?\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (source == saveZmSqButton) {
                JFileChooser fch = new JFileChooser();
                if (fch.showSaveDialog(MandelViewer.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        ZoomJockey.saveZoomSequence(fractal, fch.getSelectedFile());
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "FileNotFoundException\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "IOException\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (source == saveZoomButton) {
                JFileChooser fch = new JFileChooser();
                if (fch.showSaveDialog(MandelViewer.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        ZoomJockey.saveZoomWindow(fractal, fch.getSelectedFile());
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "FileNotFoundException\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(MandelViewer.this,
                                "IOException\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}
