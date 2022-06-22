import GUI.Startbildschirm;
import Logik.Game2KI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Laut Swing-Dokumentation sollte die graphische Oberfläche
        // nicht direkt im Hauptprogramm (bzw. im Haupt-Thread) erzeugt
        // und angezeigt werden, sondern in einem von Swing verwalteten
        // separaten Thread.
        // Hierfür wird der entsprechende Code in eine parameterlose
        // anonyme Funktion () -> { ...... } "verpackt", die an
        // SwingUtilities.invokeLater übergeben wird.
       /* Game2KI game = new Game2KI();
        game.startGame();     //Mapgröße, Schiffstypen und Positionierung werden vor Spielstart abgefragt
//        game.demoGame();        /5/5x5 game mit bereits gesetzten Schiffen*/
        SwingUtilities.invokeLater(() -> {Startbildschirm.start();});
    }
}


