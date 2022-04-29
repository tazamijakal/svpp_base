/**
 * Die Klasse Ship beinhaltet den Konstruktor für die Schiffe und eine Funktion um dem Schiff seine einzelnen Koordinaten zuzuweisen.
 */
public class Ship {
    int length;
    int initialX, initialY, initialD;       //Zwischenspeicher
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
    public Ship(int initialX, int initialY, int initialD, int length) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.initialD = initialD;
        System.out.println("SHIP CONSTTUCTOR");
        this.length = length;
    }

    /**
     * Fuegt einem Schiffsobjekt seine Koordinatenliste zu, da diese bei Objekterstellung noch nicht vorhanden ist.
     * @param coordinates   [2]*[lange], beinhaltet die jeweiligen x und y Werte
     */
    public void addCoordinates(int[][] coordinates) {
        this.coordinates = coordinates;
    }
}


