package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;


/**
 * Die Hilfsklasse MyImageCellRenderer wird benutzt um im JTable nur Images / ImageIcons zu verwenden.
 */
public class MyImageCellRenderer extends DefaultTableCellRenderer {

    /**
     * Methode die ImageViewer mit DefaultTableCellRenderer verknuepft
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
        try{
            Image image = ((ImageIcon) value).getImage();
            ImageViewer imageViewer = new ImageViewer(image);
            return imageViewer;
        }
        catch(Exception e){

        }
        return null;
    }
}
