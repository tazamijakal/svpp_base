package com.company;

public class Hilffunktion {
    public static void printField(int mapSize, String board[][]) {
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                if (x == mapSize - 1) {
                    System.out.print(board[x][y] + " \n");
                }
                else {
                    System.out.print(board[x][y] + "  ");
                }
            }
        }
    }
}
