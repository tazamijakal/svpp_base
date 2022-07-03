package ladenspeichern;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 *
 * Klasse um Spielstand zu laden aus txt File
 *
 */
public class Laden implements Serializable{
    public static AllWeNeed load() throws IOException, ClassNotFoundException {
        //JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();

        //Filter fuer Text Dateien
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "*.txt", "txt", "text");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        //Dialog zum Oeffnen von Dateien anzeigen
        int click = chooser.showOpenDialog(null);   //Open fuer oeffnen / Save fuer speichern

        //Abfrage ob auf "Oeffnen" geklickt wurde oder "cancel"
        if(click == JFileChooser.APPROVE_OPTION){
            //Ausgabe der ausgewaehlten Datei (Name)
            System.out.println("Datei: " + chooser.getSelectedFile().getName());
        }
        else{
            //Abbrechen?!
        }
        //Pickup for selected file
        File selectedFile = chooser.getSelectedFile();
        if(selectedFile == null){
            //Exception?
        }
        else{
            System.out.println("Selected file: " + selectedFile.getAbsolutePath()); //path fuer Datei ausgeben
            FileInputStream fileinput = new FileInputStream(selectedFile);      //FileNotFound exception wenn file == null
            ObjectInputStream objectinput = new ObjectInputStream(fileinput);   //IO-Exception
            AllWeNeed data = (AllWeNeed) objectinput.readObject();              //ClassNotFound Exception
            objectinput.close();
            return data;
        }
        return null;
    }
}
