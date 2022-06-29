package KI;
import KI.leichte_KI_zufall;


public class mittlere_KI extends KI {


    /**
     * Konstruktor erzeugt neuen KI.
     *
     * @param name           Name des Spielers
     * @param mapSize        Groesse der Map (mapSize*mapSize == x*y)
     * @param hp             Legt fest wie hp man noch hat
     * @param remainingShips legt die anzahl der verbleibenden schiffe fest
     */

    public mittlere_KI(String name, int mapSize, int hp, int[] remainingShips) {
        super(name, mapSize, hp, remainingShips);
    }

    @Override
    public void KIplazieren() {
        String Vergleich = "Here";
        int counter;
        for (int shiplength = 0; shiplength < this.remainingShips.length; shiplength++) {
            counter = 0;
            while (remainingShips[shiplength] > 0) {
                fieldposition rdmZielpos = RdmZielpos();
                boolean direction = getRandomBoolean();
                if (spaceCheck(rdmZielpos.x, rdmZielpos.y, shiplength, direction)) {
                    placeRemoveShip(true, rdmZielpos.x, rdmZielpos.y, shiplength, direction);
                }
                boolean true_false = Vergleich.equals(Vergleich2);
                if (true_false) {
                    if (counter >= 100) {
                        while (shipList.size() > 0) {
                            removeShipRequest(shipList.get(0).initialX, shipList.get(0).initialY);
                        }
                        counter = 0;
                        KIplazieren();
                    }
                    counter++;
                }
            }
        }
    }

    @Override
    public String KIshoot() throws Exception {
        if (visibleBoard[hitX][hitY] instanceof TrefferObject) {
            fieldposition shootalg2Treffer = shootalg2Treffer(hitX, hitY);
            //visibleBoard[shootalg2Treffer.x][shootalg2Treffer.y] = new MisfireObject();
            return shot(shootalg2Treffer.x,shootalg2Treffer.y);
        }
        fieldposition shootalg1 = shootalg1();
        //visibleBoard[shootalg1.x][shootalg1.y] = new TrefferObject();
        return shot(shootalg1.x,shootalg1.y);
    }

    public static void main(String[] args) throws Exception {
        KI mittlere_KI = new mittlere_KI("middle", 5, 15, new int[]{0,0,1,1,1,0,0});
        mittlere_KI.KIplazieren();
        mittlere_KI.KIshoot();
        Hilffunktion.printField(mittlere_KI.mapSize,mittlere_KI.board);
    }
}
