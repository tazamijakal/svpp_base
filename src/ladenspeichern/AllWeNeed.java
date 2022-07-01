package ladenspeichern;

import Logik.*;
import KI.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

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

    public static Long nextId() {
        return currentTime.accumulateAndGet(System.currentTimeMillis(),
                (prev, next) -> next > prev ? next : prev + 1);
    }
}

