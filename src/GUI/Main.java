package GUI;
import javax.swing.*;

public class Main {

    // Hauptprogramm.
    public static void main (String [] args) {
        // Laut Swing-Dokumentation sollte die graphische Oberfläche
        // nicht direkt im Hauptprogramm (bzw. im Haupt-Thread) erzeugt
        // und angezeigt werden, sondern in einem von Swing verwalteten
        // separaten Thread.
        // Hierfür wird der entsprechende Code in eine parameterlose
        // anonyme Funktion () -> { ...... } "verpackt", die an
        // SwingUtilities.invokeLater übergeben wird.
        SwingUtilities.invokeLater(
                //Startbildschirm wird angezeitgt
                () -> { Startbildschirm.start(); }
        );
    }
}
