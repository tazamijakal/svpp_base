import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    public int mapSize; /** test */
    public int minCapacity;
    public int hp = 0;
    public int[] availableShips;
    public int[] smallFieldAvailableShips = {2, 3, 4, 5};
    public int[] bigFieldAvailableShips = {3, 4, 5, 6};
    public int[] shipPool = {0, 0, 0, 0, 0, 0, 0};

    public Game() {
        System.out.println("Spiel wird vorbereitet..");
    }

    Scanner userInput = new Scanner(System.in);  // Create a Scanner object

    /**
     * Bereitet das Spiel vor (Spielfeldgröße und festlegen, Spieler und deren Spielfelder erzeugen, Schiffe platzieren und Spiel mit startWar() starten.
     */
    public void startGame() {
        requestMapSize();
        requestShipTypes();
        System.out.println("Name für Spieler 1: ..");
        String player1name = userInput.next();
        System.out.println("Name für Spieler 2: ..");
        String player2name = userInput.next();
        Spieler spieler1 = new Spieler(player1name, mapSize, hp, shipPool);
        Spieler spieler2 = new Spieler(player2name, mapSize, hp, shipPool);
        spieler1.printAll(spieler1, spieler2);
        startPlacingShips(spieler1, spieler2);
        startPlacingShips(spieler2, spieler1);
        startWar(spieler1, spieler2);
    }

    public void demoGame() {
        mapSize = 5;
        minCapacity = (int) ((double) mapSize * mapSize / 100 * 30);
        hp = 7;
        adjustShipPool(true, 4);
        adjustShipPool(true, 3);
        Spieler spieler1 = new Spieler("RECHTS", mapSize, hp, shipPool);
        Spieler spieler2 = new Spieler("LINKS", mapSize, hp, shipPool);
        spieler1.printAll(spieler1, spieler2);
        spieler1.manualShipPlacement(1,1,6,4);
        spieler1.manualShipPlacement(1,3,6,3);
        spieler2.manualShipPlacement(5,5,4,4);
        spieler2.manualShipPlacement(5,3,4,3);
        spieler1.radar(4,4,spieler1,spieler2);
        System.out.println("Radar map for Player 1 at R5|5: "+spieler1.radarMap[4][4]);
        spieler1.printAll(spieler1, spieler2);
//        gameCoordinator(spieler1, spieler2);
        startWar(spieler1, spieler2);
    }

    /**
     * Lässt beide Spieler abwechselnd aufeinander schießen bis ein Spieler gewinnt oder es zum Unentschieden kommt.
     *
     * @param player1
     * @param player2
     */
    public void startWar(Spieler player1, Spieler player2) {
        player1.hp = minCapacity;              //hier bezeichnet hp die Anzahl an Feldern auf welchen noch Schiffe liegen
        player2.hp = minCapacity;
        while (player1.hp > 0 && player2.hp > 0) {
            player1.printAll(player1, player2);
            System.out.println("Zielkoordinaten eingeben " + player1.name);
            player2.shootrequest(player1, player2);
            player1.printAll(player1, player2);
            System.out.println("Zielkoordinaten eingeben " + player2.name);
            player1.shootrequest(player2, player1);
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


    public void gameCoordinator(Spieler spieler1, Spieler spieler2){
        spieler1.hp = minCapacity;              //hier bezeichnet hp die Anzahl an Feldern auf welchen noch Schiffe liegen
        spieler2.hp = minCapacity;
        int whatDo;
        Spieler attacker = spieler1;
        Spieler defender = spieler2;
        while (spieler1.hp > 0 && spieler2.hp > 0) {
            if (attacker == spieler1) {
                attacker = spieler2;
                defender = spieler1;
            } else if (attacker == spieler2) {
                attacker = spieler1;
                defender = spieler2;
            }
            attacker.printAll(attacker, defender);
            while (true) {
                System.out.println("Was willst du tun, "+attacker.name+": Schießen(1)\tRadar(2)\tAirstrike(not implemented)");
                whatDo = userInput.nextInt();
                if (whatDo == 1) {
                    attacker.shootrequest(attacker, defender);
                    break;
                } else if (whatDo == 2) {
                    attacker.radarRequest(attacker, defender);
                    break;
                } else {
                    System.err.print("Keine mögliche Option!");
                }
            }
        }
        System.out.println("Spiel vorbei.");
    }

    /**
     * Frägt Spieler nach der gewünschten Spielfeldgröße, berechnet Kapazität, und schließt Schiffslängen aus, welche nicht regelkonform sind.
     */
    public void requestMapSize() {
        System.out.println("Spielfeldgröße zwischen 5 und 30 auswählen:");
        mapSize = userInput.nextInt();
        if (mapSize < 31 && mapSize > 4) {
            minCapacity = (int) ((double) mapSize * mapSize / 100 * 30);            //nicht unbedingt genau 30% da es nur ganze Felder gibt
            String netSize = String.format("size %d", mapSize);                     //todo netzwerk
            if (mapSize < 19)
                availableShips = smallFieldAvailableShips;           //gibt die möglichen Schiffslängen vor
            else availableShips = bigFieldAvailableShips;
        } else if (mapSize > 30) {
            System.out.println("Maximale Spielfeldgröße überschritten");
            requestMapSize();
        } else {
            System.out.println("Spielfeld zu klein!");
            requestMapSize();
        }
    }

    /**
     * Legt Spielfeldgröße fest, berechnet Mindestkapazität, und schließt Schiffslängen aus, welche nicht regelkonform sind.
     *
     * @param size  size*size = länge*breite
     */
    public void requestMapSize(int size) {
        mapSize = size;
        minCapacity = (int) ((double) mapSize * mapSize / 100 * 30);
        if (mapSize < 19)
            availableShips = smallFieldAvailableShips;           //gibt die möglichen Schiffslängen vor
        else availableShips = bigFieldAvailableShips;
    }

    public void requestShipTypes() {
        int whatDo;             //Schiff hinzufügen, entfernen, oder Auswahl beenden
        int shipType;           //2er, 3er, 4er, .. 6er-Schiff
        while (true) {
            while (true) {
                System.out.println("Erforderliche Kapazität: " + minCapacity + "\tAktuelle Kapazität: " + hp);
                System.out.println("Schiffstyp hinzufügen(1), entfernen(2) oder Auswahl beenden(3)?");
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
                    if (hp <= 0) {
                        System.out.println("Keine Schiffe zum entfernen vorhanden!");
                        break;
                    } else {
                        System.out.println("Von welchem Schiffstyp (Länge) sollen Schiffe entfernt werden?");      //TODO mögliche Schiffe auflisten
                        printShipPool();
                        shipType = userInput.nextInt();
                        if (shipType >= availableShips[0] && shipType <= availableShips[availableShips.length - 1]) {
                            if (shipPool[shipType] > 0) {
                                adjustShipPool(false, shipType);
                                break;
                            }
                        } else {
                            System.out.println("Keine legitime Schiffslänge!\n");
                        }
                    }
                }
            } else if (whatDo == 1) {
                while (true) {
                    System.out.println("Von welchem Schiffstyp (Länge) sollen Schiffe hinzugefügt werden?");
                    showPossibleShips();
                    shipType = userInput.nextInt();
                    if (shipType >= availableShips[0] && shipType <= availableShips[availableShips.length - 1]) {
                        adjustShipPool(true, shipType);
                        break;
                    } else {
                        System.out.println("Keine legitime Schiffslänge!\n");
                    }
                }
            }
        }
        if (hp == minCapacity) {
            System.out.println("Erforderliche Kapazität erfüllt!");
            printShipPool();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ships ");
            for (int i = shipPool.length - 1; i >= 0; i--) {
                for (int c = 1; c <= shipPool[i]; c++) {
                    stringBuilder.append(i);
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            String shipsLength = stringBuilder.toString();
            System.out.println("Netzwerk-Antwort: " + shipsLength);             //TODO netzwerk
        } else if (hp < minCapacity) {
            System.out.println("Erforderliche Kapazität nicht erreicht, bitte weitere Schiffe auswählen!\nFolgende Schiffe wurden bereits platziert:");
            printShipPool();
            requestShipTypes();
        } else {
            System.out.println("Erforderliche Kapazität überschritten, bitte Schiffe entfernen!\nFolgende Schiffe wurden bereits platziert:");
            printShipPool();
            requestShipTypes();
        }
    }


    public void adjustShipPool(boolean addRemoveToggle, int length) {
        if (addRemoveToggle) {
            shipPool[length]++;
            hp += length;
        } else {
            shipPool[length]--;
            hp -= length;
        }
    }

    /**
     * Fordert Spieler so lange auf Schiffe zu setzen bis Kapazitäten aufgebraucht sind.
     *
     * @param player1
     * @param player2
     */
    //TODO removeShip iwie integrieren
    public void startPlacingShips(Spieler player1, Spieler player2) {
        System.out.println("Platziere deine Schiffe " + player1.name + ": ..");
        while (player1.hp > 0) {
            player1.placeShipRequest();
            System.out.println("Non working printAll()");
            player1.printAll(player1, player2);
        }
        System.out.println("Folgende Schiffe wurden platziert:");
        player1.printShipList();
        player1.printAll(player1, player2);
    }

    public void printShipPool(){
        for (int i = availableShips[0]; i < availableShips[availableShips.length-1]+1; i++) {
            System.out.println(shipPool[i] + "x " + i + "-er Schiff");              //TODO nur mögliche Schiffstypen auflisten
        }
    }

    public void showPossibleShips(){
        System.out.println("Verfügbare Schiffslängen wären: ");
        for (int i = availableShips[0]; i < availableShips[availableShips.length-1]+1; i++) {
            System.out.print("["+ i + "] ");              //TODO nur mögliche Schiffstypen auflisten
        }
    }
}