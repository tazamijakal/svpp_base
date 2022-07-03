package GUI;

import KI.leichte_KI_zufall;
import KI.mittlere_KI;
import Logik.Ship;
import Logik.Spieler;
import ladenspeichern.AllWeNeed;
import ladenspeichern.Speichern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


/**
 * Klasse um das Spiel Schiffeversenken Online (im eigenen Netzwerk) zu starten mit JTables fuer eigenes Feld und Gegner Feld mit ActionListeners
 */
public final class SpielStart extends JFrame{

    //Attribute:
    //Spielfeldgroesse
    protected int spielfeldgr = 0;

    //fuer Schiffe setzen
    protected boolean direction = false;
    protected int length = 0;
    protected int x = 0;
    protected int y = 0;


    protected boolean placeship = true;
    public JButton spielerzugdisplay;


    //Nur einer der beiden RadioButtons kann ausgewaehlt werden
    //Hilfsvariablen
    protected boolean radioButton_l = false;
    protected boolean radioButton_o = false;

    public JTable table;
    int[] remainingships;
    public JTable table2;

    public JFrame startframe;

    public SpielStart(){

    }


    public void setTable2CellBLUE(int x, int y){
        table2.setValueAt(new ImageIcon(getClass().getResource("blue.png")), x, y);
    }
    public void setTableCellBLUE(int x, int y){
        table.setValueAt(new ImageIcon(getClass().getResource("blue.png")), x, y);
    }
    public void setTable2RedCross(int x, int y){
        table2.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), x, y);
    }
    public void setTable2BlackCross(int x, int y){
        table2.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), x, y);
    }

    public void setTableRedCross(int x, int y){
        table.setValueAt(new ImageIcon(getClass().getResource("redcross.png")), x, y);
    }
    public void setTableBlackCross(int x, int y){
        table.setValueAt(new ImageIcon(getClass().getResource("blackcross.png")), x, y);
    }

    public JTable getTable(){
        return table;
    }

    public JTable getTable2(){
        return table2;
    }


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


    /**
     *
     * Graphische Oberfläche aufbauen und anzeigen.
     * Fuer eigentlichen Spielstart.
     * Nachdem Einstellungen in Startbildschirm gemacht wurden.
     * @param player Spieler um Spiel zu starten
     * @param datei AllWeNeed datei kann leer sein
     */
    public void SpielStarten(Spieler player, AllWeNeed datei)
    {
        //Headers for JTable
        String[] columns = new String[player.mapSize];
        for(int i=0; i<player.mapSize; i++){
            columns[i] = "" + (i+1);
        }
        if(datei != null){
            System.out.println("datei != null");
        }

        if(datei == null || datei.table == null || datei.table2 == null){
            Object[][] data = new Object[player.mapSize][player.mapSize];
            for(int i = 0; i<player.board.length; i ++){
                for(int k = 0; k<player.board.length; k++){
                    if(player.board[i][k] != null && player.board[i][k] instanceof Ship){
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

            //data for JTable in a 2D table
            Object[][] data2 = new Object[player.mapSize][player.mapSize];
            for(int j=0; j<player.mapSize; j++){
                for(int k=0; k<player.mapSize; k++){
                    data2[j][k] = new ImageIcon(getClass().getResource("water.png"));
                }
            }

            DefaultTableModel model = new DefaultTableModel(data, columns);
            DefaultTableModel model2 = new DefaultTableModel(data2, columns);

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
        }
        else{
            table = datei.table;
            table2 = datei.table2;
        }




        table.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        table2.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());
        this.table = table;
        this.table2 = table2;
        table.getTableHeader().setReorderingAllowed(false);
        table2.getTableHeader().setReorderingAllowed(false);

        //Fenster mit Titel Schiffeversenken
        JFrame frame = new JFrame("Schiffeversenken " + player.name);
        this.startframe = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon(getClass().getResource("PirateBay.png")).getImage());


        //setzt Layout auf horizontale Box
        frame.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 20
        frame.add(Box.createHorizontalStrut(20));
        frame.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(table2);

        spielerzugdisplay = new JButton("AM ZUG");
        JLabel amzug = new JLabel("");

        if(player.attackToken == true){
            spielerzugdisplay.setBackground(Color.GREEN);
        }
        else if (player.name.equals("Server")){
            spielerzugdisplay.setBackground(Color.GREEN);
        }
        else {
            spielerzugdisplay.setBackground(Color.RED);
        }


        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table2.getSelectedRow();
                int selecCol = table2.getSelectedColumn();

                String a, b;
                try{
                    a = table2.getValueAt(selecRow,selecCol).toString();
                    b = getClass().getResource("water.png").toString();
                    if(table2.getValueAt(selecRow, selecCol).equals(getClass().getResource("blue.png"))){
                        System.out.println("do nothing");
                    }
                    else if(player.attackToken == true && a.equals(b)){
                        if(player.name.equals("Server")){
                            player.attackToken = false;
                            player.lastShotX = selecRow;
                            player.lastShotY = selecCol;
                            player.server.TextClient("shot " + selecRow + " " + selecCol);
                        }
                        else if(player.name.equals("Client")){
                            System.out.println(table2.getValueAt(selecRow, selecCol));
                            player.attackToken = false;
                            player.lastShotX = selecRow;
                            player.lastShotY = selecCol;
                            player.client.TextServer("shot " + selecRow + " " + selecCol);
                        }
                    }
                    else if(player.attackToken == false){
                        SwingWorker<Void, Void> sw33 = new SwingWorker<Void, Void>(){
                            @Override
                            protected Void doInBackground() throws Exception {
                                amzug.setText("Waiting for Opponent!");
                                Thread.sleep(2000);
                                amzug.setText("");
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
            //vertikale Box fuer eigenes Spielfeld
            JLabel label_gegner = new JLabel("Eigene Schiffe");
            vbox_1.add(label_gegner);

            table.setRowHeight(120);
            table.setSize(750, 750);

            vbox_1.add(scrollPane);
        }

        frame.add(vbox_1);

        // Zwischenraum der Breite 20
        frame.add(Box.createHorizontalStrut(20));
        frame.add(Box.createHorizontalGlue());


        Box vbox_2 = Box.createVerticalBox();
        {
            //vertikale Box fuer gegnerisches Spielfeld
            JLabel label_eigene = new JLabel("Gegner Schiffe");
            vbox_2.add(label_eigene);

            table2.setRowHeight(120);//setzt Höhe der einzelnen Zeilen
            table.setSize(750, 750);

            vbox_2.add(scrollPane2);

        }

        frame.add(vbox_2);

        // Zwischenraum der Breite 10
        frame.add(Box.createHorizontalStrut(10));
        frame.add(Box.createHorizontalGlue());

        JButton speichern = new JButton("Spiel speichern");

        Box vbox_3 = Box.createVerticalBox();
        {
            //vertikale Box fuer Spiel speichern und Ampel, ob am Zug
            speichern.setBackground(Color.CYAN);

            vbox_3.add(spielerzugdisplay);

            vbox_3.add(speichern);

            vbox_3.add(amzug);

        }

        frame.add(vbox_3);

        speichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.attackToken == true){
                    String filename = "" + AllWeNeed.nextId();
                    AllWeNeed newsave = new AllWeNeed(true, player,null, table, table2 ,null, null, filename);              //Speichern fuer Online versus
                    SwingUtilities.invokeLater(() -> {
                        try {
                            Speichern.save(newsave, filename);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    if(player.name.equals("Server")){
                        player.server.TextClient("save " + filename);
                    }
                    else if(player.name.equals("Client")){
                        player.client.TextServer("save " + filename);
                    }
                }
            }
        });

        // Zwischenraum der Breite 10
        frame.add(Box.createHorizontalStrut(10));
        frame.add(Box.createHorizontalGlue());



        // Menüzeile (JMenuBar) um Programm auch hier zu beenden
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
     * @param player Spieler 1
     * @param player2 Spieler 2
     * @return JTable wird zurueckgeben um Spiel zu starten
     */
    public JTable Setzen(Spieler player, Spieler player2)
    {
        JTable table;
        remainingships = player.remainingShips.clone();
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


        table = new JTable(model_setzen) {
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
        table.getTableHeader().setReorderingAllowed(false);


        //Fenster mit Titel Schiffeversenken
        JFrame setzen = new JFrame("Schiffeversenken " + player.name);
        setzen.setIconImage(new ImageIcon(getClass().getResource("PirateBay.png")).getImage());
        setzen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //setzt Layout auf horizontale Box
        setzen.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 20
        setzen.add(Box.createHorizontalStrut(20));
        setzen.add(Box.createHorizontalGlue());


        JScrollPane scrollPane_setzen = new JScrollPane(table);




        Box vbox_1 = Box.createVerticalBox();
        {
            //vertikale Box fuer Spielfeld zum Schiffesetzen
            JLabel label_setzen = new JLabel("Hier werden die Schiffe gesetzt");
            vbox_1.add(label_setzen);

            table.setRowHeight(120);
            table.setSize(750, 750);

            vbox_1.add(scrollPane_setzen);

        }

        setzen.add(vbox_1);

        // Zwischenraum der Breite 20
        setzen.add(Box.createHorizontalStrut(20));
        setzen.add(Box.createHorizontalGlue());



        //vertikale Box fuer Buttons und Checkboxen beim Schiffesetzen
        Box vbox_4 = Box.createVerticalBox();
        {

        }
        vbox_4.add(Box.createVerticalGlue());

        JButton add = new JButton("Schiffe hinzufügen");
        JButton delete = new JButton("Schiffe entfernen");
        add.setEnabled(false);

        vbox_4.add(Box.createVerticalStrut(20));
        {
            delete.setAlignmentX(Component.CENTER_ALIGNMENT);

            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delete.setEnabled(false);
                    placeship = false;
                    add.setEnabled(true);
                    System.out.println("delete selected");
                }
            });
            add.setAlignmentX(Component.CENTER_ALIGNMENT);

            vbox_4.add(add);

            // Zwischenraum der Hoehe 15
            vbox_4.add(Box.createVerticalStrut(15));
            vbox_4.add(Box.createVerticalGlue());

            vbox_4.add(delete);

            // Zwischenraum der Hoehe 15
            vbox_4.add(Box.createVerticalStrut(15));
            vbox_4.add(Box.createVerticalGlue());
        }

        JButton zufall = new JButton("Schiffe zufaellig");
        zufall.setAlignmentX(Component.CENTER_ALIGNMENT);

        vbox_4.add(Box.createVerticalStrut(10));
        {
            vbox_4.add(zufall);

            // Zwischenraum der Hoehe 10
            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }


        vbox_4.add(Box.createVerticalStrut(10));
        {

            //CheckBox:(fuer Auswahl Schiffsgroesse)
            JCheckBox cb_2 = new JCheckBox("2er Schiffe");
            if(player.remainingShips[2] <= 0){
                cb_2.setEnabled(false);
            }
            cb_2.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_3 = new JCheckBox("3er Schiffe");
            if(player.remainingShips[3] <= 0){
                cb_3.setEnabled(false);
            }
            cb_3.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_4 = new JCheckBox("4er Schiffe");
            if(player.remainingShips[4] <= 0){
                cb_4.setEnabled(false);
            }
            cb_4.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_5 = new JCheckBox("5er Schiffe");
            if(player.remainingShips[5] <= 0){
                cb_5.setEnabled(false);
            }
            cb_5.setAlignmentX(Component.CENTER_ALIGNMENT);
            JCheckBox cb_6 = new JCheckBox("6er Schiffe");
            if(player.remainingShips[6] <= 0){
                cb_6.setEnabled(false);
            }
            cb_6.setAlignmentX(Component.CENTER_ALIGNMENT);


            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    x = table.getSelectedRow();
                    y = table.getSelectedColumn();
                    if (length != 0 || placeship == false) {
                        if (placeship == false) {
                            if(player.board[x][y] instanceof Ship){
                                player.removeShipRequest(((Ship) player.board[x][y]).initialX, ((Ship) player.board[x][y]).initialY);
                            }
                        }
                        else if (player.spaceCheck(x, y, length, direction)) {
                            player.placeRemoveShip(placeship, x, y, length, direction);
                        }
                        Object[][] data = new Object[player.mapSize][player.mapSize];
                        for (int i = 0; i < player.board.length; i++) {
                            for (int k = 0; k < player.board.length; k++) {
                                if (player.board[i][k] instanceof Ship) {
                                    try {
                                        int initialX = ((Ship) player.board[i][k]).initialX;
                                        int initialY = ((Ship) player.board[i][k]).initialY;
                                        int length = ((Ship) player.board[i][k]).length;
                                        boolean horizontal = ((Ship) player.board[i][k]).initialD;          //horizontal == true
                                        data = drawShip(initialX, initialY, length, horizontal, data);
                                    }
                                    catch(Exception e1){}
                                } else {
                                    data[i][k] = new ImageIcon(getClass().getResource("water.png"));
                                }
                            }
                        }

                        DefaultTableModel model_set = new DefaultTableModel(data, columns);
                        table.setModel(model_set);
                        table.repaint();
                        if (player.remainingShips[2] <= 0 && length == 2) {
                            cb_2.setEnabled(false);
                            cb_2.setSelected(false);
                            length = 0;
                        }
                        if (player.remainingShips[3] <= 0 && length == 3) {
                            cb_3.setEnabled(false);
                            cb_3.setSelected(false);
                            length = 0;
                        }
                        if (player.remainingShips[4] <= 0 && length == 4) {
                            cb_4.setEnabled(false);
                            cb_4.setSelected(false);
                            length = 0;
                        }
                        if (player.remainingShips[5] <= 0 && length == 5) {
                            cb_5.setEnabled(false);
                            cb_5.setSelected(false);
                            length = 0;
                        }
                        if (player.remainingShips[6] <= 0 && length == 6) {
                            cb_6.setEnabled(false);
                            cb_6.setSelected(false);
                            length = 0;
                        }
                    }
                }
            });

            zufall.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player.resetplayer(remainingships);
                            zufall.setEnabled(false);
                            player.placerandom();
                            Object[][] data = new Object[player.mapSize][player.mapSize];
                            for (int i = 0; i < player.board.length; i++) {
                                for (int k = 0; k < player.board.length; k++) {
                                    if (player.board[i][k] != null && player.board[i][k] instanceof Ship) {
                                        int initialX = ((Ship) player.board[i][k]).initialX;
                                        int initialY = ((Ship) player.board[i][k]).initialY;
                                        int length = ((Ship) player.board[i][k]).length;
                                        boolean horizontal = ((Ship) player.board[i][k]).initialD;          //horizontal == true
                                        data = drawShip(initialX, initialY, length, horizontal, data);
                                    } else {
                                        data[i][k] = new ImageIcon(getClass().getResource("water.png"));
                                    }
                                }
                            }

                            DefaultTableModel model_set = new DefaultTableModel(data, columns);
                            table.setModel(model_set);
                            table.repaint();


                            zufall.setEnabled(true);

                            if (player.remainingShips[2] <= 0) {
                                cb_2.setEnabled(false);
                                cb_2.setSelected(false);
                                length = 0;
                            }
                            if (player.remainingShips[3] <= 0) {
                                cb_3.setEnabled(false);
                                cb_3.setSelected(false);
                                length = 0;
                            }
                            if (player.remainingShips[4] <= 0) {
                                cb_4.setEnabled(false);
                                cb_4.setSelected(false);
                                length = 0;
                            }
                            if (player.remainingShips[5] <= 0) {
                                cb_5.setEnabled(false);
                                cb_5.setSelected(false);
                                length = 0;
                            }
                            if (player.remainingShips[6] <= 0) {
                                cb_6.setEnabled(false);
                                cb_6.setSelected(false);
                                length = 0;
                            }
                        }
            });

            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    add.setEnabled(false);
                    placeship = true;
                    delete.setEnabled(true);
                    if(player.remainingShips[2] > 0){cb_2.setEnabled(true);}
                    if(player.remainingShips[3] > 0){cb_3.setEnabled(true);}
                    if(player.remainingShips[4] > 0){cb_4.setEnabled(true);}
                    if(player.remainingShips[5] > 0){cb_5.setEnabled(true);}
                    if(player.remainingShips[6] > 0){cb_6.setEnabled(true);}
                    System.out.println("add selected");
                }
            });

            cb_2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

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


            //Checkboxen zu vertikaler Box hinzufuegen
            vbox_4.add(cb_2);
            vbox_4.add(cb_3);
            vbox_4.add(cb_4);
            vbox_4.add(cb_5);
            vbox_4.add(cb_6);


            // Zwischenraum der Hoehe 10
            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }


        JButton horizontal = new JButton("Horizontal");
        JButton vertikal = new JButton("Vertikal");
        vertikal.setEnabled(false);

        vbox_4.add(Box.createVerticalStrut(10));
        {
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

            // Zwischenraum der Hoehe 10
            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }

        vbox_4.add(Box.createVerticalStrut(10));
        {
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

            // Zwischenraum der Hoehe 10
            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }





        vbox_4.add(Box.createVerticalStrut(10));
        {
            JButton beginn = new JButton("Spiel beginnen!");
            beginn.setAlignmentX(Component.CENTER_ALIGNMENT);

            beginn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    int counter = 0;
                    for(int j = 0; j < player.board.length;j++)
                    {
                        for(int k = 0; k < player.board.length;k++)
                        {
                            if(player.board[j][k] instanceof Ship)
                            {
                                counter = counter + 1;
                            }

                        }
                    }
                    //Spiel kann nur begonnen werden, wenn alle Schiffe gesetzt
                    if(counter > 0 && player.remainingShips[2] == 0 && player.remainingShips[3] == 0 && player.remainingShips[4] == 0 && player.remainingShips[5] == 0 && player.remainingShips[6] == 0){
                        setzen.setVisible(false);
                        if(player.name.equals("Spieler1")) {
                            player.lokaltoken = false;
                            if (player2.name.equals("Spieler2")) {
                                SwingUtilities.invokeLater(() -> {
                                    player2.GAME.Setzen(player2, player);
                                });
                            }
                            else if ((player2.name.equals("KI_leicht")) ||(player2.name.equals("KI_mittel"))) {
                                if (player2.name.equals("KI_leicht")){
                                    try {
                                        ((leichte_KI_zufall) player2).KIplazieren();
                                    } catch (Exception ex) {
                                        System.err.println("Error");
                                    }
                                }
                                if (player2.name.equals("KI_mittel")){
                                    ((mittlere_KI) player2).KIplazieren();
                                }
                                player.lokaltoken = false;
                                AllWeNeed Sp2 = new AllWeNeed(true, player2, null, player2.GAME.getTable(), player2.GAME.getTable2(), null, null, null);
                                AllWeNeed Sp1 = new AllWeNeed(false, player, null, player.GAME.getTable(), player.GAME.getTable2(), null, null, null);
                                player2.attackToken = true;
                                SpielStartLokal local = new SpielStartLokal();
                                SwingUtilities.invokeLater(() -> {
                                    local.SpielStartenLokal(Sp1, Sp2);
                                });
                            }
                        }
                        else if(player.name.equals("Spieler2")){
                            player.lokaltoken = false;
                            AllWeNeed Sp2 = new AllWeNeed(true, player, null, player.GAME.getTable(), player.GAME.getTable2(), null, null, null);
                            AllWeNeed Sp1 = new AllWeNeed(false, player2, null, player2.GAME.getTable(), player2.GAME.getTable2(), null, null, null);
                            player2.attackToken = true;
                            SpielStartLokal local = new SpielStartLokal();
                            SwingUtilities.invokeLater(() -> {
                                local.SpielStartenLokal(Sp1, Sp2);
                            });
                        }
                        else {
                            if (player.server != null) {
                                player.server.TextClient("ready");
                            }
                            else if (player.client != null) {
                                player.client.TextServer("ready");
                            }
                            if (radioButton_l && Startbildschirm.p1) {


                            }
                            SwingUtilities.invokeLater(() -> {
                                SpielStarten(player, null);
                            });
                        }
                    }
                    else{

                        //Meldung, wenn keine Schiffe gesetzt sind
                        JOptionPane.showMessageDialog(setzen, "Du hast nicht alle Schiffe gesetzt!!");
                    }
                }
            });
            vbox_4.add(beginn);

            // Zwischenraum der Hoehe 10
            vbox_4.add(Box.createVerticalStrut(10));
            vbox_4.add(Box.createVerticalGlue());

        }



        setzen.add(vbox_4);

        // Zwischenraum der Hoehe 10
        vbox_4.add(Box.createHorizontalStrut(10));
        vbox_4.add(Box.createHorizontalGlue());


        // Menüzeile (JMenuBar) um Programm auch hier zu beenden
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


        // Menüzeile zum Fenster hinzufügen.
        setzen.setJMenuBar(bar);

        setzen.pack();

        //Fenster wird im Vollbildmodus geoeffnet
        setzen.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //macht Fenster sichtbar
        setzen.setVisible(true);
        return table;
    }
}
