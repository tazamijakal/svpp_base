package GUI;

import java.awt.*;
import javax.swing.*;


/**
 *
 * Hilfsklasse zum dynamischen resizing von images.
 *
 */
public class ImageViewer extends JPanel {

    private java.awt.Image image;
    private int xCoordinate;
    private int yCoordinate;
    private boolean stretched = true;

    /**
     * Konstruktor setzt Image Attribut
     * @param image Image
     */
    public ImageViewer(Image image) {
        this.image = image;
    }


    /**
     * Methode zeichnet image to width und height von panel damit es spaeter automatisch Groesse aendert.
     * @param g Graphics Component
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            if (isStretched()) {
                g.drawImage(image, xCoordinate, yCoordinate,
                        getSize().width, getSize().height, this);
            } else {
                g.drawImage(image, xCoordinate, yCoordinate, this);
            }
        }
    }


    /**
     * Methode liefert zurueck ob isStreched
     * @return boolean
     */
    public boolean isStretched() {
        return stretched;
    }


    /*public java.awt.Image getImage() {
        return image;
    }
    public void setImage(java.awt.Image image) {
        this.image = image;
        repaint();
    }
    public void setStretched(boolean stretched) {
        this.stretched = stretched;
        repaint();
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
        repaint();
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
        repaint();
    }*/
}
