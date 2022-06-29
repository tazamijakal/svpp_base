package KI;



public class leichte_KI_zufall extends KI {

    /**
     * Konstruktor erzeugt neue KI und kopiert die Werte vom Spieler.
     *
     * @param name
     * @param mapSize
     * @param hp
     * @param remainingShips
     */

    public leichte_KI_zufall(String name, int mapSize, int hp, int[] remainingShips) {
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
                        while(shipList.size() > 0){
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
    public String KIshoot() {
        System.out.println("new shot");
        fieldposition rdmZielpos = RdmZielpos();
        if (visibleBoard[rdmZielpos.x][rdmZielpos.y] instanceof TrefferObject || visibleBoard[rdmZielpos.x][rdmZielpos.y] instanceof MisfireObject) {
            System.out.println("konnte nicht schießen");
            return KIshoot();
        }
        //visibleBoard[rdmZielpos.x][rdmZielpos.y] = new TrefferObject();
        return shot(rdmZielpos.x,rdmZielpos.y);
    }

    public static void main(String[] args) throws Exception {
        KI leichte_ki_zufall = new leichte_KI_zufall("easy", 5, 15, new int[]{0,0,1,1,1});
        leichte_ki_zufall.KIplazieren();
        leichte_ki_zufall.KIshoot();
        Hilffunktion.printField(leichte_ki_zufall.mapSize,leichte_ki_zufall.board);
    }
}
