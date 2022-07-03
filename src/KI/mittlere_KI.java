package KI;
import GUI.SpielStart;
import KI.leichte_KI_zufall;


/**
 * KI als mittlere KI Klasse
 */
public class mittlere_KI extends KI {


    /**
     * Konstruktor erzeugt neuen KI.
     *
     * @param name           Name des Spielers
     * @param mapSize        Groesse der Map (mapSize*mapSize == x*y)
     * @param hp             Legt fest wie hp man noch hat
     * @param remainingShips legt die anzahl der verbleibenden schiffe fest
     * @param GAME SpielStart Objekt
     */
    public mittlere_KI(String name, int mapSize, int hp, int[] remainingShips, SpielStart GAME) {
        super(name, mapSize, hp, remainingShips, GAME);
    }

    /**
     * KI plaziert autamtisch und zufaellig die Schiffe, falls zu viele Versuche gebraucht werden,
     * faengt sie neu an zu plazieren.
     */
    @Override
    public void KIplazieren() {
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
     * KI schiesst zufaellig im Karomusster und braucht dadurch weniger Schuesse als die leichte KI.
     * @return String Nachricht fuer Gegner
     * @throws Exception falls es fehlschlaegt
     */
    @Override
    public String KIshoot() throws Exception {
        if (visibleBoard[hitX][hitY] instanceof TrefferObject) {
            fieldposition shootalg2Treffer = shootalg2Treffer(hitX, hitY);
            this.lastShotX = shootalg2Treffer.x;
            this.lastShotY = shootalg2Treffer.y;
            this.testx = shootalg2Treffer.x;
            this.testy = shootalg2Treffer.y;
            return shot(shootalg2Treffer.x,shootalg2Treffer.y);
        }
        fieldposition shootalg1 = shootalg1();
        this.lastShotX = shootalg1.x;
        this.lastShotY = shootalg1.y;
        this.testx = shootalg1.x;
        this.testy = shootalg1.y;
        return shot(shootalg1.x,shootalg1.y);
    }

}
