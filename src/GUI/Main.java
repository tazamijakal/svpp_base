package GUI;

import Logik.Spieler;

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
        int[] sp = {0,0,1,1,0,0,0};
        Spieler player = new Spieler("Server", 5, 8, sp);
        //SpielStart.SpielStarten(player);
        //SwingUtilities.invokeLater(() -> {SpielStart.SpielStarten(player);});
        SwingUtilities.invokeLater(() -> {Startbildschirm.start();});
    }
}
