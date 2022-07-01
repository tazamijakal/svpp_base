package ladenspeichern;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.io.Serializable;

public class Speichern implements Serializable {

    AllWeNeed savefile;

    public static void save(AllWeNeed data, String id) throws IOException {
        if(data.player.name.equals("Server") || data.player.name.equals("Client")){

        }
        //JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        //Filter fuer Text Dateien
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "*.txt", "txt", "text");

        chooser.setFileFilter(filter);

        if(data.player.name.equals("Server") || data.player.name.equals("Client") || data.player.name.equals("Spieler1") || data.player.name.equals("Spieler2")){
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
        }

        //Pickup for selected file
        File savedFile = new File(chooser.getSelectedFile() + id + ".txt");
        FileOutputStream fileoutput;
        if(savedFile.equals("")){
            fileoutput = new FileOutputStream(id + ".txt");
        }
        else{
            fileoutput = new FileOutputStream(savedFile);
        }
        ObjectOutputStream objectoutput = new ObjectOutputStream(fileoutput);
        objectoutput.writeObject(data);                 //Object AllWeNeed reinschreiben
        objectoutput.flush(); //Eventuell nicht noetig
        objectoutput.close();
    }

    /*public static void main(String[] args) {
        //save();
    }*/

}
