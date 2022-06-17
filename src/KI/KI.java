package KI;

import Logik.Spieler;
import java.util.Random;

public abstract class KI extends Spieler {

    protected int hitX, hitY;
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

    public boolean shoottester(int x, int y) {
        if (x < 0 || y < 0 || x >= mapSize || y >= mapSize) {
            return false;
        }
        return !(visibleBoard[x][y] instanceof TrefferObject || visibleBoard[x][y] instanceof MisfireObject);
    }

    final fieldposition shootalg1 () throws Exception {
        int x, y, feldzaehler;
        //schiessen auf (2)(1), danach (mapsize-1)(1).... (2)(mapsize-1)(mapsize-1)(mapsize-1)ausser es trifft etwas.
        feldzaehler = mapSize > 1?1:0;
        while (feldzaehler < mapSize*mapSize) {
            x = feldzaehler % mapSize;
            y = feldzaehler / mapSize;
            if (!shoottester(x, y)){
                feldzaehler+=2;
            }

            else {
                hitX = x;
                hitY = y;
                return new fieldposition(x, y);
            }
        }
        throw new Exception("no position left");
    }

    final fieldposition shootalg2Treffer (int hitX, int hitY) throws Exception {
        int counter = 1;
        if (shoottester(hitX, hitY - counter)) {
            return new fieldposition(hitX, hitY - counter);
        } else if (!(hitX  < 0 || hitY - counter < 0 || hitX >= mapSize || hitY - counter >= mapSize) && visibleBoard[hitX][hitY - counter] instanceof TrefferObject) {
            while (!(hitX  < 0 || hitY - counter < 0 || hitX >= mapSize || hitY - counter >= mapSize) && visibleBoard[hitX][hitY - counter] instanceof TrefferObject) {
                counter++;
            }
            if (shoottester(hitX, hitY - counter)) {
                return new fieldposition(hitX, hitY - counter);
            }
        }
        counter = 1;
        if (shoottester(hitX - counter, hitY)) {
            return new fieldposition(hitX - counter, hitY);
        } else if (!(hitX - counter  < 0 || hitY < 0 || hitX - counter >= mapSize || hitY >= mapSize) && visibleBoard[hitX - counter][hitY] instanceof TrefferObject) {
            while (!(hitX - counter  < 0 || hitY < 0 || hitX - counter >= mapSize || hitY >= mapSize) && visibleBoard[hitX - counter][hitY] instanceof TrefferObject) {
                counter++;
            }
            if (shoottester(hitX - counter,hitY)) {
                return new fieldposition(hitX - counter, hitY);
            }
        }
        counter = 1;
        if (shoottester(hitX, hitY + counter)) {
            return new fieldposition(hitX, hitY + counter);
        } else if (!(hitX  < 0 || hitY + counter < 0 || hitX >= mapSize || hitY + counter >= mapSize) && visibleBoard[hitX][hitY + counter] instanceof TrefferObject) {
            while (!(hitX  < 0 || hitY + counter < 0 || hitX >= mapSize || hitY + counter >= mapSize) && visibleBoard[hitX][hitY + counter] instanceof TrefferObject) {
                counter++;
            }
            if (shoottester(hitX, hitY + counter)) {
                return new fieldposition(hitX, hitY + counter);
            }
        }
        counter = 1;
        if (shoottester(hitX + counter, hitY)) {
            return new fieldposition(hitX + counter, hitY);
        } else if (!(hitX + counter  < 0 || hitY < 0 || hitX + counter >= mapSize || hitY >= mapSize) && visibleBoard[hitX + counter][hitY] instanceof TrefferObject) {
            while (!(hitX + counter  < 0 || hitY < 0 || hitX + counter >= mapSize || hitY >= mapSize) && visibleBoard[hitX + counter][hitY] instanceof TrefferObject) {
                counter++;
            }
            if (shoottester(hitX + counter,hitY)) {
                return new fieldposition(hitX + counter, hitY);
            }
        }
        return shootalg1()  ;
    }

    public fieldposition RdmZielpos() {
        Integer x;
        Integer y;
        x = randInt(0, mapSize - 1);
        y = randInt(0, mapSize - 1);
        return new fieldposition(x, y);
    }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
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
    public abstract String KIshoot() throws Exception;

}

