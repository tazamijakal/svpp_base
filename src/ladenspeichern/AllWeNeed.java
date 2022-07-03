package ladenspeichern;

import Logik.*;
import javax.swing.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * Klasse um wichtige Attribute und Daten in einem Objekt zu speichern fuer laden und speichern im Programm
 *
 */
public class AllWeNeed implements Serializable {
    public final boolean amZug;
    public final Spieler player;
    public final Spieler player2; //eventuell KI
    public final String ID;
    public final JTable table;
    public final JTable table2;
    public final JTable table3;
    public final JTable table4;
    private static AtomicReference<Long> currentTime = new AtomicReference<>(System.currentTimeMillis());

    /**
     * Konstruktor um AllWeNeed Objekt zu erstellen
     * @param amZug boolean ob player am zug
     * @param player Spieler 1
     * @param player2 Spieler 2
     * @param table table zum laden und speichern fuer lokal und online
     * @param table2 table2 zum laden und speichern fuer lokal und online
     * @param table3 table3 zum laden und speicher von lokalem Spiel
     * @param table4 table4 zum laden und speicher von lokalem Spiel
     * @param name ID mit der nachher das Objekt abgespeichert und geladen wird
     */
    public AllWeNeed(boolean amZug, Spieler player, Spieler player2, JTable table, JTable table2, JTable table3, JTable table4, String name){
        this.amZug = amZug;
        this.player = player;
        this.player2 = player2;
        this.table = table;
        this.table2 = table2;
        this.table3 = table3;
        this.table4 = table4;
        this.ID = name;
    }

    /**
     * Methode um eine neue unique ID zu erstellen
     */
    public static Long nextId() {
        return currentTime.accumulateAndGet(System.currentTimeMillis(),
                (prev, next) -> next > prev ? next : prev + 1);
    }
}

