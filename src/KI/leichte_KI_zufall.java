package KI;


import GUI.SpielStart;

/**
 * KI als leichte KI Klasse
 */
public class leichte_KI_zufall extends KI {

    /**
     * Konstruktor erzeugt neue KI und kopiert die Werte vom Spieler.
     *
     * @param name String name
     * @param mapSize int mapSize
     * @param hp int HP
     * @param remainingShips int Array remaining ships
     * @param GAME SpielStart Objekt
     */
    public leichte_KI_zufall(String name, int mapSize, int hp, int[] remainingShips, SpielStart GAME) {
        super(name, mapSize, hp, remainingShips, GAME);
    }

    /**
     * KI plaziert autamtisch und zufaellig die Schiffe, falls zu viele Versuche gebraucht werden,
     * faengt sie neu an zu plazieren.
     *
     * @throws Exception falls es fehlschlaegt
     */
    @Override
    public void KIplazieren() throws Exception {
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

    /**
     * KI schiesst zufaellig bis sie etwas trifft und fuehrt dann schootalg2 aus.
     *
     * @return answer als String
     * @throws Exception falls es fehlschlaegt
     */
    @Override
    public String KIshoot() throws Exception {
        System.out.println("new shot");
        fieldposition rdmZielpos = RdmZielpos();
        if (shoottester(rdmZielpos.x, rdmZielpos.y)) {
            if (visibleBoard[hitX][hitY] instanceof TrefferObject) {
                fieldposition shootalg2Treffer = shootalg2Treffer(hitX, hitY);
                this.lastShotX = shootalg2Treffer.x;
                this.lastShotY = shootalg2Treffer.y;
                this.testx = shootalg2Treffer.x;
                this.testy = shootalg2Treffer.y;
                return shot(shootalg2Treffer.x, shootalg2Treffer.y);
            }
            this.lastShotX = rdmZielpos.x;
            this.lastShotY = rdmZielpos.y;
            this.testx = rdmZielpos.x;
            this.testy = rdmZielpos.y;
            hitX = rdmZielpos.x;
            hitY = rdmZielpos.y;
            return shot(rdmZielpos.x, rdmZielpos.y);
        }
        return KIshoot();
    }
}
