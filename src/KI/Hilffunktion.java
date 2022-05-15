package KI;

import Logik.Ship;
import Logik.Spieler;

public class Hilffunktion {
    public static void printField(int mapSize, Object[][] board) {
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                if (x == mapSize - 1) {
                    readableFieldCell(board [x][y]);
                    System.out.print(readableFieldCell(board[x][y]) + " \n");
                }
                else {
                    readableFieldCell(board [x][y]);
                    System.out.print(readableFieldCell(board [x][y]) + "  ");
                }
            }
        }
    }

    /**
     * Hiermit geben wir eine lesbare Zelle aus.
     * @param Cell
     * @return
     */
    private static String readableFieldCell(Object Cell) {
        if (Cell instanceof Spieler.MisfireObject) {
            return "Miss";
        }
        if (Cell instanceof Spieler.TrefferObject) {
            return "Tref";
        }
        if (Cell instanceof Ship) {
            return "Ship";
        }
        if (Cell == null) {
            return " ~  ";
        }
        return "why ";
    }
}
