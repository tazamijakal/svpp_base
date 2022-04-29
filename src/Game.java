import java.util.Scanner;

public class Game {

    public int mapSize;
    public int healthPoints;
    public int[] availableShips;
    public int[] smallFieldAvailableShips = {2,3,4,5};
    public int[] bigFieldAvailableShips = {3,4,5,6};
    public Game() {
        System.out.println("Spiel wird vorbereitet..");
    }
    Scanner userInput = new Scanner(System.in);  // Create a Scanner object

    /**
     * Bereitet das Spiel vor (Spielfeldgroesse und festlegen, Spieler und deren Spielfelder erzeugen, Schiffe platzieren und Spiel mit startWar() starten.
     */
    public void startGame(){
        setMapSize();
        System.out.println("Name fuer Spieler 1: ..");
        String player1name = userInput.next();
        System.out.println("Name fuer Spieler 2: ..");
        String player2name = userInput.next();
        Spieler spieler1 = new Spieler(player1name, mapSize, healthPoints);
        Spieler spieler2 = new Spieler(player2name, mapSize, healthPoints);
        spieler1.cPrintBoth(spieler1, spieler2);
        startPlacingShips(spieler1, spieler2);
        startPlacingShips(spieler2, spieler1);
        startWar(spieler1, spieler2);
    }

    /**
     * Laesst beide Spieler abwechselnd aufeinander schiessen bis ein Spieler gewinnt oder es zum Unentschieden kommt.
     * @param player1
     * @param player2
     */
    public void startWar(Spieler player1, Spieler player2) {
        player1.hp = healthPoints;              //hier bezeichnet hp die Anzahl an Feldern auf welchen noch Schiffe liegen
        player2.hp = healthPoints;
        while (player1.hp > 0 && player2.hp > 0) {
            player1.cPrintBoth(player1, player2);
            System.out.println("Zielkoordinaten eingeben "+player1.name);
            player2.shootrequest(player2, player1);
            player1.cPrintBoth(player1, player2);
            System.out.println("Zielkoordinaten eingeben "+player2.name);
            player1.shootrequest(player1, player2);
        }
        if (player1.hp == player2.hp) {
            System.out.println("Unentschieden!");
        } else if (player1.hp < player2.hp) {
            System.out.println(player2.name+" hat gewonnen!");
        } else {
            System.out.println(player1.name+" hat gewonnen!");
        }
        System.out.println("GG WP");
    }

    /**
     * Fraegt Spieler nach der gewuenschten Spielfeldgroesse und ueberprueft ob diese valid ist.
     */
    public void setMapSize(){
        System.out.println("Spielfeldgroesse zwischen 5 und 30 auswaehlen:");
        mapSize = userInput.nextInt();
        if  (mapSize < 31 && mapSize > 4) {
            healthPoints = (int) ((double) mapSize*mapSize/100*30);             //nicht unbedingt genau 30% da es nur ganze Felder
            if(mapSize<19) availableShips = smallFieldAvailableShips;           //gibt die moeglichen Schiffslaengen vor
            else availableShips = bigFieldAvailableShips;
        } else if  (mapSize > 30) {
            System.out.println("Maximale Spielfeldgroesse ueberschritten");
            setMapSize();
        } else {
            System.out.println("Spielfeld zu klein!");
            setMapSize();
        }
    }

    /**
     * Fordert Spieler so lange auf Schiffe zu setzen bis Kapazitaeten aufgebraucht sind.
     * @param player1
     * @param player2
     */
    //TODO removeShip iwie integrieren
    public void startPlacingShips(Spieler player1, Spieler player2){
        System.out.println("Platziere deine Schiffe "+player1.name+": ..");
        while(player1.hp>0){
            System.out.println("Verbleibende Kapazitaeten: "+player1.hp);
            player1.placeShipRequest(availableShips);
            player1.cPrintBoth(player1, player2);
        }
        System.out.println("Folgende Schiffe wurden platziert:");
        player1.printShipList();
        player1.cPrintBoth(player1, player2);
    }
}
