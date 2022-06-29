package GUI;

import Logik.Ship;
import Logik.Spieler;
import ladenspeichern.AllWeNeed;
import ladenspeichern.Speichern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;

public class SpielStartLokal implements Serializable {

    //Attribute:
    //Spielfeldgroesse
    protected int spielfeldgr = 0;

    //fuer Schiffe setzen
    protected boolean direction = false;
    protected int length = 0;
    protected int x = 0;
    protected int y = 0;

    //fuer Checkboxen
    protected boolean cb2Selec = false;
    protected boolean cb3Selec = false;
    protected boolean cb4Selec = false;
    protected boolean cb5Selec = false;
    protected boolean cb6Selec = false;
    protected boolean placeship = true;


    //Index von Kaestchen in Spielfeld von Gegner Schiffen
    public static int index_zeile;
    public static int index_spalte;

    //Index von Kaestchen in Spielfeld von eigenen Schiffen
    protected int index_zeile_eig;
    protected int index_spalte_eig;


    //Nur einer der beiden RadioButtons kann ausgewaehlt werden
    //Hilfsvariablen
    protected boolean radioButton_l = false;
    protected boolean radioButton_o = false;

    public JTable table;
    int[] remainingships;
    public JTable table2;
    public JTable table3;
    public JTable table4;
    public Spieler Spieler1;
    public Spieler Spieler2;
    public int mapSize;



    public Object[][] drawShip(int initialX, int initialY, int length, boolean horizontal, Object[][] data){
        try{
            if(horizontal == true){
                int endX = initialX + length - 1;
                int currentX = initialX;
                while(currentX <= endX){
                    if(currentX == initialX){
                        data[currentX][initialY] = new ImageIcon(getClass().getResource("ShipVstart.png"));
                        currentX++;
                    }
                    else if(currentX == endX){
                        data[currentX][initialY] = new ImageIcon(getClass().getResource("ShipVend.png"));
                        currentX++;
                    }
                    else{
                        data[currentX][initialY] = new ImageIcon(getClass().getResource("ShipVmiddle.png"));
                        currentX++;
                    }
                }
            }
            else{
                int endY = initialY + length - 1;
                int currentY = initialY;
                while(currentY <= endY){
                    if(currentY == initialY){
                        data[initialX][currentY] = new ImageIcon(getClass().getResource("ShipHstart.png"));
                        currentY++;
                    }
                    else if(currentY == endY){
                        data[initialX][currentY] = new ImageIcon(getClass().getResource("ShipHend.png"));
                        currentY++;
                    }
                    else{
                        data[initialX][currentY] = new ImageIcon(getClass().getResource("ShipHmiddle.png"));
                        currentY++;
                    }
                }
            }
        }
        catch(Exception e){}
        return data;
    }

    public void SpielStartenLokal(AllWeNeed player1, AllWeNeed player2)
    {
        this.Spieler1 = player1.player;
        Spieler1.attackToken = true;
        System.out.println(Spieler1.name);
        this.Spieler2 = player2.player;
        Spieler2.attackToken = false;
        System.out.println(Spieler2.name);
        this.mapSize = Spieler1.mapSize;
        //Headers for JTable
        String[] columns = new String[player1.player.mapSize];
        for(int i=0; i<player1.player.mapSize; i++){
            columns[i] = "" + (i+1);
        }

        //Fuer Schiffe setzen:
        //String[][] feldSetzen = new String[model.getSpielfeld()][model.getSpielfeld()];
        Object[][] data = new Object[mapSize][mapSize];
        for(int i = 0; i<mapSize; i ++){
            for(int k = 0; k<mapSize; k++){
                if(Spieler1.board[i][k] != null && Spieler1.board[i][k] instanceof Ship){
                    int initialX = ((Ship) Spieler1.board[i][k]).initialX;
                    int initialY = ((Ship) Spieler1.board[i][k]).initialY;
                    int length = ((Ship) Spieler1.board[i][k]).length;
                    boolean horizontal = ((Ship) Spieler1.board[i][k]).initialD;          //horizontal == true
                    data = drawShip(initialX, initialY, length, horizontal, data);
                }
                else{
                    data[i][k] = new ImageIcon(getClass().getResource("water.png"));
                }
            }
        }

        Object[][] data3 = new Object[mapSize][mapSize];
        for(int i = 0; i<Spieler2.board.length; i ++){
            for(int k = 0; k<Spieler2.board.length; k++){
                if(Spieler2.board[i][k] != null && Spieler2.board[i][k] instanceof Ship){
                    int initialX = ((Ship) Spieler2.board[i][k]).initialX;
                    int initialY = ((Ship) Spieler2.board[i][k]).initialY;
                    int length = ((Ship) Spieler2.board[i][k]).length;
                    boolean horizontal = ((Ship) Spieler2.board[i][k]).initialD;          //horizontal == true
                    data3 = drawShip(initialX, initialY, length, horizontal, data3);
                }
                else{
                    data3[i][k] = new ImageIcon(getClass().getResource("water.png"));
                }
            }
        }

        //data for JTable in a 2D table
        Object[][] data2 = new Object[player1.player.mapSize][player1.player.mapSize];
        for(int j=0; j<player1.player.mapSize; j++){
            for(int k=0; k<player1.player.mapSize; k++){
                data2[j][k] = new ImageIcon(getClass().getResource("water.png"));
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columns);
        DefaultTableModel model2 = new DefaultTableModel(data2, columns);
        DefaultTableModel model3 = new DefaultTableModel(data3, columns);
        DefaultTableModel model4 = new DefaultTableModel(data2.clone(), columns);

        table = new JTable(model) {
            public Class getColumnClass(int column) {
                return ImageIcon.class;
            }
            public boolean isCellEditable(int row, int column){
                return false;
            }
            public int getRowHeight(){              //Cells are squares
                return this.getColumnModel().getColumn(0).getWidth();
            }
        };
        table2 = new JTable(model2) {
            public Class getColumnClass(int column) {
                return ImageIcon.class;
            }
            public boolean isCellEditable(int row, int column){
                return false;
            }
            public int getRowHeight(){              //Cells are squares
                return this.getColumnModel().getColumn(0).getWidth();
            }
        };
        table3 = new JTable(model3) {
            public Class getColumnClass(int column) {
                return ImageIcon.class;
            }
            public boolean isCellEditable(int row, int column){
                return false;
            }
            public int getRowHeight(){              //Cells are squares
                return this.getColumnModel().getColumn(0).getWidth();
            }
        };
        table4 = new JTable(model4) {
            public Class getColumnClass(int column) {
                return ImageIcon.class;
            }
            public boolean isCellEditable(int row, int column){
                return false;
            }
            public int getRowHeight(){              //Cells are squares
                return this.getColumnModel().getColumn(0).getWidth();
            }
        };

        table.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table2.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table3.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table4.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());

        JFrame frame = new JFrame("Schiffeversenken " + player1.player.name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Zwischenraum der Breite 50 oder mehr.


        frame.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 50 oder mehr.
        frame.add(Box.createHorizontalStrut(20));
        frame.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(table2);

        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table2.getSelectedRow();
                int selecCol = table2.getSelectedColumn();
                //feldSetzen[selecRow][selecRow] = "ship";
                //table2.setValueAt(new ImageIcon("src\\blue.png"), selecRow, selecCol);
                String a, b;
                try{
                    a = table2.getValueAt(selecRow,selecCol).toString();
                    b = getClass().getResource("water.png").toString();
                    if(player1.player.attackToken == true && a.equals(b)){
                        Spieler1.lastShotX = selecRow;
                        Spieler1.lastShotY = selecCol;
                        String answer = Spieler2.shootYourself(selecRow, selecCol);
                        if(answer.equals("answer 0")){
                            table2.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            table3.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            Spieler1.attackToken = false;
                            Spieler2.attackToken = true;
                        }
                        else if(answer.equals("answer 1")){
                            table2.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                            table3.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                        }
                        else if(answer.equals("answer 2")){
                            table2.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            table3.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            Spieler2.hp = Spieler2.hp - 1;
                            Spieler1.hp2 = Spieler1.hp2 - 1;
                            System.out.println(Spieler2.hp + " " + Spieler1.hp2);
                            if(Spieler2.hp == 0){
                                JOptionPane.showMessageDialog(frame, "Spieler 1 hat das Spiel gewonnen :)" );
                                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                            }
                        }
                    }
                }
                catch(Exception exc){}
                System.out.println("Tabelle2 " + selecRow + "," + selecCol);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table.getSelectedRow();
                int selecCol = table.getSelectedColumn();
                System.out.println("Tabelle1 " + selecRow + "," + selecCol);

            }
        });


        Box vbox_1 = Box.createVerticalBox();
        {
            JLabel label_gegner = new JLabel("Eigene Schiffe");
            vbox_1.add(label_gegner);

            table.setRowHeight(120);
            table.setSize(750, 750);

            vbox_1.add(scrollPane);
        }

        frame.add(vbox_1);


        frame.add(Box.createHorizontalStrut(20));
        frame.add(Box.createHorizontalGlue());


        Box vbox_2 = Box.createVerticalBox();
        {
            JLabel label_eigene = new JLabel("Gegner Schiffe");
            vbox_2.add(label_eigene);

            table2.setRowHeight(120);//setzt Höhe der einzelnen Zeilen
            table.setSize(750, 750);

            vbox_2.add(scrollPane2);

        }

        frame.add(vbox_2);

        frame.add(Box.createHorizontalStrut(10));
        frame.add(Box.createHorizontalGlue());

        JButton speichern = new JButton("Spiel speichern");

        Box vbox_3 = Box.createVerticalBox();
        {
            speichern.setBackground(Color.CYAN);


            vbox_3.add(speichern);

        }

        frame.add(vbox_3);

        speichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        frame.add(Box.createHorizontalStrut(10));
        frame.add(Box.createHorizontalGlue());


        // Menüzeile (JMenuBar) erzeugen und einzelne Menüs (JMenu)
        // mit Menüpunkten (JMenuItem) hinzufügen.
        //ist jetzt bei startbildschirm (siehe startbildschirm)
        JMenuBar bar = new JMenuBar();
        {
            JMenu menu = new JMenu("Programm");
            {
                JMenuItem item = new JMenuItem("Beenden");
                item.addActionListener(
                        (e) -> {
                            System.exit(0);
                        }
                );
                menu.add(item);
            }
            bar.add(menu);
        }
        {
            JMenu menu = new JMenu("Tabelle");
            {
                JMenuItem item = new JMenuItem("Ausgeben");
                item.addActionListener(
                        (e) -> {
                            //model.dump();
                        }
                );
                menu.add(item);
            }
            bar.add(menu);
        }

        // Menüzeile zum Fenster hinzufügen.
        frame.setJMenuBar(bar);

        frame.pack();

        //Fenster wird im Vollbildmodus geoeffnet
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //macht Fenster sichtbar
        frame.setVisible(true);


        JFrame frame2 = new JFrame("Schiffeversenken " + player2.player.name);

        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Zwischenraum der Breite 50 oder mehr.


        frame2.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 50 oder mehr.
        frame2.add(Box.createHorizontalStrut(20));
        frame2.add(Box.createHorizontalGlue());

        JScrollPane scrollPane3 = new JScrollPane(table3);
        JScrollPane scrollPane4 = new JScrollPane(table4);

        table4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table4.getSelectedRow();
                int selecCol = table4.getSelectedColumn();
                //feldSetzen[selecRow][selecRow] = "ship";
                //table2.setValueAt(new ImageIcon("src\\blue.png"), selecRow, selecCol);
                String a, b;
                try{
                    a = table4.getValueAt(selecRow,selecCol).toString();
                    b = getClass().getResource("water.png").toString();
                    if(player2.player.attackToken == true && a.equals(b)){
                        Spieler2.lastShotX = selecRow;
                        Spieler2.lastShotY = selecCol;
                        String answer = Spieler1.shootYourself(selecRow, selecCol);
                        if(answer.equals("answer 0")){
                            table.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            table4.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            Spieler2.attackToken = false;
                            Spieler1.attackToken = true;
                        }
                        if(answer.equals("answer 1")){
                            table.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                            table4.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                        }
                        if(answer.equals("answer 2")){
                            table.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            table4.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            Spieler1.hp = Spieler1.hp - 1;
                            Spieler2.hp2 = Spieler2.hp2 - 1;
                            System.out.println(Spieler1.hp + " " + Spieler2.hp2);
                            if(Spieler1.hp == 0){
                                JOptionPane.showMessageDialog(frame, "Spieler 2 hat das Spiel gewonnen :)" );
                                frame2.dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                            }
                        }
                    }
                }
                catch(Exception exc){}
                System.out.println("Tabelle4 " + selecRow + "," + selecCol);
            }
        });

        table3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table3.getSelectedRow();
                int selecCol = table3.getSelectedColumn();
                System.out.println("Tabelle3 " + selecRow + "," + selecCol);

            }
        });


        Box vbox_10 = Box.createVerticalBox();
        {
            JLabel label_gegner = new JLabel("Eigene Schiffe");
            vbox_10.add(label_gegner);

            table3.setRowHeight(120);
            table3.setSize(750, 750);

            vbox_10.add(scrollPane3);
        }

        frame2.add(vbox_10);


        frame2.add(Box.createHorizontalStrut(20));
        frame2.add(Box.createHorizontalGlue());


        Box vbox_20 = Box.createVerticalBox();
        {
            JLabel label_eigene = new JLabel("Gegner Schiffe");
            vbox_20.add(label_eigene);

            table4.setRowHeight(120);//setzt Höhe der einzelnen Zeilen
            table3.setSize(750, 750);

            vbox_20.add(scrollPane4);

        }

        frame2.add(vbox_20);

        frame2.add(Box.createHorizontalStrut(10));
        frame2.add(Box.createHorizontalGlue());

        JButton speichernn = new JButton("Spiel speichern");

        Box vbox_30 = Box.createVerticalBox();
        {
            speichernn.setBackground(Color.CYAN);


            vbox_30.add(speichernn);

        }

        frame2.add(vbox_30);

        speichernn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        frame2.add(Box.createHorizontalStrut(10));
        frame2.add(Box.createHorizontalGlue());


        // Menüzeile (JMenuBar) erzeugen und einzelne Menüs (JMenu)
        // mit Menüpunkten (JMenuItem) hinzufügen.
        //ist jetzt bei startbildschirm (siehe startbildschirm)
        JMenuBar barr = new JMenuBar();
        {
            JMenu menu = new JMenu("Programm");
            {
                JMenuItem item = new JMenuItem("Beenden");
                item.addActionListener(
                        (e) -> {
                            System.exit(0);
                        }
                );
                menu.add(item);
            }
            barr.add(menu);
        }
        {
            JMenu menu = new JMenu("Tabelle");
            {
                JMenuItem item = new JMenuItem("Ausgeben");
                item.addActionListener(
                        (e) -> {
                            //model.dump();
                        }
                );
                menu.add(item);
            }
            barr.add(menu);
        }

        // Menüzeile zum Fenster hinzufügen.
        frame2.setJMenuBar(barr);

        frame2.pack();

        //Fenster wird im Vollbildmodus geoeffnet
        frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //macht Fenster sichtbar
        frame2.setVisible(true);
    }


}
