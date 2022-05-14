package KI;

public class leichte_KI_zufall extends KI {

    /**
     * Konstruktor erzeugt neuen Spieler mit eigener Map.
     *
     * @param name     Name des Spielers
     * @param mapSize  Groesse der Map (mapSize*mapSize == x*y)
     * @param hp
     * @param remainingShips
     */

    public leichte_KI_zufall(String name, int mapSize, int hp, int[] remainingShips) {
        super(name, mapSize, hp, remainingShips);
    }

    @Override
    public void KIplazieren(int[] Schiffgroessen) {
        fieldposition rdmZielpos = RdmZielpos();
        int direction = randInt(1,4);
        //int length =
        for (int groesse: Schiffgroessen) {
           // placeShipRequest(rdmZielpos.x,rdmZielpos.y,);
        }
    }

    @Override
    public void KIshoot() {
        fieldposition rdmZielpos = RdmZielpos();
        if (board[rdmZielpos.x][rdmZielpos.y].equals("X") && board[rdmZielpos.x][rdmZielpos.y].equals("0")) {
            System.out.println("kann nicht hierhin schießen");
            KIshoot();
            return;
        }
       // shoot(rdmZielpos.x, rdmZielpos.y);
    }
    public static void main(String[] args) {
        KI leichte_ki_zufall = new leichte_KI_zufall("easy", 5, 5, new int[1]);
        leichte_ki_zufall.KIshoot();
        Hilffunktion.printField(leichte_ki_zufall.mapSize,leichte_ki_zufall.board);
    }
}
//help
