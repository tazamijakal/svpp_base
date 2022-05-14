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
     * @param Schiffgroessen Array mit Schiffgroessen
     */
    public abstract void KIplazieren(int Schiffgroessen[]);

    /**
     * KI schiesst auf ein Feld, entweder zufaellig, oder mit logik.
     */
    public abstract void KIshoot();
}

class testKI extends KI {

    public testKI(String name, int mapSize, int hp, int[]remainingShips) {
        super(name, mapSize, hp, remainingShips);
    }

    @Override
    public void KIplazieren(int[] Schiffgroessen) {
        for(int groesse: Schiffgroessen){
            System.out.println("Bot soll Schiff der Groesse "+groesse+" platzieren!");
        }
    }
    @Override
    public void KIshoot() {;
    }


    public static void main(String[] args) {
            KI testKI = new testKI("halp",5,5, new int [5]);
            //testKI.manualShipPlacement(3,4,8,2);
            testKI.KIplazieren(new int[]{2,5,3});
            Hilffunktion.printField(testKI.mapSize,testKI.board);

        }
}