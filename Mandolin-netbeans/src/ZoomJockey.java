

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Used by MandelViewer for Zoom/Window save and load
 * @author ali.kocaturk.1
 */
public class ZoomJockey {

    /**
     * Saves the zoom history and calculation settings from the given Mandelbrot
     * to to given File.
     * @param horse the source Mandelbrot
     * @param file the destination file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void saveZoomSequence(Mandelbrot horse, File file)
            throws FileNotFoundException, IOException {
        Stack<ZoomWindow> tzsq_lifo = horse.getZoomSequence();


        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(tzsq_lifo);
        oos.writeObject(horse.getFormulaContainer());
        oos.writeInt(horse.getIter());
        oos.writeDouble(horse.getBailout());
        oos.close();
    }

    /**
     * Replays the zoom window/history from the given File on the given
     * Mandelbrot
     * @param horse the playback Mandelbrot
     * @param file the file to read from
     * @param delay the delay (in seconds) between zooms
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings(value = "unchecked")
    public static void runZoomSequence(Mandelbrot horse, File file, int delay)
            throws FileNotFoundException, IOException, ClassNotFoundException  {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Stack<ZoomWindow> tzsq_lifo = (Stack<ZoomWindow>) (ois.readObject());
        horse.getFormulaContainer().setFormula(((FormulaContainer) (ois.readObject())).getFormula());
        horse.setIter(ois.readInt());
        horse.setBailout(ois.readDouble());
        ois.close();

        try {
            for (ZoomWindow c : tzsq_lifo) {
                horse.setZoomWindow(c);
                horse.paintComponent(horse.getGraphics());  // dumb hack to force repaint ASAP
                Thread.sleep(1000 * delay);
            }
        } catch (InterruptedException iex) {
            return;
        }
    }

    /**
     * Saves the current zoom window and calculation settings from the given
     * Mandelbrot to the given file.
     * @param horse Mandelbrot to receive settings from
     * @param file File to save to
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void saveZoomWindow(Mandelbrot horse, File file) throws FileNotFoundException, IOException {
        Stack<ZoomWindow> tzsq = new Stack<ZoomWindow>();
        tzsq.push(horse.getZoomWindow());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(tzsq);
        oos.writeObject(horse.getFormulaContainer());
        oos.writeInt(horse.getIter());
        oos.writeDouble(horse.getBailout());
        oos.close();
    }
}
