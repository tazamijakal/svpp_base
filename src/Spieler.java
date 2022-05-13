import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Die Klasse Spieler beinhaltet momentan Methoden zur Spieler- und Spielfelderzeugung, Schiffsplatzierung und Kollisionsabfrage.
 */
public class Spieler {
    public final String name;       //damit wird der Spieler angesprochen
    int x, y, xd, yd, direction, length; //wird für Schiffplatzierung benutzt
    public int hp;          //hp = felder die schiffe sind bzw. HP welche 0 erreichen wenn alle Schiffe zerstört sind
    public int mapSize;         //mapSize = länge/breite
    static int pCounter = 1;       //wird benötigt damit Spielfeld 1 immer links ist und vice versa
    public int playerNumber;         //wird als Index verwendet damit Spielfeld von Spieler 1 immer auf der linken Seite ist
    public final Object[][] board;      //Spielfeld ist eine Matrix und kann leicht navigiert und bearbeitet werden
    public final Object[][] visibleBoard;
    public int[][] radarMap;   //TODO für Radar
    private int[][] collisionMap;        //wird überprüft um zu wissen ob Schiff genug Abstand zu den anderen Schiffen hat
    private int[][] hitMap;                //Zwischenspeicher für die einzelnen Koordinaten aus denen Schiff besteht
    private int[] remainingShips;
    public ArrayList<Ship> shipList = new ArrayList<>();        //Liste mit all den Schiffen eines Spielers
    Scanner userinput = new Scanner(System.in); //wird für Userinput benötigt

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
     * @param mapSize   Größe der Map (mapSize*mapSize == länge*breite)
     * @param hp        Legt fest wie viele Felder mit Schiffen belegt werden können und wird gleichzeitig als health points verwendet.
     */
    public Spieler(String name, int mapSize, int hp, int[]remainingShips) {     //
        this.name = name;
        this.hp = hp;
        this.mapSize = mapSize;
        this.remainingShips = remainingShips.clone();
        playerNumber = pCounter;
        pCounter++;                   //der 2te Spieler der erstellt wird bekommt automatisch die #2 zugewiesen
        collisionMap = new int[mapSize][mapSize];
        board = new Object[mapSize][mapSize];       //Spielfeld
        visibleBoard = new Object[mapSize][mapSize];
        radarMap = new int[mapSize][mapSize];
        for (int y = 0; y < mapSize; y++) {         //füllt die map mit ~, soll Wasser darstellen
            for (int x = 0; x < mapSize; x++) {
                board[x][y] = null;
                visibleBoard[x][y] = null;
                collisionMap[x][y] = 0;
                radarMap[x][y] = 0;
            }
        }
    }

    /**
     * Frägt den Spieler wo er Schiffe haben will. Ist in einem loop, bis passende Position gefunden wurde.
     */
    public boolean placeShipRequest() {
        while(true) {
            System.out.print("Folgende Schiffe müssen noch platziert werden: ");      //mögliche Längen werden aus remainingShips[] ermittelt
            showRemainingShips();
            System.out.println("\nX-Koordinate bei der das Schiff anfangen soll: ...");
            x = userinput.nextInt() - 1;                  //-1 da User nicht davon ausgeht das Matrix mit [0] startet
            System.out.println("Y-Koordinate bei der das Schiff anfangen soll: ...");
            y = userinput.nextInt() - 1;
            if (x < 0 || x > mapSize || y < 0 || y > mapSize) {
                System.out.println("Startpunkt out of bounds!");
            } else {
                break;
            }
        }

        while(true){
            System.out.println("Richtung in die das Schiff wachsen soll 8=runter/6=rechts/2=runter/4=links (numpad notation): ...");        //siehe directionCheck() für Erklärung
            direction = userinput.nextInt();
            if(directionCheck()){
                break;
            }
        }
        int index;
        while(true) {
            System.out.println("Länge des Schiffs auswählen, mögliche Längen wären:");
            showRemainingShips();
            length = userinput.nextInt();
            if(this.remainingShips[length]>0) {
                break;
            }
            System.out.println("Gewünschte Länge nicht verfügbar!");
        }
        if (spaceCheck()) {
            placeShip(true);
            this.remainingShips[length]--;
            hp -= length;
            System.out.println(length+"-er Schiff wurde platziert!");
            return true;
        } else {
            System.out.println("Schiff konnte nicht gesetzt werden, da kein Platz oder out of bounds!");
            placeShipRequest();
            return false;
        }
    }

    /**
     * überprüft  mittels der in placeShip aktualisierten collision map ob an der gewünschten Stelle Platz für das Schiff ist
     *
     * @return false = kein Platz für das Schiff
     */
    public boolean spaceCheck() {
//        xd = 0;      // xd/yd variable wird angeben in welche Richtung Schiff beim platzieren wächst             //TODO REMOVE THIS
//        yd = 0;
//        if(!directionCheck()){return false;}
        int xCheck = xd * length + x;         //wenn xCheck<0 clipped das Schiff mit der linken Wand, bei xCheck>mapSize clipped es rechts
        int yCheck = yd * length + y;         //same wie xCheck nur vertikal
        if (xCheck > mapSize || xCheck+1 < 0 || yCheck > mapSize || yCheck + 1 < 0) {      //wallclipchecker
            System.out.println("Schiff überschreitet Spielfeldgrenzen!");
            return false;
        }

        int tempX = x;      //damit x und y nicht überschrieben werden
        int tempY = y;
        for (int i = 0; i < length; i++) {    //prüft ob Felder (+deren angrenzende Felder) bereits belegt sind
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
     * Platziert oder entfernt Schiff und aktualisiert collision map. Aufruf nur über interne Funktionen damit Platzierung auch valid ist
     *
     * @param placeRemoveToggle true um Schiffe zu platzieren, false um Schiffe zu entfernen
     */
    //TODO Wenn man mehrmals erfolglos versucht Schiff zu platzieren kann das nächste broken sein
    protected void placeShip(boolean placeRemoveToggle) {
        int[] startingPoint = {x, y};       //x+y werden später für Schiffserstellung benötigt und deswegen zwischengespeichert
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
    }       //TODO rename to placeRemoveShip

    /**
     * Manuelle Positionierung von Schiffen. Funktioniert theoretisch, wurde aber noch nicht an startGame()/startWar() angepasst!
     *
     * @param x         x wert
     * @param y         y wert
     * @param direction richtung
     * @param length    länge
     */
    public void manualShipPlacement(int x, int y, int direction, int length) {
        this.x = x-1;
        this.y = y-1;
        this.direction = direction;
        this.length = length;
        directionCheck();
        placeShip(true);
        this.remainingShips[length]--;
        hp -= length;
        System.out.println(length+"-er Schiff wurde platziert!");
    }

    /**
     * überprüft ob Schiff auf dem Feld liegt, liest die Koordinaten aus und entfernt das komplette Schiff vom Spielfeld und der shipList[]
     *
     * @param x x-Achse
     * @param y y-Achse
     */
    public void removeShip(int x, int y) {                  //TODO re-add in remainingShips list and hp
        if (board[x][y] instanceof Ship) {
            Object dummy = board[x][y];
            Ship schiff = (Ship) dummy;
            this.x = schiff.initialX;
            this.y = schiff.initialY;
            direction = schiff.initialD;
            length = schiff.length;
            xd = 0;      // xd/yd variable wird angeben in welche Richtung Schiff beim platzieren wächst
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
        xd=0;
        yd=0;
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
     * Frägt den Spieler nach Zielkoordinaten, überprüft ob bereits auf Feld geschossen wurde.
     */
    public void shootrequest(Spieler attacker, Spieler defender) {
        try{
            System.out.println("X-Koordinate eingeben: ...");
            int xAxis = userinput.nextInt() - 1;  // Read user input
            System.out.println("Y-Koordinate eingeben: ...");
            int yAxis = userinput.nextInt() - 1;
            if (attacker.visibleBoard[xAxis][yAxis] instanceof TrefferObject || attacker.visibleBoard[xAxis][yAxis] instanceof MisfireObject) {
                System.out.println("Bereits auf Feld geschossen!");
                shootrequest(attacker, defender);
            } else {
                if (defender.board[xAxis][yAxis] instanceof Ship) {
                    if(((Ship) defender.board[xAxis][yAxis]).length==1){
                        System.out.println("TREFFER, VERSENKT!");
                    } else {
                        System.out.println("TREFFER!!!");
                    }
                    ((Ship) defender.board[xAxis][yAxis]).length--;
                    attacker.visibleBoard[xAxis][yAxis] = trefferObject;
                    defender.board[xAxis][yAxis] = trefferObject;
                    this.hp--;
                    if(this.hp>0){
                        printAll(attacker, defender);
                        shootrequest(attacker, defender);
                    }
                } else {
                    defender.board[xAxis][yAxis] = misfireObject;
                    attacker.visibleBoard[xAxis][yAxis] = misfireObject;
                    System.out.println("NICHTS GETROFFEN");
                }
            }
        }catch (ArrayIndexOutOfBoundsException E){
            System.out.println("Out of bounds!");
            shootrequest(attacker, defender);
        }
    }

    /**
     * Schießt auf angegebene Koordinaten, soll nur von shootrequest() aufgerufen werden da dieses prüft ob bereits auf das Feld geschossen wurde.
     * Nach einem Treffer darf der Spieler erneut schießen.
     * @param x x-Achse
     * @param y y-Achse
     */
    public void shoot(int x, int y, Spieler defender, Spieler attacker) {
        if (board[x][y] instanceof Ship) {
            if(((Ship) board[x][y]).length == 1){
                System.out.println("Treffer, versenkt!!!");
            } else {
                System.out.println("TREFFER!!!");
            }
            ((Ship) board[x][y]).length--;
            board[x][y] = trefferObject;
            visibleBoard[x][y] = trefferObject;
            this.hp--;
            if(hp>0){
                shootrequest(attacker, defender);
            }
            } else {
            board[x][y] = misfireObject;
            visibleBoard[x][y] = misfireObject;
            System.out.println("Nichts getroffen..");
        }
    }

    public void radarRequest(Spieler attacker, Spieler defender){
        while(true){
            System.out.println("Wo soll Radar sein?");
            System.out.println("X-Koordinate eingeben..");
            int x = userinput.nextInt()-1;
            System.out.println("Y-Koordinate eingeben..");
            int y = userinput.nextInt()-1;
            if(x>0 && x<mapSize-1 && y>0 && y<mapSize-1){
                attacker.radar(x,y, attacker, defender);
                break;
            } else {
                System.out.println("mapsize: "+mapSize);
                System.err.println("Out of bounds!");
            }
        }
    }

    public void radar(int x, int y, Spieler attacker, Spieler defender) {
        int enemies = 0;
        for(int i=x-1; i<x+1; i++){
            for(int c=y-1; c<y+1; c++){
                System.out.println(x+" "+y);
                if(defender.board[x][y] instanceof Ship){
                    enemies++;
                }
            }
        }
        System.out.println("Detected "+enemies+" enemies at ["+x+"]["+y+"]");
        attacker.radarMap[x][y]=enemies;
        System.out.println(attacker.radarMap[x][y]);
    }

    public void showRemainingShips(){
        for (int i = 0; i < this.remainingShips.length; i++) {
            if(this.remainingShips[i]>0){
                System.out.print(this.remainingShips[i]+"x "+ i +"-er Schiff, ");
            }
        }
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
    public void printAll(Spieler one, Spieler two) {
        Spieler dummy;
        if(one.playerNumber < two.playerNumber){            //stellt sicher das Spielfeld von Spieler 1 immer links ist
            dummy = one;
        } else {dummy = two;}
        System.out.println();
        StringBuilder stringBuilder = new StringBuilder();      //TODO REMOVE
        stringBuilder.append("one P#:"+one.playerNumber+" two P#: "+two.playerNumber+"\t");
        for (int y = 0; y < one.mapSize; y++) {
            for(int i=0; i<=1; i++){
                printRow(dummy, y);

                stringBuilder.append(dummy.playerNumber);
                System.out.print("     ");
//                printCollisionRow(dummy, y);
//                System.out.print("     ");
                printVisibleRow(dummy, y);
                System.out.print("   |     ");
                if(dummy.playerNumber==1){
                    stringBuilder.append("S-->");
                    dummy = two;
                } else {dummy = one;
                    stringBuilder.append("S-->");}
            }
            System.out.println("\n");
        }
        System.out.println("Player switcher: "+stringBuilder.toString());

    }


//    /**
//     * Printet zusätzlich die collision maps, ansonsten gleich wie printBoth()
//     *
//     * @param one Spieler 1
//     * @param two Spieler 2
//     */
//    public void cPrintBoth(Spieler one, Spieler two) {
//        System.out.println();
//        Spieler temp;
//        if(one.playerNumber > two.playerNumber){
//            temp = one;
//            one = two;
//            two = temp;
//        }
//        for (int y = 0; y < mapSize; y++) {
//            one.printRow(y);
//            System.out.print("     ");
//            one.printCollisionRow(y);
//            System.out.print("     ");
//            two.printRow(y);
//            System.out.print("     ");
//            two.printCollisionRow(y);
//            System.out.println("\n");
//        }
//    }

    /**
     * printet nur 1 Reihe, wird in printBoth() verwendet
     *
     * @param row       Zeilenangabe
     */
    public void printRow(Spieler player, int row) {
        for (int x = 0; x < mapSize; x++) {
//            if(x==3 && row==3){
//                System.out.println("Should "+ this.radarMap[x][row] +" now create "+x+" "+row+" radar map");
//            }
//            if (radarMap[x][row] > 0) {
//                System.err.println(radarMap[x][row]);
//                System.out.print(radarMap[x][row] + "    ");
//            } else {
                if (player.board[x][row] instanceof Ship) {
                    System.out.print("@" + "    ");
                }
                if (player.board[x][row] instanceof TrefferObject) {
                    System.out.print("X" + "    ");
                }
                if (player.board[x][row] == null) {
                    System.out.print("~" + "    ");
                }
                if (player.board[x][row] instanceof MisfireObject) {
                    System.out.print("Z" + "    ");
                }
//            }
        }
    }

    public void printVisibleRow(Spieler player, int row){

        for (int x = 0; x < mapSize; x++) {
            if (player.visibleBoard[x][row] instanceof Ship) {
                System.out.print("@\t");
            }
            if (player.visibleBoard[x][row] instanceof TrefferObject) {
                System.out.print("X\t");
            }
            if (player.visibleBoard[x][row] == null) {
                System.out.print("~\t");
            }
            if (player.visibleBoard[x][row] instanceof MisfireObject) {
                System.out.print("Z\t");
            }
        }
    }

    /**
     * Gibt jeweils 1 Zeile aus der collision map aus, wird von den printBoth()-Befehlen verwendet.
     *
     * @param row       Zeilenangabe
     */
    public void printCollisionRow(Spieler player, int row){
        for (int x = 0; x < mapSize; x++) {
            System.out.print(player.collisionMap[x][row] + "    ");
        }
    }

}

