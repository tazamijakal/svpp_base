package Logik;


import GUI.SpielStart;
import Socket.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * Die Klasse Spieler beinhaltet Methoden zur Spieler- und Spielfelderzeugung, Schiffsplatzierung und Kollisionsabfrage.
 */


public class Spieler implements Serializable {
    public String Vergleich2;
    public boolean attackToken;
    public int lastShotX;
    public int lastShotY;
    public String name;       //damit wird der Spieler angesprochen
    public Server server;
    public Client client;
    public int x, y;
    public int xd, yd;
    public int hp;          //hp = felder die schiffe sind bzw. HP welche 0 erreichen wenn alle Schiffe zerstört sind
    public int hp2;
    public int mapSize;         //mapSize = länge/breite
    static int pCounter = 1;       //wird benötigt damit Spielfeld 1 immer links ist und vice versa
    public int playerNumber;         //wird als Index verwendet damit Spielfeld von Spieler 1 immer auf der linken Seite ist
    public Object[][] board;      //Spielfeld ist eine Matrix und kann leicht navigiert und bearbeitet werden
    public Object[][] visibleBoard;
    public int[][] radarMap;   //TODO für Radar
    public int[][] collisionMap;        //wird überprüft um zu wissen ob Schiff genug Abstand zu den anderen Schiffen hat
    //public int[][] hitMap;                //Zwischenspeicher für die einzelnen Koordinaten aus denen Schiff besteht
    public int[] remainingShips = {0,0,0,0,0,0,0};
    public ArrayList<Ship> shipList = new ArrayList<>();        //Liste mit all den Schiffen eines Spielers
    transient Scanner userinput = new Scanner(System.in); //wird für Userinput benötigt
    public boolean lokaltoken = true;
    public boolean load = false;
    public static SpielStart GAME;

    /**
     *
     * Konstruktor erzeugt neuen Spieler mit eigener Map.
     *
     * @param name      Name des Spielers
     * @param mapSize   Größe der Map (mapSize*mapSize == länge*breite)
     * @param hp        Legt fest wie viele Felder mit Schiffen belegt werden können und wird gleichzeitig als health points verwendet.
     * @param remainingShips die verbleibenden Schiffe werden angezeigt/gespeichert.
     * @param GAME SpielStart Objekt
     */
    public Spieler(String name, int mapSize, int hp, int[] remainingShips, SpielStart GAME) {     //
        this.name = name;
        this.hp = hp;
        attackToken = false;
        hp2 = hp;
        this.GAME = GAME;
        this.mapSize = mapSize;
        if(remainingShips != null){
            this.remainingShips = remainingShips.clone();
        }
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
     * Setter Methode fuer Client Attribut
     * @param c Client Objekt
     */
    public void clientSetter(Client c){
        this.client = c;
    }

    /**
     * Setter Methode fuer Client Attribut
     * @param s Server Objekt
     */
    public void serverSetter(Server s) {
        this.server = s;
    }

    /**
     * Methode um den player zurueckzusetzen, wichtig um zufaellig Schiffe zu platzieren mehrmals hintereinander
     * @param rms remaining ships vor Platzierung
     */
    public void resetplayer(int[] rms){
        collisionMap = new int[mapSize][mapSize];
        board = new Object[mapSize][mapSize];       //Spielfeld
        visibleBoard = new Object[mapSize][mapSize];
        radarMap = new int[mapSize][mapSize];
        remainingShips = rms.clone();
        shipList = new ArrayList<>();
        x = y = xd = yd = 0;
        hp = hp2 = remainingShips[2] + remainingShips[3] + remainingShips[4] + remainingShips[5] + remainingShips[6];
    }

    /**
     * Setter Methode um Spieler HPs neu zu setzen
     * @param newhp neues HP
     */
    public void sethps(int newhp){
        hp = newhp;
        hp2 = newhp;
    }

//----------------------PLACE-METHODEN--------------------------------------------------------------------------------------


    /**
     *
     * Ersetzt bei Treffer Schiffsobjekt auf dem Spielfeld.
     */
    public static class TrefferObject implements Serializable{ public TrefferObject(){}}
    TrefferObject trefferObject = new TrefferObject();

    /**
     *
     * Ersetzt "Wasser"-Felder(null-Onjekte) wenn man nichts trifft.
     */
    public static class MisfireObject implements Serializable{ public MisfireObject(){}}
    MisfireObject misfireObject = new MisfireObject();

    /**
     *
     * Platziert oder entfernt Schiff und aktualisiert collision map. Aufruf nur über interne Funktionen damit Platzierung auch valid ist
     *
     * @param placeRemoveToggle true um Schiffe zu platzieren, false um Schiffe zu entfernen
     * @param x Koordinate x
     * @param y Koordinate y
     * @param l Laenge
     * @param d Direction
     */
    public void placeRemoveShip(boolean placeRemoveToggle, int x, int y, int l, boolean d) {
        if(l == 0){
            return;
        }
        directionSetter(d);
        int[] startingPoint = {x, y};       //x+y werden später für Schiffserstellung benötigt und deswegen zwischengespeichert
        int[][] hitMap = new int[l][2];     //speichert die einzelnen Koordinaten aus denen ein Schiff bestehen
        for (int i = 0; i < l; i++) {
            hitMap[i][0] = x;
            hitMap[i][1] = y;
            board[x][y] = null;     //wird zwar null gesetzt aber später durch Ship ersetzt, falls placeRemoveToggle true ist
            x += xd;
            y += yd;
        }
        int[] endPoint = {x - xd, y - yd};
        if (placeRemoveToggle) {
            Ship schiff = new Ship(startingPoint[0], startingPoint[1], d, l);
            schiff.addCoordinates(hitMap);
            this.remainingShips[l]--;
            //hp -= l;
            System.out.println(l+"-er Schiff wurde platziert!");
            shipList.add(schiff);
            for (int i = 0; i < l; i++) {
                board[hitMap[i][0]][hitMap[i][1]] = schiff;
            }
        } else {
            this.remainingShips[l]++;
        }

        //collision map aktualisierung:
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
                } catch (ArrayIndexOutOfBoundsException E) {}
                startingPoint[0]++;             //??????????
            }
            startingPoint[1]++;                 //??????????
            startingPoint[0] = startxBU;        //??????????
        }
    }

    /**
     *
     * Frägt den Spieler wo er Schiffe haben will. Ist in einem loop, bis passende Position gefunden wurde.
     * @return boolean
     */
    public boolean uInputPlaceShipRequest() {
        int x, y, l;
        boolean d;
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
            System.out.println("Horizontale oder vertikale Platzierung(6/2): ...");
            int direction = userinput.nextInt();
            if(direction==6){
                d=true;
                break;
            } else if (direction==2) {
                d=false;
                break;
            }
            System.out.println("Keine gültige Schiffsorientierung!");
        }
        int index;
        while(true) {
            System.out.println("Länge des Schiffs auswählen, mögliche Längen wären:");
            showRemainingShips();
            l = userinput.nextInt();
            if(this.remainingShips[l]>0) {
                break;
            }
            System.out.println("Gewünschte Länge nicht verfügbar!");
        }
        if (spaceCheck(x,y,l,d)) {
            directionSetter(d);
            placeRemoveShip(true, x, y, l, d);
            return true;
        } else {
            System.out.println("Schiff konnte nicht gesetzt werden, da kein Platz oder out of bounds!");
            uInputPlaceShipRequest();
            return false;
        }
    }

    /**
     *
     * überprüft ob Schiff auf dem Feld liegt, liest die Koordinaten aus und entfernt das komplette Schiff vom Spielfeld und der shipList[]
     *
     * @param x x-Achse
     * @param y y-Achse
     * @return boolean ob okay
     */
    public boolean removeShipRequest(int x, int y){
        if(board[x][y] instanceof Ship){
            int xd = 0;
            int yd = 0;
            Ship dummy = (Ship) board[x][y];
            directionSetter(dummy.initialD);
            placeRemoveShip(false, x, y, dummy.length, dummy.initialD);
            shipList.remove(dummy);
            System.out.println("Schiff erfolgreich entfernt!");
            return true;
        }
        System.out.println("Kein Schiff an dieser Stelle!");
        return false;
    }

    /**
     *
     * überprüft  mittels der in placeShip aktualisierten collision map ob an der gewünschten Stelle Platz für das Schiff ist
     * @param x Koordinate x
     * @param y Koordinate y
     * @param l Laenge
     * @param d Direction
     * @return false = kein Platz für das Schiff
     */
    public boolean spaceCheck(int x, int y, int l, boolean d){
        if (x < 0 || x >= mapSize || y < 0 || y >= mapSize || (d && x + l > mapSize) || (!d && y + l > mapSize)) {return false; }
        int tempX = x;      //damit x und y nicht überschrieben werden
        int tempY = y;
        xd = 0;
        yd = 0;
        directionSetter(d);
        for (int i = 0; i < l; i++) {    //prüft ob Felder (+deren angrenzende Felder) bereits belegt sind
            if (collisionMap[tempX][tempY]>0) {
                System.out.println("Nicht genug Platz für das Schiff!");
                Vergleich2 = "Here";
                return false;
            }
            tempX += xd;
            tempY += yd;
        }
        Vergleich2 = "enough";
        return true;
    }

    /**
     *
     * Für horizontales Wachstum wird xd=1 gesetzt, ansonsten yd=1
     * @param d d=direction
     */
    public void directionSetter (boolean d){
        this.xd=0;
        this.yd=0;
        if(d){xd=1;} else {yd=1;}
    }

//------------------------SHOOT-METHODEN--------------------------------------------------------------------------------------------------


    /*public void setTargetCoordinates(int x, int y){
        this.x = x;
        this.y = y;
   }*/

    /**
     *
     * Frägt den Spieler nach Zielkoordinaten, überprüft ob bereits auf Feld geschossen wurde.
     * @return String shot
     */
    public String uInputShootRequest() {
        try{
            System.out.println("X-Koordinate eingeben: ...");
            x = userinput.nextInt() - 1;  // Read user input
            System.out.println("Y-Koordinate eingeben: ...");
            y = userinput.nextInt() - 1;
            if (visibleBoard[x][y] instanceof TrefferObject || visibleBoard[x][y] instanceof MisfireObject) {
                System.out.println("Bereits auf Feld geschossen!");
                return uInputShootRequest();
            } else {
                lastShotX = x;
                lastShotY = y;
                attackToken=false;
                if(name.equals("Client")){
                    Client.TextServer(shot(x,y));
                }
                return shot(x,y);
            }
        }catch (ArrayIndexOutOfBoundsException E){
            System.out.println("Out of bounds!");
            return uInputShootRequest();
        }
    }

    /*public void shootRequest(int x, int y) {
        try{
            if (visibleBoard[x][y] instanceof TrefferObject || visibleBoard[x][y] instanceof MisfireObject) {
                System.out.println("Bereits auf Feld geschossen!");
            } else {
                attackToken=false;
                if(name.equals("Client")){
                    Client.TextServer(shot(x,y));
                } else if (name.equals("Server")) {
                    Server.TextClient(shot(x,y));
                }
            }
        }catch (ArrayIndexOutOfBoundsException E){
            System.out.println("Out of bounds!");
        }
    }*/

    /**
     *
     * Nachricht an den Gegenspieler mit Schusskoordinaten.
     * @param x x-Achse
     * @param y y-Achse
     * @return shot row col (gemäß Kommunikationsprotokoll)
     */
    public String shot(int x, int y){
        return "shot "+x+" "+y;
    }

    /**
     *
     * Methode liest shot Koordinaten aus String
     * @param shot Uebergebener String
     * @return gibt shot Koordinaten zurueck
     */
    public int[] shotReader(String shot){
        int[] shotCoordinates = new int[2];
        shotCoordinates[0] = Integer.parseInt(shot.substring(5,6));
        shotCoordinates[1] = Integer.parseInt(shot.substring(7,8));
        return shotCoordinates;
    }

    /**
     *
     * Schießt auf eigenes Feld und antwortet Gegner....
     * @param x Koordinate x
     * @param y Koordinate y
     * @return gibt answer als String zurueck
     */
    public String shootYourself(int x, int y){
        if(board[x][y] instanceof Ship){
            if(((Ship) board[x][y]).length==1){
                System.out.println("Treffer, versenkt!");
                ((Ship) board[x][y]).length--;
                board[x][y] = trefferObject;
                //hp--;
                return "answer 2";
            } else {
                System.out.println("Treffer!");
                ((Ship) board[x][y]).length--;
                board[x][y] = trefferObject;
                //hp--;
                return "answer 1";
            }
        } else {
            System.out.println("Nichts getroffen!");
            board[x][y] = misfireObject;
            return "answer 0";
        }
    }

    /**
     *
     * Reaktion auf gegnerische Schussantwort (visibleBoard[][] aktualisieren)
     * @param x Koordinate x
     * @param y Koordinate y
     * @param answerString Antwort String
     * @return 0 = false 1,2 = true
     */
    public boolean answerReader(int x, int y, String answerString){
        String[] answer = answerString.split(" ");
        switch(answer[1]){
            case "0": visibleBoard[x][y]=misfireObject;
                attackToken=false;
                return false;
            case "1": visibleBoard[x][y]=trefferObject;
                return true;
            case "2": visibleBoard[x][y]=trefferObject;       //TODO sollte das gesamte Schiff als zerstört markieren
                return true;
            default:
                System.err.println("DEFAULT CASE");
                return true;
        }

    }


//--------------------TERMINAL-DARSTELLUNGS-Methoden-----------------------------------------------------------------------------------

    /**
     *
     * Gibt die verbleibenden Schiffe in der Konsole aus
     */
    public void showRemainingShips(){
        for (int i = 0; i < this.remainingShips.length; i++) {
            if(this.remainingShips[i]>0){
                System.out.print(this.remainingShips[i]+"x "+ i +"-er Schiff, ");
            }
        }
    }

    /**
     *
     * Gibt Liste mit all den Schiffen aus welche ein Spieler platziert hat.
     */
    protected void printShipList(){
        for(Ship i: shipList){
            System.out.print(i.length+"-er Schiff bei:");
            for(int[] z: i.coordinates){
                System.out.print(Arrays.toString(z)+"; ");
            }
            System.out.println("");
        }
    }

    /**
     *
     * Printet nur das Spielfeld des Objekts, welches die Funktion aufruft.
     */
    public void printField() {
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                System.out.print(board[x][y] + "  ");
            }
            System.out.println("");
        }
    }

    /**
     *
     * Printet beide Spielfelder nebeneinander. Da die Reihenfolge gleich bleibt kann man immer abwechselnd one und two abrufen.
     * Funktion ist iwie verbuggt, oldPrintAll() mnomentan besser.
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

    /**
     *
     * Gibt alle Felder der zwei Spieler in der Konsole aus
     * @param one Spieler 1
     * @param two Spieler 2
     */
    public void oldPrintAll(Spieler one, Spieler two){
        Spieler temp;
        if(one.playerNumber > two.playerNumber){
            temp = one;
            one = two;
            two = temp;
        }
        System.out.println();
        for (int y = 0; y < one.mapSize; y++) {
            printRow(one, y);
            System.out.print("     ");
            printCollisionRow(one, y);
            System.out.print("     ");
            printVisibleRow(one, y);
            System.out.print("     ");
            System.out.print("     ");
            printRow(two, y);
            System.out.print("     ");
            printCollisionRow(two, y);
            printVisibleRow(two, y);
            System.out.println("\n");
        }
    }

    /**
     *
     * printet nur 1 Reihe, wird in printBoth() verwendet
     * @param player Spieler Objekt
     * @param row       Zeilenangabe
     */
    public void printRow(Spieler player, int row) {
        for (int x = 0; x < mapSize; x++) {
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

    /**
     *
     * Gibt visibleboard spezifische row aus
     * @param player Spieler
     * @param row Reihe
     */
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
     *
     * Gibt jeweils 1 Zeile aus der collision map aus, wird von den printBoth()-Befehlen verwendet.
     * @param player Spieler Objekt
     * @param row       Zeilenangabe
     */
    public void printCollisionRow(Spieler player, int row){
        for (int x = 0; x < mapSize; x++) {
            System.out.print(player.collisionMap[x][row] + "    ");
        }
    }

    /**
     *
     * Hilfsklasse um fieldposition in einem Objekt zu speichern
     *
     */
    public static final class fieldposition {
        public int x, y;
        /**
         *
         * fieldposition Konstruktor
         * @param x Koordinate x
         * @param y Koordinate y
         */
        public fieldposition (int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     *
     * Hilfsmethode fuer placerandom() zuefaelliger Int als Rueckgabewert
     * @param min Minimum
     * @param max Maximum
     * @return random int
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min + 1 ) - min);
        return randomNum;
    }

    /**
     *
     * Hilfsmethode fuer placerandom() zuefaelliger boolean Rueckgabe wert
     * @return Random boolean
     */
    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    /**
     *
     * Hilfsmethode fuer placerandom()
     * @return fieldposition Objekt
     * @throws Exception falls es fehlschlaegt
     */
    public fieldposition RdmZielpos() throws Exception {
        Integer x;
        Integer y;
        x = randInt(0, mapSize - 1);
        y = randInt(0, mapSize - 1);
        return new fieldposition(x, y);
    }


    /**
     *
     * Methode um alle Schiffe (remainingships) auf dem Spielfeld zu platzieren
     *
     */
    public void placerandom() {
        String Vergleich = "Here";
        int counter;
        for (int shiplength = 0; shiplength < this.remainingShips.length; shiplength++) {
            counter = 0;
            while (remainingShips[shiplength] > 0) {
                fieldposition rdmZielpos = null;
                try {
                    rdmZielpos = RdmZielpos();
                } catch (Exception e) {
                    System.err.println("Error");
                }
                boolean direction = getRandomBoolean();
                if (spaceCheck(rdmZielpos.x, rdmZielpos.y, shiplength, direction)) {
                    placeRemoveShip(true, rdmZielpos.x, rdmZielpos.y, shiplength, direction);
                }
                boolean true_false = Vergleich.equals(Vergleich2);
                if (true_false) {
                    if (counter >= 5000) {
                        while (shipList.size() > 0) {
                            removeShipRequest(shipList.get(0).initialX, shipList.get(0).initialY);
                        }
                        counter = 0;
                        placerandom();
                    }
                    counter++;
                }
            }
        }
    }
}



