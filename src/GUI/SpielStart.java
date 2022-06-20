package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class SpielStart {


    /**
     *
     * Graphische Oberfläche aufbauen und anzeigen.
     * Fuer eigentlichen Spielstart.
     * Nachdem Einstellungen in Startbildschirm gemacht wurden.
     */
    public static void SpielStarten(DefaultTableModel model, DefaultTableModel model2)
    {
        //Fuer Schiffe setzen:
        //String[][] feldSetzen = new String[model.getSpielfeld()][model.getSpielfeld()];


        JFrame frame = new JFrame("Schiffeversenken");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Zwischenraum der Breite 50 oder mehr.
        //frame.add(Box.createHorizontalStrut(50));
        //frame.add(Box.createHorizontalGlue());

        frame.setBackground(Color.BLUE);
        frame.setContentPane(Box.createHorizontalBox());

        // Zwischenraum der Breite 50 oder mehr.
        frame.add(Box.createHorizontalStrut(20));
        frame.add(Box.createHorizontalGlue());


        JTable table = new JTable(model.getSpielfeld(), model.getSpielfeld());
        JTable table2 = new JTable(model.getSpielfeld(), model.getSpielfeld());



        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(table2);

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
                table2.setValueAt("S", selecRow, selecCol);

                System.out.println("Tabelle2 " + selecRow + "," + selecCol);

            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecRow = table.getSelectedRow();
                int selecCol = table.getSelectedColumn();

                //feldSetzen[selecRow][selecRow] = "ship";
                table.setValueAt("S", selecRow, selecCol);

                System.out.println("Tabelle1 " + selecRow + "," + selecCol);

            }
        });


        Box vbox_1 = Box.createVerticalBox();
        {
            JLabel label_gegner = new JLabel("Gegner Schiffe");
            vbox_1.add(label_gegner);

            table.setRowHeight(30);
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

            table2.setRowHeight(30);//setzt Höhe der einzelnen Zeilen
            table.setSize(750, 750);

            vbox_2.add(scrollPane2);

        }

        frame.add(vbox_2);

        //Um alle Felder am Anfang auf Wasser zu setzen
        //Image Wasser:
        //Bilder importieren:
        //Image water;
        Icon icon_water = new ImageIcon("wasser.jpg");
        //JLabel water_label= new JLabel(icon_water);
        //water = icon_water.getImage();

        for(int i = 0; i < model.getSpielfeld(); i++)
        {
            for(int j = 0; j < model.getSpielfeld(); j++)
            {

                //table.setValueAt(icon_water, i, j);
                //table2.setValueAt(icon_water, i, j);

                //table.setValueAt("w", i, j);
                table2.setValueAt("w", i, j);

            }
        }




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

            JButton feld_gegner = new JButton("Feld Platz Gegner");
            feld_gegner.setAlignmentX(Component.CENTER_ALIGNMENT);
            vbox_4.add(feld_gegner);

            feld_gegner.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    model.setIndex_spalte(table.getSelectedColumn());
                    model.setIndex_zeile(table.getSelectedRow());

                    System.out.println("Tabelle1 " + model.getIndex_zeile() + "," + model.getIndex_spalte());

                }


            });

            JButton feld_eigene = new JButton("Feld Platz Eigene");
            feld_eigene.setAlignmentX(Component.CENTER_ALIGNMENT);
            vbox_4.add(feld_eigene);

            feld_eigene.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    model2.setIndex_spalte_eig(table2.getSelectedColumn());
                    model2.setIndex_zeile_eig(table2.getSelectedRow());



                    System.out.println("Tabelle2 " + model2.getIndex_zeile_eig() + "," + model2.getIndex_spalte_eig());

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

        //Fenster wird im Vollbildmodus geoeffnet
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //macht Fenster sichtbar
        frame.setVisible(true);

    }
}
