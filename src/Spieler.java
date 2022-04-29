import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Die Klasse Spieler beinhaltet momentan Methoden zur Spieler- und Spielfelderzeugung, Schiffsplatzierung und Kollisionsabfrage.
 */
public class Spieler {
    public final String name;       //damit wird der Spieler angesprochen
    int x, y, xd, yd, direction, length; //wird fuer Schiffplatzierung benutzt
    public int hp;          //capacity = felder die schiffe sind bzw. HP welche 0 erreichen wenn alle Schiffe zerstoert sind
    public int mapSize;         //mapSize = laenge/breite
    static int pCounter = 1;       //wird benötigt damit Spielfeld 1 immer links ist und vice versa
    public int playerNumber;         //currently not used, automatically incremented on player creation
    public final Object[][] board;      //Spielfeld ist eine Matrix und kann leicht navigiert und bearbeitet werden
    private int[][] collisionMap;        //wird ueberprueft um zu wissen ob Schiff genug Abstand zu den anderen Schiffen hat
    private int[][] hitMap;                //Zwischenspeicher für die einzelnen Koordinaten aus denen Schiff besteht
    public ArrayList<Ship> shipList = new ArrayList<>();        //Liste mit all den Schiffen eines Spielers
    Scanner userinput = new Scanner(System.in); //wird fuer Userinput benoetigt

    /**
     * Ersetzt bei Treffer Schiffsobjekt auf dem Spielfeld.
     */
    public static class TrefferObject{ public TrefferObject(){}}
    TrefferObject trefferObject = new TrefferObject();

    /**
     * Ersetzt "Wasser"-Felder(null-Onjekte) wenn man nichts trifft.
     */
    public static class MisfireObject{ public MisfireObject(){}}
    MisfireObject misfireObject = new MisfireObject();


    /**
     * Konstruktor erzeugt neuen Spieler mit eigener Map.
     *
     * @param name      Name des Spielers
     * @param mapSize   Groesse der Map (mapSize*mapSize == laenge*breite)
     * @param hp        Legt fest wie viele Felder mit Schiffen belegt werden koennen und wird gleichzeitig als health points verwendet.
     */
    public Spieler(String name, int mapSize, int hp) {     //
        this.name = name;
        this.hp = hp;
        this.mapSize = mapSize;
        playerNumber = pCounter;
        pCounter++;                   //der 2te Spieler der erstellt wird bekommt automatisch die #2 zugewiesen
        collisionMap = new int[mapSize][mapSize];
        board = new Object[mapSize][mapSize];       //Spielfeld
        for (int y = 0; y < mapSize; y++) {         //fuellt die map mit ~, soll Wasser darstellen
            for (int x = 0; x < mapSize; x++) {
                board[x][y] = null;
                collisionMap[x][y] = 0;
            }
        }
    }

    /**
     * Printet nur das Spielfeld des Objekts, welches die Funktion aufruft.
     */
    public void printField() {
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                System.out.print(board[x][y] + "  ");
            }
        }
    }

    /**
     * Printet beide Spielfelder nebeneinander. Da die Reihenfolge gleich bleibt kann man immer abwechselnd one und two abrufen.
     *
     * @param one Spieler 1
     * @param two Spieler 2
     */
    public void printBoth(Spieler one, Spieler two) {
        System.out.println();
        for (int y = 0; y < one.mapSize; y++) {
            one.printRow(y);
            System.out.print("     ");
            two.printRow(y);
            System.out.println("\n");
        }
    }

    /**
     * Printet zusätzlich die collision maps, ansonsten gleich wie printBoth()
     *
     * @param one Spieler 1
     * @param two Spieler 2
     */
    public void cPrintBoth(Spieler one, Spieler two) {
        System.out.println();
        Spieler temp;
        if(one.playerNumber > two.playerNumber){
            temp = one;
            one = two;
            two = temp;
        }
        for (int y = 0; y < mapSize; y++) {
            one.printRow(y);
            System.out.print("     ");
            one.printCollisionRow(y);
            System.out.print("     ");
            two.printRow(y);
            System.out.print("     ");
            two.printCollisionRow(y);
            System.out.println("\n");
        }
    }

    /**
     * printet nur 1 Reihe, wird in printBoth() verwendet
     *
     * @param row       Zeilenangabe
     */
    public void printRow(int row) {
        for (int x = 0; x < mapSize; x++) {
            if(board[x][row] instanceof Ship) {
                System.out.print("@" + "    ");
            }
            if(board[x][row] instanceof TrefferObject) {
                System.out.print("X" + "    ");
            }
            if(board[x][row] == null) {
                System.out.print("~" + "    ");
            }
            if(board[x][row] instanceof MisfireObject) {
                System.out.print("Z" + "    ");
            }
        }
    }

    /**
     * Gibt jeweils 1 Zeile aus der collision map aus, wird von den printBoth()-Befehlen verwendet.
     *
     * @param row       Zeilenangabe
     */
    public void printCollisionRow(int row){
        for (int x = 0; x < mapSize; x++) {
            System.out.print(collisionMap[x][row] + "    ");
        }
    }

    /**
     * Manuelle Positionierung von Schiffen. Funktioniert theoretisch, wurde aber noch nicht an startGame()/startWar() angepasst!
     *
     * @param x         x wert
     * @param y         y wert
     * @param direction richtung
     * @param length    laenge
     */

    public void manualShipPlacement(int x, int y, int direction, int length) {
        this.x = x - 1;
        this.y = y - 1;
        this.direction = direction;
        this.length = length;
        if (spaceCheck()) {
            placeShip(true);
        } else {
            System.out.println("Manual ship placement failed!");
        }
    }

    /**
     * Fraegt den Spieler wo er Schiffe haben will. Ist in einem loop, bis passende Position gefunden wurde.
     */
    public boolean placeShipRequest(int[] availableShips) {
        while(true) {
            System.out.println("X-Koordinate bei der das Schiff anfangen soll: ...");
            x = userinput.nextInt() - 1;                  //-1 da User nicht davon ausgeht das Matrix mit [0] startet
            System.out.println("Y-Koordinate bei der das Schiff anfangen soll: ...");
            y = userinput.nextInt() - 1;
            if (x < 0 || x > mapSize || y < 0 || y > mapSize) {
                System.out.println("Startpunkt out of bounds!");
            } else {
                break;
            }
        }
        System.out.println("Ship direction (numpad notation): ...");        //siehe directionCheck() für Erklärung
        direction = userinput.nextInt();
        while(true) {
            System.out.println("Schiffsgroesse eingeben, mögliche Längen wären:");      //mögliche Längen werden aus availableShips[] ermittelt
            for (int i : availableShips) {
                System.out.print(" [" + i + "]");
            }
            length = userinput.nextInt();
            if (length > hp) {
                System.out.println("Nicht ausreichend Schiffskapazitaeten vorhanden!");
            } else if(length <= availableShips[availableShips.length-1] && length >= availableShips[0]){
                break;
            }
        }
        if (spaceCheck()) {
            placeShip(true);
            hp -= length;       //verringert verbleibende Kapazitaet
            System.out.println(length+"-er Schiff wurde platziert!");
            return true;
        } else {
            System.out.println("Schiff konnte nicht gesetzt werden, da kein Platz oder out of bounds!");
            placeShipRequest(availableShips);
            return false;
        }
    }

    /**
     * Ueberprueft  mittels der in placeShip aktualisierten collision map ob an der gewuenschten Stelle Platz fuer das Schiff ist
     *
     * @return false = kein Platz fuer das Schiff
     */
    public boolean spaceCheck() {
        xd = 0;      // xd/yd variable wird angeben in welche Richtung Schiff beim platzieren waechst
        yd = 0;
        if(!directionCheck()){return false;}
        int xCheck = xd * length + x;         //wenn xCheck<0 clipped das Schiff mit der linken Wand, bei xCheck>mapSize clipped es rechts
        int yCheck = yd * length + y;         //same wie xCheck nur vertikal
        if (xCheck > mapSize || xCheck+1 < 0 || yCheck > mapSize || yCheck + 1 < 0) {      //wallclipchecker
            System.out.println("Schiff ueberschreitet Spielfeldgrenzen!");
            return false;
        }

        int tempX = x;      //damit x und y nicht ueberschrieben werden
        int tempY = y;
        for (int i = 0; i < length; i++) {    //prueft ob Felder (+deren angrenzende Felder) bereits belegt sind
            if (collisionMap[tempX][tempY]>0) {
                System.out.println("Nicht genug Platz für das Schiff!");
                return false;
            }
            tempX = tempX + xd;
            tempY = tempY + yd;
        }
        return true;
    }

    /**
     * Gibt Liste mit all den Schiffen aus welche ein Spieler platziert hat.
     */
    protected void printShipList(){
        int c = 1;
        for(Ship i: shipList){
            System.out.print(i.length+"-er Schiff bei:");
            for(int[] z: i.coordinates){
                System.out.print(Arrays.toString(z)+"; ");
                c++;
            }
            System.out.println("");
        }
    }

    /**
     * Platziert oder entfernt Schiff und aktualisiert collision map. Aufruf nur ueber interne Funktionen damit Platzierung auch valid ist
     *
     * @param placeRemoveToggle true um Schiffe zu platzieren, false um Schiffe zu entfernen
     */
    protected void placeShip(boolean placeRemoveToggle) {
        int[] startingPoint = {x, y};       //x+y werden spaeter für Schiffserstellung benoetigt und deswegen zwischengespeichert
        int[][] hitMap = new int[length][2];     //speichert die einzelnen Koordinaten aus dene ein Schiff bestehen
        for (int i = 0; i < length; i++) {
            hitMap[i][0] = x;
            hitMap[i][1] = y;
            board[x][y] = null;     //wird zwar null gesetzt aber später durch Ship ersetzt, falls placeRemoveToggle true ist
            x = x + xd;
            y = y + yd;
        }
        int[] endPoint = {x - xd, y - yd};
        if (placeRemoveToggle) {
            Ship schiff = new Ship(startingPoint[0], startingPoint[1], direction, length);
            schiff.addCoordinates(hitMap);
            shipList.add(schiff);
            for (int i = 0; i < length; i++) {
                board[hitMap[i][0]][hitMap[i][1]] = schiff;
            }
        }

        //collision map aktualisierung:
        if (xd != 0) {        //bei horizontalem Wachstum soll die Koordinate mit dem kleineren x Startpunkt sein
            if (startingPoint[0] > endPoint[0]) {
                int[] temp = startingPoint;
                startingPoint = endPoint;
                endPoint = temp;
            }
        } else {            //same bei vertikal, nur mit y
            if (startingPoint[1] > endPoint[1]) {
                int[] temp = startingPoint;
                startingPoint = endPoint;
                endPoint = temp;
            }
        }
        startingPoint[0] -= 1;  //Erweiterung um die angrenzenden Felder da Schiffe 1 Feld Abstand zueinander brauchen
        startingPoint[1] -= 1;
        endPoint[0] += 1;
        endPoint[1] += 1;

        int startxBU = startingPoint[0];        //Zwischenspeicher
        for (int c = startingPoint[1]; c <= endPoint[1]; c++) {       //zeichenet das Rechteck mit den Eckpunkten (sp0, sp1, ep0, ep1) in die collision map ein
            for (int cc = startingPoint[0]; cc <= endPoint[0]; cc++) {
                try {
                    if (placeRemoveToggle) {
                        collisionMap[startingPoint[0]][startingPoint[1]]++;
                    } else {
                        collisionMap[startingPoint[0]][startingPoint[1]]--;
                    }
                } catch (ArrayIndexOutOfBoundsException E) {
                }
                startingPoint[0]++;
            }
            startingPoint[1]++;
            startingPoint[0] = startxBU;
        }
    }

    /**
     * Ueberprueft ob Schiff auf dem Feld liegt, liest die Koordinaten aus und entfernt das komplette Schiff vom Spielfeld und der shipList[]
     *
     * @param x x-Achse
     * @param y y-Achse
     */
    public void removeShip(int x, int y) {
        if (board[x][y] instanceof Ship) {
            Object dummy = board[x][y];
            Ship schiff = (Ship) dummy;
            this.x = schiff.initialX;
            this.y = schiff.initialY;
            direction = schiff.initialD;
            length = schiff.length;
            xd = 0;      // xd/yd variable wird angeben in welche Richtung Schiff beim platzieren waechst
            yd = 0;
            directionCheck();       //legt xd/yd fest
            placeShip(false);
            shipList.remove(dummy);
            System.out.println("Schiff erfolgreich entfernt!");
        }
    }

    /**
     * Sucht nach passendem xd oder yd Wert (wird benötigt in spaceCheck() und removeShip()).
     * 4=links, 8=hoch, 6=rechts, 2=runter
     *
     * @return true/false
     */
    public boolean directionCheck() {        //sucht nach passendem xd oder yd wert
        if (direction == 4) {
            xd = -1;
        } else if (direction == 6) {
            xd = 1;
        } else if (direction == 8) {
            yd = -1;
        } else if (direction == 2) {
            yd = 1;
        } else {
            System.out.println("Keine gültige Richtungsangabe!");
            return false;
        }
        return true;
    }

    /**
     * Schiesst auf angegebene Koordinaten, soll nur von shootrequest() aufgerufen werden da dieses prüft ob bereits auf das Feld geschossen wurde.
     * Nach einem Treffer darf der Spieler erneut schiessen.
     * @param x x-Achse
     * @param y y-Achse
     */
    public void shoot(int x, int y, Spieler player1, Spieler player2) {
        if (board[x][y] instanceof Ship) {
            ((Ship) board[x][y]).length--;
            if(((Ship) board[x][y]).length==0){
                System.out.println("Treffer, versenkt!!!");
            } else{
                System.out.println("TREFFER!!!");
            }
            board[x][y] = trefferObject;
            hp--;
            if(hp>0){
                shootrequest(player1, player2);
            }
        } else {
            board[x][y] = misfireObject;
            System.out.println("Nichts getroffen..");
        }
    }

    /**
     * Fraegt den Spieler nach Zielkoordinaten, ueberprueft ob bereits auf Feld geschossen wurde.
     */
    public void shootrequest(Spieler player1, Spieler player2) {
        try{
            System.out.println("X-Koordinate eingeben: ...");
            int xAxis = userinput.nextInt() - 1;  // Read user input
            System.out.println("Y-Koordinate eingeben: ...");
            int yAxis = userinput.nextInt() - 1;
            if (board[xAxis][yAxis] instanceof TrefferObject || board[xAxis][yAxis] instanceof MisfireObject) {
                System.out.println("Bereits auf Feld geschossen!");
                shootrequest(player1, player2);
            } else {
                if (board[xAxis][yAxis] instanceof Ship) {
                    board[xAxis][yAxis] = trefferObject;
                    System.out.println("TREFFER!!!");
                    hp--;
                    if(hp>0){
                        cPrintBoth(player1, player2);
                        shootrequest(player1, player2);
                    }
                } else {
                    board[xAxis][yAxis] = misfireObject;
                    System.out.println("NICHTS GETROFFEN");
                }
            }
        }catch (ArrayIndexOutOfBoundsException E){
            System.out.println("Out of bounds!");
            shootrequest(player1, player2);
        }
    }
}

