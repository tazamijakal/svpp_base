package GUI;

import KI.*;
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
        if(player1 != null && player2 != null){
            this.Spieler1 = player1.player;
            Spieler1.attackToken = true;
            System.out.println(Spieler1.name);
            this.Spieler2 = player2.player;
            Spieler2.attackToken = false;
            System.out.println(Spieler2.name);
            this.mapSize = Spieler1.mapSize;
        }

        if(table == null) {


            //Headers for JTable
            String[] columns = new String[mapSize];
            for (int i = 0; i < mapSize; i++) {
                columns[i] = "" + (i + 1);
            }

            //Fuer Schiffe setzen:
            //String[][] feldSetzen = new String[model.getSpielfeld()][model.getSpielfeld()];
            Object[][] data = new Object[mapSize][mapSize];
            for (int i = 0; i < mapSize; i++) {
                for (int k = 0; k < mapSize; k++) {
                    if (Spieler1.board[i][k] != null && Spieler1.board[i][k] instanceof Ship) {
                        int initialX = ((Ship) Spieler1.board[i][k]).initialX;
                        int initialY = ((Ship) Spieler1.board[i][k]).initialY;
                        int length = ((Ship) Spieler1.board[i][k]).length;
                        boolean horizontal = ((Ship) Spieler1.board[i][k]).initialD;          //horizontal == true
                        data = drawShip(initialX, initialY, length, horizontal, data);
                    } else {
                        data[i][k] = new ImageIcon(getClass().getResource("water.png"));
                    }
                }
            }

            Object[][] data3 = new Object[mapSize][mapSize];
            for (int i = 0; i < Spieler2.board.length; i++) {
                for (int k = 0; k < Spieler2.board.length; k++) {
                    if (Spieler2.board[i][k] != null && Spieler2.board[i][k] instanceof Ship) {
                        int initialX = ((Ship) Spieler2.board[i][k]).initialX;
                        int initialY = ((Ship) Spieler2.board[i][k]).initialY;
                        int length = ((Ship) Spieler2.board[i][k]).length;
                        boolean horizontal = ((Ship) Spieler2.board[i][k]).initialD;          //horizontal == true
                        data3 = drawShip(initialX, initialY, length, horizontal, data3);
                    } else {
                        data3[i][k] = new ImageIcon(getClass().getResource("water.png"));
                    }
                }
            }

            //data for JTable in a 2D table
            Object[][] data2 = new Object[mapSize][mapSize];
            for (int j = 0; j < mapSize; j++) {
                for (int k = 0; k < mapSize; k++) {
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

                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                public int getRowHeight() {              //Cells are squares
                    return this.getColumnModel().getColumn(0).getWidth();
                }
            };
            table2 = new JTable(model2) {
                public Class getColumnClass(int column) {
                    return ImageIcon.class;
                }

                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                public int getRowHeight() {              //Cells are squares
                    return this.getColumnModel().getColumn(0).getWidth();
                }
            };
            table3 = new JTable(model3) {
                public Class getColumnClass(int column) {
                    return ImageIcon.class;
                }

                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                public int getRowHeight() {              //Cells are squares
                    return this.getColumnModel().getColumn(0).getWidth();
                }
            };
            table4 = new JTable(model4) {
                public Class getColumnClass(int column) {
                    return ImageIcon.class;
                }

                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                public int getRowHeight() {              //Cells are squares
                    return this.getColumnModel().getColumn(0).getWidth();
                }
            };
        }
        table.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table2.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table3.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table4.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table.getTableHeader().setReorderingAllowed(false);
        table2.getTableHeader().setReorderingAllowed(false);
        table3.getTableHeader().setReorderingAllowed(false);
        table4.getTableHeader().setReorderingAllowed(false);
        JFrame frame = new JFrame("Schiffeversenken " + Spieler1.name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Zwischenraum der Breite 50 oder mehr.
        JFrame frame2 = new JFrame("Schiffeversenken " + Spieler2.name);

        frame.setIconImage(new ImageIcon(getClass().getResource("PirateBay.png")).getImage());
        frame2.setIconImage(new ImageIcon(getClass().getResource("PirateBay.png")).getImage());

        frame.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 50 oder mehr.
        frame.add(Box.createHorizontalStrut(20));
        frame.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(table2);

        JButton spieler1_frame = new JButton("AM ZUG");
        JButton spieler2_frame = new JButton("AM ZUG");
        JLabel amzug1 = new JLabel("");
        JLabel amzug2 = new JLabel("");

        spieler1_frame.setBackground(Color.GREEN);
        spieler2_frame.setBackground(Color.RED);


        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table2.getSelectedRow();
                int selecCol = table2.getSelectedColumn();
                //feldSetzen[selecRow][selecRow] = "ship";
                //table2.setValueAt(new ImageIcon("src\\blue.png"), selecRow, selecCol);
                String a, b;
                try {
                    a = table2.getValueAt(selecRow, selecCol).toString();
                    b = getClass().getResource("water.png").toString();
                    if (Spieler1.attackToken == true && a.equals(b)) {
                        Spieler1.lastShotX = selecRow;
                        Spieler1.lastShotY = selecCol;

                        String answer = Spieler2.shootYourself(selecRow, selecCol);
                        if (answer.equals("answer 0")) {
                            Runnable k = new Runnable() {
                                public void run() {
                                    AudioPlayer MusicPlayer = new AudioPlayer();
                                    MusicPlayer.Soundplay(getClass().getResource("Music/water.wav"));
                            }
                        };

                            new Thread(k).start();
                            table2.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            table3.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            spieler1_frame.setBackground(Color.RED);
                            spieler2_frame.setBackground(Color.GREEN);
                            frame.repaint();
                            frame2.repaint();
                            Spieler1.attackToken = false;
                            Spieler2.attackToken = true;
                            if (Spieler2.name.equals("KI_leicht") || Spieler2.name.equals("KI_mittel")) {
                                while (Spieler2.attackToken == true) {
                                    if (Spieler2.name.equals("KI_leicht")) {
                                        ((leichte_KI_zufall) Spieler2).KIshoot();
                                        selecRow = ((leichte_KI_zufall) Spieler2).testx;
                                        selecCol = ((leichte_KI_zufall) Spieler2).testy;
                                    }
                                    if (Spieler2.name.equals("KI_mittel")) {
                                        ((mittlere_KI) Spieler2).KIshoot();
                                        selecRow = ((mittlere_KI) Spieler2).testx;
                                        selecCol = ((mittlere_KI) Spieler2).testy;
                                    }
                                    String answer2 = Spieler1.shootYourself(selecRow, selecCol);
                                    if (answer2.equals("answer 0")) {
                                        Runnable j = new Runnable() {
                                            public void run() {
                                                AudioPlayer MusicPlayer = new AudioPlayer();
                                                MusicPlayer.Soundplay(getClass().getResource("Music/water.wav"));
                                            }
                                        };

                                        new Thread(j).start();
                                        Spieler2.visibleBoard[selecRow][selecCol] = new Spieler.MisfireObject();
                                        table.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                                        table4.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                                        spieler1_frame.setBackground(Color.GREEN);
                                        spieler2_frame.setBackground(Color.RED);
                                        frame2.repaint();
                                        frame.repaint();
                                        Spieler2.attackToken = false;
                                        Spieler1.attackToken = true;
                                    }
                                    if (answer2.equals("answer 1")) {
                                        Runnable j = new Runnable() {
                                            public void run() {
                                                AudioPlayer MusicPlayer = new AudioPlayer();
                                                MusicPlayer.Soundplay(getClass().getResource("Music/explode.wav"));
                                            }
                                        };

                                        new Thread(j).start();
                                        table.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                                        table4.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                                        Spieler2.visibleBoard[selecRow][selecCol] = new Spieler.TrefferObject();
                                    }
                                    if (answer2.equals("answer 2")) {
                                        Runnable j = new Runnable() {
                                            public void run() {
                                                AudioPlayer MusicPlayer = new AudioPlayer();
                                                MusicPlayer.Soundplay(getClass().getResource("Music/explode.wav"));
                                            }
                                        };

                                        new Thread(j).start();
                                        table.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                                        table4.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                                        Spieler1.hp = Spieler1.hp - 1;
                                        Spieler2.hp2 = Spieler2.hp2 - 1;
                                        System.out.println(Spieler1.hp + " " + Spieler2.hp2);
                                        Spieler2.visibleBoard[selecRow][selecCol] = new Spieler.TrefferObject();
                                        if (Spieler1.hp == 0) {
                                            JOptionPane.showMessageDialog(frame, Spieler2.name + " hat das Spiel gewonnen :)");
                                            frame2.dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                                        }
                                    }
                                }
                            }
                        }
                        else if(answer.equals("answer 1")){
                            Runnable k = new Runnable() {
                                public void run() {
                                    AudioPlayer MusicPlayer = new AudioPlayer();
                                    MusicPlayer.Soundplay(getClass().getResource("Music/explode.wav"));
                                }
                            };

                            new Thread(k).start();
                            table2.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                            table3.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                        }
                        else if(answer.equals("answer 2")){
                            Runnable k = new Runnable() {
                                public void run() {
                                    AudioPlayer MusicPlayer = new AudioPlayer();
                                    MusicPlayer.Soundplay(getClass().getResource("Music/explode.wav"));
                                }
                            };

                            new Thread(k).start();
                            table2.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            table3.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            Spieler2.hp = Spieler2.hp - 1;
                            Spieler1.hp2 = Spieler1.hp2 - 1;
                            System.out.println(Spieler2.hp + " " + Spieler1.hp2);
                            if(Spieler2.hp == 0){
                                Runnable w = new Runnable() {
                                    public void run() {
                                        AudioPlayer MusicPlayer = new AudioPlayer();
                                        MusicPlayer.Soundplay(getClass().getResource("Music/csgo.wav"));
                                    }
                                };
                                new Thread(w).start();
                                JOptionPane.showMessageDialog(frame, "Spieler 1 hat das Spiel gewonnen :)" );
                                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                            }
                        }
                    }

                    else if(Spieler1.attackToken == false){
                        SwingWorker<Void, Void> sw33 = new SwingWorker<Void, Void>(){
                            @Override
                            protected Void doInBackground() throws Exception {
                                amzug1.setText("Waiting for Opponent!");
                                Thread.sleep(2000);
                                amzug1.setText("");
                                return null;
                            }
                        };
                        sw33.execute();
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

            vbox_3.add(spieler1_frame);

            vbox_3.add(speichern);

            vbox_3.add(amzug1);

        }

        frame.add(vbox_3);

        speichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Spieler1.attackToken == true){
                    String filename = "" + AllWeNeed.nextId();
                    AllWeNeed newsave = new AllWeNeed(true, Spieler1, Spieler2, table, table2, table3, table4, filename);              //Speichern fuer Online versus
                    SwingUtilities.invokeLater(() -> {
                        try {
                            System.out.println("speichern: ");
                            Speichern.save(newsave, filename);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
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


        //JFrame frame2 = new JFrame("Schiffeversenken " + player2.player.name);

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
                    if(Spieler2.attackToken == true && a.equals(b)){
                        Spieler2.lastShotX = selecRow;
                        Spieler2.lastShotY = selecCol;
                        String answer = Spieler1.shootYourself(selecRow, selecCol);
                        if(answer.equals("answer 0")){
                            Runnable k = new Runnable() {
                                public void run() {
                                    AudioPlayer MusicPlayer = new AudioPlayer();
                                    MusicPlayer.Soundplay(getClass().getResource("Music/water.wav"));
                                }
                            };

                            new Thread(k).start();
                            table.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            table4.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
                            spieler1_frame.setBackground(Color.GREEN);
                            spieler2_frame.setBackground(Color.RED);
                            frame2.repaint();
                            frame.repaint();
                            Spieler2.attackToken = false;
                            Spieler1.attackToken = true;
                        }
                        if(answer.equals("answer 1")){
                            Runnable k = new Runnable() {
                                public void run() {
                                    AudioPlayer MusicPlayer = new AudioPlayer();
                                    MusicPlayer.Soundplay(getClass().getResource("Music/explode.wav"));
                                }
                            };

                            new Thread(k).start();
                            table.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                            table4.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), selecRow, selecCol);
                        }
                        if(answer.equals("answer 2")){
                            Runnable k = new Runnable() {
                                public void run() {
                                    AudioPlayer MusicPlayer = new AudioPlayer();
                                    MusicPlayer.Soundplay(getClass().getResource("Music/explode"));
                                }
                            };
                            new Thread(k).start();
                            table.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            table4.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), selecRow, selecCol);
                            Spieler1.hp = Spieler1.hp - 1;
                            Spieler2.hp2 = Spieler2.hp2 - 1;
                            System.out.println(Spieler1.hp + " " + Spieler2.hp2);
                            if(Spieler1.hp == 0){
                                Runnable w = new Runnable() {
                                    public void run() {
                                        AudioPlayer MusicPlayer = new AudioPlayer();
                                        MusicPlayer.Soundplay(getClass().getResource("Music/csgo.wav"));
                                    }
                                };
                                new Thread(w).start();
                                JOptionPane.showMessageDialog(frame, "Spieler 2 hat das Spiel gewonnen :)" );
                                frame2.dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                            }
                        }
                    }
                    else if(Spieler2.attackToken == false){
                        SwingWorker<Void, Void> sw33 = new SwingWorker<Void, Void>(){
                            @Override
                            protected Void doInBackground() throws Exception {
                                amzug2.setText("Waiting for Opponent!");
                                Thread.sleep(2000);
                                amzug2.setText("");
                                return null;
                            }
                        };
                        sw33.execute();
                    }
                }
                catch(Exception exc){
                    exc.printStackTrace();
                }
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

            vbox_30.add(spieler2_frame);
            speichernn.setBackground(Color.CYAN);
            vbox_30.add(speichernn);
            vbox_30.add(amzug2);
        }

        frame2.add(vbox_30);

        speichernn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Spieler2.attackToken == true){
                    String filename = "" + AllWeNeed.nextId();
                    AllWeNeed newsave = new AllWeNeed(true, Spieler1, Spieler2, table, table2, table3, table4, filename);              //Speichern fuer Online versus
                    SwingUtilities.invokeLater(() -> {
                        try {
                            Speichern.save(newsave, filename);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
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
