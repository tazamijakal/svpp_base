package KI;

import Logik.Spieler;
import java.util.Random;

public abstract class KI extends Spieler {

    /**
     * hier wird ein random number generator erstellt, welcher spaeter mit dem kleinsten int wert der Karte und
     * dem groessten int wert der Karte ein Schiff plaziert, und auch schiessen wird.
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min + 1 ) - min);

        return randomNum;
    }
    final class fieldposition {
        public int x, y;
        public fieldposition (int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

    public fieldposition RdmZielpos() {
        Integer x;
        Integer y;
        x = randInt(0, mapSize - 1);
        y = randInt(0, mapSize - 1);
        return new fieldposition(x, y);
    }

    /**
     * Konstruktor erzeugt neuen Spieler mit eigener Map.
     *
     * @param name     Name des Spielers
     * @param mapSize  Groesse der Map (mapSize*mapSize == x*y)
     * @param hp Legt fest wie hp man noch hat
     * @param remainingShips  legt die anzahl der verbleibenden schiffe fest
     */
    public KI(String name, int mapSize, int hp, int[]remainingShips) {
        super(name, mapSize, hp, remainingShips);
    }

    /**
     * KI plaziert hiermiet spaeter die Schiffe
     */
    public abstract void KIplazieren();

    /**
     * KI schiesst auf ein Feld, entweder zufaellig, oder mit logik.
     */
    public abstract void KIshoot();

}

