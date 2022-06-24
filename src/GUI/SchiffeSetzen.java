package GUI;

import Logik.Spieler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SchiffeSetzen {

    SpielStart GAME = new SpielStart();
    public static JTable table_setzen;

    public Spieler player;

    public SchiffeSetzen(){

    }



    //Methode evtl. fuer Schiffe setzen benutzen?
    public void setTable2CellSHIP(int x, int y){
        table_setzen.setValueAt(new ImageIcon(getClass().getResource("water.png")), x, y);
    }



    /**
     *
     * Graphische Oberfläche aufbauen und anzeigen.
     * Um Schiffe zu setzen.
     * Nachdem Einstellungen in Startbildschirm gemacht wurden.
     */
    public void Setzen(Spieler player)
    {

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


        JTable table_setzen = new JTable(model_setzen) {
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


        table_setzen.setDefaultRenderer(ImageIcon.class, new MyImageCellRenderer());

        this.table_setzen = table_setzen;

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


        JScrollPane scrollPane_setzen = new JScrollPane(this.table_setzen);



        table_setzen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Code anpassen fuer anklicken Schiffe platzieren

            }
        });


        Box vbox_1 = Box.createVerticalBox();
        {

            JLabel label_setzen = new JLabel("Hier werden die Schiffe gesetzt");
            vbox_1.add(label_setzen);

            table_setzen.setRowHeight(120);
            table_setzen.setSize(750, 750);

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
            vbox_4.add(zufall);

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

                    setzen.setVisible(false);

                    SwingUtilities.invokeLater(
                            () -> { GAME.SpielStarten(player); }
                    );
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

