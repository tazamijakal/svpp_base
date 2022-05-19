package ladenspeichern;

import Logik.*;
import KI.*;

public class AllWeNeed {
    public final boolean amZug;
    public final Spieler player;
    public final KI bot;
    public final int ID;

    public AllWeNeed(boolean amZug, Spieler player,KI bot,int id){
        this.amZug = amZug;
        this.player = player;
        this.bot = bot;
        this.ID = id;
    }
}
