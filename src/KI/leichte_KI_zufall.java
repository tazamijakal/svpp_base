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
    public void KIplazieren() {
        String Vergleich = "Here";
        int counter = 0;
        int savex = 0;
        int savey = 0;
        for (int shiplength = 0; shiplength < this.remainingShips.length; shiplength++) {
            counter = 0;
            while (remainingShips[shiplength] > 0) {
                fieldposition rdmZielpos = RdmZielpos();
                boolean direction = getRandomBoolean();
                if (spaceCheck(rdmZielpos.x, rdmZielpos.y, shiplength, direction)) {
                    placeRemoveShip(true, rdmZielpos.x, rdmZielpos.y, shiplength, direction);
                    savex = rdmZielpos.x;
                    savey = rdmZielpos.y;
                }
                boolean true_false = Vergleich.equals(Vergleich2);
                if (true_false) {
                    if (counter >= 50) {
                        removeShipRequest(savex, savey);
                        counter = 0;
                    }
                    counter++;
                    System.out.println(counter);
                }
            }
        }
    }

    @Override
    public void KIshoot() {
        fieldposition rdmZielpos = RdmZielpos();
        if (board[rdmZielpos.x][rdmZielpos.y] instanceof TrefferObject && board[rdmZielpos.x][rdmZielpos.y] instanceof MisfireObject) {
            System.out.println("konnte nicht schießen");
            KIshoot();
            return;
        }
        shotReader(shot(rdmZielpos.x,rdmZielpos.y));
    }

    public static void main(String[] args) {
        KI leichte_ki_zufall = new leichte_KI_zufall("easy", 5, 15, new int[]{0,0,1,1,1});
        leichte_ki_zufall.KIplazieren();
        leichte_ki_zufall.KIshoot();
        Hilffunktion.printField(leichte_ki_zufall.mapSize,leichte_ki_zufall.board);
    }
}
