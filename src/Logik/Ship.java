package Logik;

/**
 * Die Klasse Logik.Ship beinhaltet den Konstruktor für die Schiffe und eine Funktion um dem Schiff seine einzelnen Koordinaten zuzuweisen.
 */
public class Ship {
    public int length;
    public boolean initialD;
    public int initialX, initialY;       //Zwischenspeicher
    int[][] coordinates;        //beinhaltet alle [x][y]-Paare aus denen ein Schiff besteht

    /**
     * Schiffsobjekt welches auf dem Spielfeld platziert wird und angeschossen werden kann.
     * Beinhaltet alle Parameter von placeShipRequest da diese im Nachhinein zum entfernen der Schiffe benötigt werden.
     *
     * @param initialX
     * @param initialY
     * @param initialD
     * @param length
     */
    public Ship(int initialX, int initialY, boolean initialD, int length) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.initialD = initialD;
        this.length = length;
    }

    /**
     * Fügt einem Schiffsobjekt seine Koordinatenliste zu, da diese bei Objekterstellung noch nicht vorhanden ist.
     * @param coordinates   [2]*[lange], beinhaltet die jeweiligen x und y Werte
     */
    public void addCoordinates(int[][] coordinates) {
        this.coordinates = coordinates;
    }

//    /**
//     *
//     * @return 4 = Spiel zu Ende; 3 = Treffer, versenkt; 2 = Treffer
//     */
//    public int shoot() {
//        this.length--;
//        if (this.length <= 0) {
//            remShips--;
//            if (remShips <= 0) {
//                return 4; // Spiel zuende
//            }
//            return 3; // Treffer versenkt
//        } else {
//            return 2; // Treffer
//        }
//    }

//    /**
//     * Schießt auf angegebene Koordinaten, soll nur von shootrequest() aufgerufen werden da dieses prüft ob bereits auf das Feld geschossen wurde.
//     * Nach einem Treffer darf der Logik.Spieler erneut schießen.
//     * @param x x-Achse
//     * @param y y-Achse
//     */
//    public void shoot(int x, int y, Logik.Spieler player1, Logik.Spieler player2) {
//        if (board[x][y] instanceof Logik.Ship) {
//            int result = ((Logik.Ship) board[x][y]).shoot();
//            if(result == 3){
//                System.out.println("Treffer, versenkt!!!");
//            } else if (result == 2) {
//                System.out.println("TREFFER!!!");
//            } else {
//                // TODO Spiel zu Ende
//            }
//            board[x][y] = trefferObject;
//            hp--;
//            if(hp>0){
//                shootrequest(player1, player2);
//            }
//        } else {
//            board[x][y] = misfireObject;
//            System.out.println("Nichts getroffen..");
//        }
//    }
}


