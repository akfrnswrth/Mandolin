

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author ali
 */
public class PaletteEditor extends JFrame {

    private ColorPalette palette;
    private PaletteBrick pvwr;
    private JPanel editorPanel;
    private JSpinner pointChooser;
    private SpinnerNumberModel pcrmodel;
    private JButton colorButton;
    
    /**
     * Constructs a new PaletteEditor using/editing the given ColorPalette
     * @param palette 
     */
    public PaletteEditor(ColorPalette palette) {
        super();
        setSize(300, 100);
        setTitle("Palette Editor");
        setLayout(new BorderLayout());
        this.palette = palette;
        
        pvwr = new PaletteBrick();
        add(pvwr, BorderLayout.SOUTH);
        
        editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(1,2, 5, 5));
        add(editorPanel, BorderLayout.CENTER);
        
        colorButton = new JButton("Color");
        colorButton.addActionListener(new ebcActionListener());
        
        pointChooser = new JSpinner(pcrmodel = new SpinnerNumberModel(1, 1, 
                palette.getNumPoints(), 1));
        colorButton.setBackground(new Color(palette.getPoint(pcrmodel.getNumber().intValue())));
        pointChooser.addChangeListener(new pcrChangeListener());
        editorPanel.add(pointChooser);
        editorPanel.add(colorButton);
        
        setVisible(true);
    }
    
    /**
     * Component showing a preview of the ColorPalette in the form of a gradient
     */
    private class PaletteBrick extends JLabel {
        
        /**
         * Constructs a new PaletteBrick
         */
        public PaletteBrick() {
            super();
            setText("Preview");
            setHorizontalTextPosition(CENTER);
        }
        
        
        /**
         * Paints the gradient and then superimposes the JLabel text
         * @param g 
         */
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            BufferedImage bui = new BufferedImage(getWidth(), getHeight(), 
                    BufferedImage.TYPE_INT_RGB);
            for(int y = 0; y < bui.getHeight(); y++) {
                for(int x = 0; x < bui.getWidth(); x++) {
                    bui.setRGB(x, y, palette.toRGB(x * 255 / bui.getWidth()));
                }
            }
            g2.drawImage(bui, 0, 0, this);
            super.paintComponent(g);
        }
    }

    
    /**
     * Handles setpoint selection
     */
    private class pcrChangeListener implements ChangeListener {
        
        /**
         * Handles setpoint selection
         * @param e 
         */
        public void stateChanged(ChangeEvent e) {
            colorButton.setBackground(new Color(palette.getPoint(pcrmodel.getNumber().intValue())));
        }
    }
    
    /**
     * Handles presses on the Color button
     */
    private class ebcActionListener implements ActionListener {
        
        /**
         * Pops up a color chooser and handles its choice
         * @param e 
         */
        public void actionPerformed(ActionEvent e) {
            int ptidx = pcrmodel.getNumber().intValue();
            Color tc = JColorChooser.showDialog(PaletteEditor.this, 
                    "Choose for point " + ptidx, colorButton.getBackground());
            if(tc != null) {
                colorButton.setBackground(tc);
                palette.setPoint(ptidx, tc.getRGB());
                pvwr.repaint();
            }
        }
    }
}
