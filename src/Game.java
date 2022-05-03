import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    public int mapSize;
    public int minCapacity;
    public int hp = 0;
    public int[] availableShips;
    public int[] smallFieldAvailableShips = {2, 3, 4, 5};
    public int[] bigFieldAvailableShips = {3, 4, 5, 6};
    public int[][] shipPool = { {0, 1, 2, 3, 4, 5, 6},
                                {0, 0, 0, 0, 0, 0, 0}};

    public Game() {
        System.out.println("Spiel wird vorbereitet..");
    }

    Scanner userInput = new Scanner(System.in);  // Create a Scanner object

    /**
     * Bereitet das Spiel vor (Spielfeldgroesse und festlegen, Spieler und deren Spielfelder erzeugen, Schiffe platzieren und Spiel mit startWar() starten.
     */
    public void startGame() {
        requestMapSize();
        requestShipTypes();
        System.out.println("Name fuer Spieler 1: ..");
        String player1name = userInput.next();
        System.out.println("Name fuer Spieler 2: ..");
        String player2name = userInput.next();
        Spieler spieler1 = new Spieler(player1name, mapSize, hp, shipPool);
        Spieler spieler2 = new Spieler(player2name, mapSize, hp, shipPool);
        spieler1.cPrintBoth(spieler1, spieler2);
        spieler1.showRemainingShips();
        startPlacingShips(spieler1, spieler2);
        spieler2.showRemainingShips();
        startPlacingShips(spieler2, spieler1);
        startWar(spieler1, spieler2);
    }

    /**
     * Laesst beide Spieler abwechselnd aufeinander schiessen bis ein Spieler gewinnt oder es zum Unentschieden kommt.
     *
     * @param player1
     * @param player2
     */
    public void startWar(Spieler player1, Spieler player2) {
        player1.hp = minCapacity;              //hier bezeichnet hp die Anzahl an Feldern auf welchen noch Schiffe liegen
        player2.hp = minCapacity;               //TODO HP FALSCH
        while (player1.hp > 0 && player2.hp > 0) {
            player1.cPrintBoth(player1, player2);
            System.out.println("Zielkoordinaten eingeben " + player1.name);
            player2.shootrequest(player2, player1);
            player1.cPrintBoth(player1, player2);
            System.out.println("Zielkoordinaten eingeben " + player2.name);
            player1.shootrequest(player1, player2);
        }
        if (player1.hp == player2.hp) {
            System.out.println("Unentschieden!");
        } else if (player1.hp < player2.hp) {
            System.out.println(player2.name + " hat gewonnen!");
        } else {
            System.out.println(player1.name + " hat gewonnen!");
        }
        System.out.println("GG WP");
    }

    /**
     * Fraegt Spieler nach der gewuenschten Spielfeldgroesse, berechnet Mindestkapazität, und schliesst Schiffslaengen aus, welche nicht regelkonform sind.
     */
    public void requestMapSize() {
        System.out.println("Spielfeldgroesse zwischen 5 und 30 auswaehlen:");
        mapSize = userInput.nextInt();
        if (mapSize < 31 && mapSize > 4) {
            minCapacity = (int) ((double) mapSize * mapSize / 100 * 30);            //nicht unbedingt genau 30% da es nur ganze Felder gibt
            String netSize = String.format("size %d", mapSize);                     //TODO für netzwerk
            if (mapSize < 19)
                availableShips = smallFieldAvailableShips;           //gibt die moeglichen Schiffslaengen vor
            else availableShips = bigFieldAvailableShips;
        } else if (mapSize > 30) {
            System.out.println("Maximale Spielfeldgroesse ueberschritten");
            requestMapSize();
        } else {
            System.out.println("Spielfeld zu klein!");
            requestMapSize();
        }
    }

    /**
     * Legt Spielfeldgroesse fest, berechnet Mindestkapazität, und schliesst Schiffslaengen aus, welche nicht regelkonform sind.
     *
     * @param size  size*size = laenge*breite
     */
    public void requestMapSize(int size) {
        mapSize = size;
        minCapacity = (int) ((double) mapSize * mapSize / 100 * 30);
        if (mapSize < 19)
            availableShips = smallFieldAvailableShips;           //gibt die moeglichen Schiffslaengen vor
        else availableShips = bigFieldAvailableShips;
    }

    public void requestShipTypes() {
        int whatDo;             //Schiff hinzufügen, entfernen, oder Auswahl beenden
        int shipType;           //2er, 3er, 4er, .. 6er-Schiff
        while (true) {
            while (true) {
                System.out.println("Mindestkapazitaet: "+minCapacity+"\tAktuelle Kapazitaet: "+hp);
                System.out.println("Schiffstyp hinzufuegen(1), entfernen(2) oder Auswahl beenden(3)?");
                whatDo = userInput.nextInt();
                if (whatDo > 0 && whatDo < 4) {
                    break;
                } else {
                    System.out.println(whatDo + " ist keine mögliche Aktion!\n");
                }
            }
            if (whatDo == 3) {
                System.out.println(whatDo + ": Auswahl beenden..");
                break;
            } else if (whatDo == 2) {
                while (true) {
                    System.out.println("Von welchem Schiffstyp (Laenge) sollen Schiffe entfernt werden?");      //TODO mögliche Schiffe auflisten
                    shipType = userInput.nextInt();
                    if (isInArray(availableShips, shipType)) {
                        adjustShipPool(false, shipType);
                        break;
                    } else {
                        System.out.println("Keine legitime Schiffslaenge!\n");
                    }
                }
            } else if (whatDo == 1) {
                while (true) {
                    System.out.println("Von welchem Schiffstyp (Laenge) sollen Schiffe hinzugefuegt werden?");
                    shipType = userInput.nextInt();
                        if (isInArray(availableShips, shipType)) {
                            adjustShipPool(true, shipType);
                            break;
                        } else {
                        System.out.println("Keine legitime Schiffslaenge!\n");
                    }
                }
            }
        }
        if (hp > minCapacity) {
            System.out.println("Mindestkapazität erfüllt!");
            for (int i = 0; i < shipPool[0].length; i++) {
                System.out.println(shipPool[1][i] + "x " + shipPool[0][i] + "-er Schiff");              //TODO nur mögliche Schiffstypen auflisten
            }
        } else {
            System.out.println("Mindestkapazität nicht erfüllt, bitte weitere Schiffe auswählen!");
            for (int i = 0; i < shipPool[0].length; i++) {
                System.out.println(shipPool[1][i] + "x " + shipPool[0][i] + "-er Schiff");              //TODO nur mögliche Schiffstypen auflisten
            }
            requestShipTypes();
        }
    }

    public void adjustShipPool(boolean addRemoveToggle, int size) {
        if (addRemoveToggle) {
            shipPool[1][size]++;
            hp += size;
        } else {
            shipPool[1][size]--;
            hp -= size;
        }
    }

    /**
     * Fordert Spieler so lange auf Schiffe zu setzen bis Kapazitaeten aufgebraucht sind.
     *
     * @param player1
     * @param player2
     */
    //TODO removeShip iwie integrieren
    public void startPlacingShips(Spieler player1, Spieler player2) {
        System.out.println("Platziere deine Schiffe " + player1.name + ": ..");
        while (player1.hp > 0) {
            player1.placeShipRequest();
            player1.cPrintBoth(player1, player2);
        }
        System.out.println("Folgende Schiffe wurden platziert:");
        player1.printShipList();
        player1.cPrintBoth(player1, player2);
    }


    public boolean isInArray(int[] array, int value) {
        for (int i=0; i<availableShips.length; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }

    public boolean isInMatrix(int[][]array, int value){
        for (int i=0; i<array.length; i++) {
            if (array[1][i] == value) {
                return true;
            }
        }
        return false;
    }
}