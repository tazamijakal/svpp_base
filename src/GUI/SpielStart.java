package GUI;

import Logik.Ship;
import Logik.Spieler;
import Socket.Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public final class SpielStart extends JFrame{

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

    public static JTable table;

    int[] remainingships;
    public static JTable table2;

    public Spieler player;
    public SpielStart(){

    }


    public void sethorizShip(int x, int y)
    {

    }

    public void setTable2CellBLUE(int x, int y){
        table2.setValueAt(new ImageIcon(getClass().getResource("blue.png")), x, y);
    }


    /**
     *
     * Setzt Spaltenindex von einzelnem Kaestchen von Gegner Spielfeld auf uebergebene Groesse.
     * Index geginnt bei 0 zu zaehlen.
     * Hier fuer Spielfeld mit Gegner Schiffe.
     */
    public void setIndex_spalte (int groesse) {

        this.index_spalte = groesse;

    }

    public Object[][] drawShip(int initialX, int initialY, int length, boolean horizontal, Object[][] data){
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
        return data;
    }


    /**
     *
     * Graphische Oberfläche aufbauen und anzeigen.
     * Fuer eigentlichen Spielstart.
     * Nachdem Einstellungen in Startbildschirm gemacht wurden.
     */
    public void SpielStarten(Spieler player)
    {
        //Fuer Schiffe setzen:
        //String[][] feldSetzen = new String[model.getSpielfeld()][model.getSpielfeld()];
        Object[][] data = new Object[player.mapSize][player.mapSize];
        for(int i = 0; i<player.board.length; i ++){
            for(int k = 0; k<player.board.length; k++){
                if(player.board[i][k] instanceof Ship){
                    int initialX = ((Ship) player.board[i][k]).initialX;
                    int initialY = ((Ship) player.board[i][k]).initialY;
                    int length = ((Ship) player.board[i][k]).length;
                    boolean horizontal = ((Ship) player.board[i][k]).initialD;          //horizontal == true
                    data = drawShip(initialX, initialY, length, horizontal, data);
                }
                else{
                    data[i][k] = new ImageIcon(getClass().getResource("water.png"));
                }
            }
        }

        //Headers for JTable
        String[] columns = new String[player.mapSize];
        for(int i=0; i<player.mapSize; i++){
            columns[i] = "" + (i+1);
        }

        //data for JTable in a 2D table
        Object[][] data2 = new Object[player.mapSize][player.mapSize];
        for(int j=0; j<player.mapSize; j++){
            for(int k=0; k<player.mapSize; k++){
                data2[j][k] = new ImageIcon(getClass().getResource("water.png"));
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columns);
        DefaultTableModel model2 = new DefaultTableModel(data2, columns);

        JTable table = new JTable(model) {
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
        JTable table2 = new JTable(model2) {
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
        this.table = table;
        this.table2 = table2;

        JFrame frame = new JFrame("Schiffeversenken " + player.name);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Zwischenraum der Breite 50 oder mehr.
        //frame.add(Box.createHorizontalStrut(50));
        //frame.add(Box.createHorizontalGlue());

        // Menüzeile zum Fenster hinzufügen.
        //frame.setJMenuBar(bar);
        //startbildschirm.setJMenuBar(bar);


        frame.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 50 oder mehr.
        frame.add(Box.createHorizontalStrut(20));
        frame.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(this.table);
        JScrollPane scrollPane2 = new JScrollPane(this.table2);

        //table.setDropMode(DropMode.USE_SELECTION);
        //table.getToolTipLocation()
        //table.isCellEditable()
        //table.isCellSelected();
        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table2.getSelectedRow();
                int selecCol = table2.getSelectedColumn();
                //feldSetzen[selecRow][selecRow] = "ship";
                //table2.setValueAt(new ImageIcon("src\\blue.png"), selecRow, selecCol);
                if(player.board[selecRow][selecCol] instanceof Spieler.MisfireObject || player.board[selecRow][selecCol] instanceof Spieler.TrefferObject){
                    System.out.println("do nothing");
                }
                else if(player.attackToken == true){
                    if(player.name.equals("Server")){
                        System.out.println(table2.getValueAt(selecRow, selecCol));
                        System.out.println(getClass().getResource("blue.png"));
                        player.attackToken = false;
                        player.lastShotX = selecRow;
                        player.lastShotY = selecCol;
                        player.server.TextClient("shot " + selecRow + " " + selecCol);
                    }
                    if(player.name.equals("Client")){
                        System.out.println(table2.getValueAt(selecRow, selecCol));
                        player.attackToken = false;
                        player.lastShotX = selecRow;
                        player.lastShotY = selecCol;
                        player.client.TextServer("shot " + selecRow + " " + selecCol);
                    }
                }
                System.out.println("Tabelle2 " + selecRow + "," + selecCol);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table.getSelectedRow();
                int selecCol = table.getSelectedColumn();

                //feldSetzen[selecRow][selecRow] = "ship";
                //table.setValueAt("Ship", selecRow, selecCol);
                table.setValueAt(new ImageIcon(getClass().getResource("blue.png")), selecRow, selecCol);
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

        frame.add(Box.createHorizontalStrut(50));
        frame.add(Box.createHorizontalGlue());


        //Buttons werden fuer eigentliches Spiel nicht gebraucht, Schiffe werden davor gesetzt
        /*Box vbox_4 = Box.createVerticalBox();
        {

        }
        vbox_4.add(Box.createVerticalGlue());

        vbox_4.add(Box.createVerticalStrut(20));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            JButton delete = new JButton("Schiffe entfernen");
            delete.setAlignmentX(Component.CENTER_ALIGNMENT);

            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    int selecRow_remove = table2.getSelectedRow();
                    int selecCol_remove = table2.getSelectedColumn();


                    //feldSetzen[selecRow][selecRow] = "ship";
                    table2.setValueAt("", selecRow_remove, selecCol_remove);

                    //System.out.println("Tabelle1 " + selecRow_remove + "," + selecCol_remove);

                }
            });



            vbox_4.add(delete);

            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());


        }

        vbox_4.add(Box.createVerticalStrut(10));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            JButton zufall = new JButton("Schiffe zufaellig");
            zufall.setAlignmentX(Component.CENTER_ALIGNMENT);
            vbox_4.add(zufall);

            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }


        vbox_4.add(Box.createVerticalStrut(10));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            JButton feld_gegner = new JButton("Feld Platz Gegner");
            feld_gegner.setAlignmentX(Component.CENTER_ALIGNMENT);
            vbox_4.add(feld_gegner);

            feld_gegner.addActionListener(new ActionListener() {


                @Override
                public void actionPerformed(ActionEvent e) {
                    SpielStart.index_spalte = table.getSelectedColumn();
                    SpielStart.index_zeile = table.getSelectedRow();
                    System.out.println("Tabelle1 " + SpielStart.index_spalte + "," + SpielStart.index_zeile);

                }


            });

            JButton feld_eigene = new JButton("Feld Platz Eigene");
            feld_eigene.setAlignmentX(Component.CENTER_ALIGNMENT);
            vbox_4.add(feld_eigene);

            feld_eigene.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    SpielStart.index_spalte = table2.getSelectedColumn();
                    SpielStart.index_zeile = table2.getSelectedRow();

                    System.out.println("Tabelle1 " + SpielStart.index_spalte + "," + SpielStart.index_zeile);

                }
            });


            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }

        frame.add(vbox_4);

        vbox_4.add(Box.createHorizontalStrut(10));
        vbox_4.add(Box.createHorizontalGlue());*/


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

    }


    /**
     *
     * Graphische Oberfläche aufbauen und anzeigen.
     * Um Schiffe zu setzen.
     * Nachdem Einstellungen in Startbildschirm gemacht wurden.
     */
    public void Setzen(Spieler player)
    {
        remainingships = player.remainingShips.clone();
        boolean allshipsareplaced = true;
        //Headers for JTable
        String[] columns = new String[player.mapSize];
        for(int i=0; i<player.mapSize; i++){
            columns[i] = "" + (i+1);
        }

        //data for JTable in a 2D table
        Object[][] data = new Object[player.mapSize][player.mapSize];
        for(int j=0; j<player.mapSize; j++){
            for(int k=0; k<player.mapSize; k++){
                data[j][k] = new ImageIcon(getClass().getResource("water.png"));
            }
        }


        DefaultTableModel model_setzen = new DefaultTableModel(data, columns);


        this.table = new JTable(model_setzen) {
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

        /*if(radioButton_l)
        {
            if(!Startbildschirm.p1 && !Startbildschirm.p2)
            {
                Startbildschirm.p1 = true;
            }
            if(Startbildschirm.p1 && !Startbildschirm.p2)
            {
                Startbildschirm.p2 = true;
                SwingUtilities.invokeLater(() -> {Setzen(Startbildschirm.sp2);});

            }

        }*/




        JFrame setzen = new JFrame("Schiffeversenken " + player.name);

        setzen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Zwischenraum der Breite 50 oder mehr.
        //frame.add(Box.createHorizontalStrut(50));
        //frame.add(Box.createHorizontalGlue());

        // Menüzeile zum Fenster hinzufügen.
        //frame.setJMenuBar(bar);
        //startbildschirm.setJMenuBar(bar);


        setzen.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 50 oder mehr.
        setzen.add(Box.createHorizontalStrut(20));
        setzen.add(Box.createHorizontalGlue());


        JScrollPane scrollPane_setzen = new JScrollPane(this.table);



        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                x = table.getSelectedRow();
                y = table.getSelectedColumn();


                //Code anpassen fuer anklicken Schiffe platzieren
                if(player.spaceCheck(x,y,length,direction)){
                    player.placeRemoveShip(true, x, y, length, direction);

                    Object[][] data = new Object[player.mapSize][player.mapSize];
                    for(int i = 0; i<player.board.length; i ++){
                        for(int k = 0; k<player.board.length; k++){
                            if(player.board[i][k] instanceof Ship){
                                int initialX = ((Ship) player.board[i][k]).initialX;
                                int initialY = ((Ship) player.board[i][k]).initialY;
                                int length = ((Ship) player.board[i][k]).length;
                                boolean horizontal = ((Ship) player.board[i][k]).initialD;          //horizontal == true
                                data = drawShip(initialX, initialY, length, horizontal, data);
                            }
                            else{
                                data[i][k] = new ImageIcon(getClass().getResource("water.png"));
                            }
                        }
                    }

                    DefaultTableModel model_set = new DefaultTableModel(data, columns);

                    table.setModel(model_set);
                    table.repaint();
                }
            }
        });


        Box vbox_1 = Box.createVerticalBox();
        {

            JLabel label_setzen = new JLabel("Hier werden die Schiffe gesetzt");
            vbox_1.add(label_setzen);

            table.setRowHeight(120);
            table.setSize(750, 750);

            vbox_1.add(scrollPane_setzen);

        }

        setzen.add(vbox_1);


        setzen.add(Box.createHorizontalStrut(20));
        setzen.add(Box.createHorizontalGlue());




        Box vbox_4 = Box.createVerticalBox();
        {

        }
        vbox_4.add(Box.createVerticalGlue());

        vbox_4.add(Box.createVerticalStrut(20));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            JButton delete = new JButton("Schiffe entfernen");
            delete.setAlignmentX(Component.CENTER_ALIGNMENT);

            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //int selecRow_remove = table2.getSelectedRow();
                    //int selecCol_remove = table2.getSelectedColumn();


                    //feldSetzen[selecRow][selecRow] = "ship";
                    //table2.setValueAt("", selecRow_remove, selecCol_remove);

                    //System.out.println("Tabelle1 " + selecRow_remove + "," + selecCol_remove);

                }
            });



            vbox_4.add(delete);

            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());


        }

        vbox_4.add(Box.createVerticalStrut(10));
        {
            JButton zufall = new JButton("Schiffe zufaellig");
            zufall.setAlignmentX(Component.CENTER_ALIGNMENT);
            zufall.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player.resetplayer(remainingships);
                    SwingWorker<Void, Void> sw10 = new SwingWorker<Void, Void>(){
                        @Override
                        protected Void doInBackground() throws Exception {
                            zufall.setEnabled(false);
                            player.placerandom();
                            Object[][] data = new Object[player.mapSize][player.mapSize];
                            for(int i = 0; i<player.board.length; i ++){
                                for(int k = 0; k<player.board.length; k++){
                                    if(player.board[i][k] instanceof Ship){
                                        int initialX = ((Ship) player.board[i][k]).initialX;
                                        int initialY = ((Ship) player.board[i][k]).initialY;
                                        int length = ((Ship) player.board[i][k]).length;
                                        boolean horizontal = ((Ship) player.board[i][k]).initialD;          //horizontal == true
                                        data = drawShip(initialX, initialY, length, horizontal, data);
                                    }
                                    else{
                                        data[i][k] = new ImageIcon(getClass().getResource("water.png"));
                                    }
                                }
                            }

                            DefaultTableModel model_set = new DefaultTableModel(data, columns);

                            table.setModel(model_set);
                            table.repaint();
                            zufall.setEnabled(true);
                            return null;
                        }
                    };
                    sw10.execute();
                }
            });

            vbox_4.add(zufall);

            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }


        vbox_4.add(Box.createVerticalStrut(10));
        {

            //CheckBox:(fuer Auswahl Schiffsgroesse)
            JCheckBox cb_2 = new JCheckBox("2er Schiffe");
            cb_2.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_3 = new JCheckBox("3er Schiffe");
            cb_3.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_4 = new JCheckBox("4er Schiffe");
            cb_4.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_5 = new JCheckBox("5er Schiffe");
            cb_5.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_6 = new JCheckBox("6er Schiffe");
            cb_6.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Listener fuer Checkboxen


            cb_2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    //JCheckBox cb2_source = (JCheckBox) e.getSource();//Objekt das verändert wurde
                    //Cast auf JCheckBox
                    if(cb_2.isSelected())
                    {
                        length = 2;
                        cb_3.setSelected(false);
                        cb_4.setSelected(false);
                        cb_5.setSelected(false);
                        cb_6.setSelected(false);
                    }
                    else
                    {
                        length = 0;
                    }
                }
            });



            cb_3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //JCheckBox cb2_source = (JCheckBox) e.getSource();//Objekt das verändert wurde
                    //Cast auf JCheckBox

                    if(cb_3.isSelected()) {
                        length = 3;
                        cb_2.setSelected(false);
                        cb_4.setSelected(false);
                        cb_5.setSelected(false);
                        cb_6.setSelected(false);
                    }
                    else {
                        length = 0;
                    }
                }
            });

            cb_4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    //JCheckBox cb2_source = (JCheckBox) e.getSource();//Objekt das verändert wurde
                    //Cast auf JCheckBox

                       if(cb_4.isSelected())
                       {
                           length = 4;
                           cb_2.setSelected(false);
                           cb_3.setSelected(false);
                           cb_5.setSelected(false);
                           cb_6.setSelected(false);
                       }
                       else
                       {
                            length = 0;
                       }
                    }
                });

            cb_5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    //JCheckBox cb2_source = (JCheckBox) e.getSource();//Objekt das verändert wurde
                    //Cast auf JCheckBox

                    if(cb_5.isSelected())
                    {
                        length = 5;
                        cb_2.setSelected(false);
                        cb_3.setSelected(false);
                        cb_4.setSelected(false);
                        cb_6.setSelected(false);
                    }
                    else
                    {
                        length = 0;
                    }
                }
            });

            cb_6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    //JCheckBox cb2_source = (JCheckBox) e.getSource();//Objekt das verändert wurde
                    //Cast auf JCheckBox

                    if(cb_6.isSelected())
                    {
                        length = 6;
                        cb_2.setSelected(false);
                        cb_3.setSelected(false);
                        cb_4.setSelected(false);
                        cb_5.setSelected(false);
                    }
                    else
                    {
                        length = 0;
                    }
                }
            });


            //zufall.setAlignmentX(Component.CENTER_ALIGNMENT);
            //Checkboxen zu vertikaler Box hinzufuegen
            vbox_4.add(cb_2);
            vbox_4.add(cb_3);
            vbox_4.add(cb_4);
            vbox_4.add(cb_5);
            vbox_4.add(cb_6);



            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }


        JButton horizontal = new JButton("Horizontal");
        JButton vertikal = new JButton("Vertikal");

        vbox_4.add(Box.createVerticalStrut(10));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            horizontal.setAlignmentX(Component.CENTER_ALIGNMENT);

            horizontal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    direction = true;
                    vertikal.setEnabled(true);
                    horizontal.setEnabled(false);
                }
            });
            vbox_4.add(horizontal);

            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }

        vbox_4.add(Box.createVerticalStrut(10));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            vertikal.setAlignmentX(Component.CENTER_ALIGNMENT);

            vertikal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    direction = false;
                    vertikal.setEnabled(false);
                    horizontal.setEnabled(true);
                }
            });
            vbox_4.add(vertikal);

            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }







        vbox_4.add(Box.createVerticalStrut(10));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            JButton beginn = new JButton("Spiel beginnen!");
            beginn.setAlignmentX(Component.CENTER_ALIGNMENT);

            beginn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(allshipsareplaced == true){
                        setzen.setVisible(false);
                        if(radioButton_l && Startbildschirm.p1)
                        {


                        }
                        SwingUtilities.invokeLater(() -> {SpielStarten(player);});
                    }
                }
            });
            vbox_4.add(beginn);

            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }


        vbox_4.add(Box.createVerticalStrut(10));
        {
            // "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
            JButton feld_gegner = new JButton("Feld Platz Gegner");
            feld_gegner.setAlignmentX(Component.CENTER_ALIGNMENT);
            vbox_4.add(feld_gegner);

            feld_gegner.addActionListener(new ActionListener() {


                @Override
                public void actionPerformed(ActionEvent e) {
                    //GUI.SpielStart.index_spalte = table.getSelectedColumn();
                    //GUI.SpielStart.index_zeile = table.getSelectedRow();
                    //System.out.println("Tabelle1 " + GUI.SpielStart.index_spalte + "," + GUI.SpielStart.index_zeile);

                }


            });

            JButton feld_eigene = new JButton("Feld Platz Eigene");
            feld_eigene.setAlignmentX(Component.CENTER_ALIGNMENT);
            vbox_4.add(feld_eigene);

            feld_eigene.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //GUI.SpielStart.index_spalte = table2.getSelectedColumn();
                    //GUI.SpielStart.index_zeile = table2.getSelectedRow();

                    //System.out.println("Tabelle1 " + GUI.SpielStart.index_spalte + "," + GUI.SpielStart.index_zeile);

                }
            });


            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }

        setzen.add(vbox_4);

        vbox_4.add(Box.createHorizontalStrut(10));
        vbox_4.add(Box.createHorizontalGlue());


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
        setzen.setJMenuBar(bar);

        setzen.pack();

        //Fenster wird im Vollbildmodus geoeffnet
        setzen.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //macht Fenster sichtbar
        setzen.setVisible(true);

    }
}
