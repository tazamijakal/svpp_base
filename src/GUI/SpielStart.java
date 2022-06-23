package GUI;

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
    public static JTable table2;

    public Spieler player;
    public SpielStart(){

    }


    public void sethorizShip(int x, int y)
    {

    }

    public void setTable2CellBLUE(int x, int y){
        table2.setValueAt(new ImageIcon(getClass().getResource("water.png")), x, y);
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

        DefaultTableModel model = new DefaultTableModel(data, columns);
        DefaultTableModel model2 = new DefaultTableModel(data, columns);

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
                if(player.attackToken == true){
                    if(player.name.equals("Server")){
                        player.attackToken = false;
                        player.server.TextClient("shot " + selecRow + " " + selecCol);
                    }
                    if(player.name.equals("Client")){
                        player.attackToken = false;
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
            JLabel label_gegner = new JLabel("Gegner Schiffe");
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
            JLabel label_eigene = new JLabel("Eigene Schiffe");
            vbox_2.add(label_eigene);

            table2.setRowHeight(120);//setzt Höhe der einzelnen Zeilen
            table.setSize(750, 750);

            vbox_2.add(scrollPane2);

        }

        frame.add(vbox_2);

        frame.add(Box.createHorizontalStrut(50));
        frame.add(Box.createHorizontalGlue());


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
        frame.setJMenuBar(bar);

        frame.pack();

        //Fenster wird im Vollbildmodus geoeffnet
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //macht Fenster sichtbar
        frame.setVisible(true);

    }
}
