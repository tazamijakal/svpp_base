package ladenspeichern;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class speichern {

    public static void save(AllWeNeed data) throws IOException {
        //JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        //Filter fuer Text Dateien
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "*.txt", "txt", "text");
        chooser.setFileFilter(filter);

        //Dialog zum Oeffnen von Dateien anzeigen
        int click = chooser.showSaveDialog(null);   //Open fuer oeffnen / Save fuer speichern

        //Abfrage ob auf "Speichern" geklickt wurde oder "cancel"
        if(click == JFileChooser.APPROVE_OPTION){
            //Ausgabe der ausgewaehlten Datei
            System.out.println("Datei: " + chooser.getSelectedFile().getName());
        }
        else{
            //Abbrechen?!
        }

        //Pickup for selected file
        File savedFile = chooser.getSelectedFile();
        if(savedFile != null){
            System.out.println("Selected file: " + savedFile.getAbsolutePath()); //path fuer Datei ausgeben
        }
        FileOutputStream fileoutput = new FileOutputStream(savedFile);
        ObjectOutputStream objectoutput = new ObjectOutputStream(fileoutput);
        objectoutput.writeObject(data);                 //Object AllWeNeed reinschreiben
        objectoutput.flush(); //Eventuell nicht noetig
        objectoutput.close();
    }

    /*public static void main(String[] args) {
        //save();
    }*/

}
