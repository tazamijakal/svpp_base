import Logik.*;
import Socket.*;
import GUI.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int[] ships = {5,10,5,3};
        SchiffeV gui = new SchiffeV();
        SwingUtilities.invokeLater(
                () -> { SchiffeV.start(); }
        );
        Spieler player1 = new Spieler("player1", 15, 75, ships);
        Spieler player2 = new Spieler("player2", 15, 75, ships);
        Server server = new Server(50000, 1, player1, player2);
        Client client = new Client(50000, "localhost", player1, player2);
    }
}


