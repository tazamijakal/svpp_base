package GUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Klasse Startbildschirm enthaelt alles fuer die graphische Darstellung des Startbildschirms.
 * Hier werden die Einstellungen fuer das Spiel gemacht.
 */

final class Startbildschirm{

    /**
     *
     * Graphische Oberfläche aufbauen und anzeigen.
     * Fuer Startbildschirm.
     */
    public static void start() {

        SpielStart GAME = new SpielStart();

        //model und model2 werden als Parameter an Funktion von SpielStart uebergeben
        //DefaultTableModel model = new DefaultTableModel();
        //DefaultTableModel model2 = new DefaultTableModel();


        //Erzeugt Fenster mit Titel Schiffeversenken
        JFrame startbildschirm = new JFrame("Schiffeversenken");
        startbildschirm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //setzt Layout auf vertikale Box
        startbildschirm.setContentPane(Box.createHorizontalBox());


        // Zwischenraum der Breite 50 oder mehr.Horizontal
        startbildschirm.add(Box.createHorizontalStrut(50));
        startbildschirm.add(Box.createHorizontalGlue());


        //Panel für Spielfeldgroesse Slider und starten von Spiel machen:
        JPanel spiel_Start = new JPanel();
        //Komponenten werden vertikal angeordnet
        spiel_Start.setLayout(new BoxLayout(spiel_Start, BoxLayout.PAGE_AXIS));

        JLabel Spielfeldgr = new JLabel("Spielfeld (Wert zuerst setzen):");

        //Setzen von Spielfeldgroesse wie vorgegeben einschraenken:
        JSlider slider_gr = new JSlider(5, 30, 10);//Standard: rang: 0-100, initial value: 50
        //->siehe Info Konstruktor, kann man auch selber noch anpassen

        slider_gr.setPaintLabels(true); //Anzeige der Zahlen
        slider_gr.setPaintTrack(true); //Balken werden angezeigt
        slider_gr.setSnapToTicks(true); //Zu ganzzahligen Werten "springen"

        //Damit aktueller Wert von Slider angezeigt werden kann
        JLabel label_slider = new JLabel();


        slider_gr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {


                int value = slider_gr.getValue();
                //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)

                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider.setText(Integer.toString(slider_gr.getValue()));
                label_slider.setText(Integer.toString(value));



                GAME.spielfeldgr = value;

                if(GAME.spielfeldgr < 20 && !GAME.flag1)
                {
                    GAME.flag1 = true;
                }

            }
        });




        JLabel start = new JLabel("Spiele Schiffe versenken!");
        JLabel start_Text = new JLabel("Das Spiel beginnt hier!");
        JButton play = new JButton("Play");
        play.setPreferredSize(new Dimension(20, 50));
        play.setBackground(Color.GREEN);

        //ist für Schrift in Button!!
        //play.setVerticalAlignment(SwingConstants.CENTER);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Spieler player1 = new Spieler("Server", model.getSpielfeld(), 75, ships);
                //Spieler player2 = new Spieler("Client", model.getSpielfeld(), 75, ships);
                startbildschirm.setVisible(false);

                SwingUtilities.invokeLater(
                        () -> { SpielStart.SpielStarten(); }
                );

            }
        });

        //Hier werden alle Komponenten zum Panel hinzugefuegt
        //Mit passendem Zwischenraum
        spiel_Start.add(Box.createVerticalStrut(10));
        spiel_Start.add(Box.createVerticalGlue());

        spiel_Start.add(Spielfeldgr);
        spiel_Start.add(slider_gr);
        spiel_Start.add(label_slider);

        spiel_Start.add(Box.createVerticalStrut(5));
        spiel_Start.add(Box.createVerticalGlue());

        spiel_Start.add(start);
        spiel_Start.add(start_Text);
        spiel_Start.add(play);

        spiel_Start.add(Box.createVerticalStrut(10));
        spiel_Start.add(Box.createVerticalGlue());



        //Panel/Container mit Teil der Komponenten:
        //Fuer Spielmodus, Spieler-Typ usw.
        JPanel spielAuswahl = new JPanel();

        //Komponenten werden vertikal angeordnet
        spielAuswahl.setLayout(new BoxLayout(spielAuswahl, BoxLayout.PAGE_AXIS));

        //Auswahl Spieler-Typ
        JLabel spieler = new JLabel("Spieler");
        String[] list_spieler = {"Client", "Server"};
        JComboBox<String> auswahl_spieler = new JComboBox<String>(list_spieler);

        //Auswahl lokales Spiel
        JRadioButton rb_lokal = new JRadioButton("Lokales Spiel");
        rb_lokal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!GAME.radioButton_o)
                {
                    rb_lokal.getAction();
                    GAME.radioButton_l = true;
                    rb_lokal.setEnabled(GAME.radioButton_l);
                }
                else
                {
                    GAME.radioButton_l = false;
                    rb_lokal.setEnabled(GAME.radioButton_l);
                }

            }
        });



        //Auswahl Spielmodus
        String[] list_Lokal = {"Spieler vs Spieler", "Spieler vs KI"};
        JComboBox<String> auswahl_lokal = new JComboBox<String>(list_Lokal);

        //Auswahl Online Spiel:
        JRadioButton rb_online = new JRadioButton("Online Spiel");
        rb_online.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!GAME.radioButton_l)
                {
                    GAME.radioButton_o = true;
                    rb_online.setEnabled(GAME.radioButton_o);
                }
                else
                {
                    GAME.radioButton_o = false;
                    rb_online.setEnabled(GAME.radioButton_o);
                }



            }
        });


        //Auswahl Spielmodus
        String[] list_online = {"Spieler vs Spieler", "Spieler vs KI", "KI vs KI"};
        JComboBox<String> auswahl_online = new JComboBox<String>(list_online);


        //Hier werden alle Komponenten zum Panel hinzugefuegt
        //Mit passendem Zwischenraum
        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());

        spielAuswahl.add(spieler);
        spielAuswahl.add(auswahl_spieler);

        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());

        spielAuswahl.add(rb_lokal);
        spielAuswahl.add(auswahl_lokal);

        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());

        spielAuswahl.add(rb_online);
        spielAuswahl.add(auswahl_online);

        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());


        //Panel für Schiffe Labels machen:
        JPanel schiffAuswahl1 = new JPanel();
        //Komponenten werden vertikal angeordnet
        schiffAuswahl1.setLayout(new BoxLayout(schiffAuswahl1, BoxLayout.PAGE_AXIS));

        JLabel Schiffe2 = new JLabel("2er Schiffe:");
        JLabel Schiffe3 = new JLabel("3er Schiffe:");
        JLabel Schiffe4 = new JLabel("4er Schiffe:");
        JLabel Schiffe5 = new JLabel("5er Schiffe:");
        JLabel Schiffe6 = new JLabel("6er Schiffe:");


        //Hier werden alle Komponenten zum Panel hinzugefuegt
        //Mit passendem Zwischenraum
        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());

        schiffAuswahl1.add(Schiffe2);

        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());

        schiffAuswahl1.add(Schiffe3);

        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());

        schiffAuswahl1.add(Schiffe4);

        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());

        schiffAuswahl1.add(Schiffe5);

        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());

        schiffAuswahl1.add(Schiffe6);

        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());


        //Panel für Schiffe Slider machen:
        JPanel schiffSlider = new JPanel();
        //Komponenten werden vertikal angeordnet
        schiffSlider.setLayout(new BoxLayout(schiffSlider, BoxLayout.PAGE_AXIS));

        JSlider slider2 = new JSlider();


        JLabel label_slider2 = new JLabel();
        if(GAME.spielfeldgr < 20 && GAME.flag1)
        {
            slider2.setEnabled(GAME.flag1);

        }
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                boolean flag2 = true;

                slider2.setEnabled(flag2);

                if(GAME.spielfeldgr >= 20)
                {
                    flag2 = false;
                    slider2.setEnabled(flag2);
                    label_slider2.setVisible(false);
                }
                if(GAME.spielfeldgr < 20)
                {
                    flag2 = true;
                    slider2.setEnabled(GAME.flag1);
                    label_slider2.setVisible(true);
                    int value2 = slider2.getValue();
                    //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)
                    //pb.setValue(value);//Fortschrittsbalken zählt nach oben

                    //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                    //Wert im Label angezeigt werden
                    label_slider2.setText(Integer.toString(slider2.getValue()));
                    label_slider2.setText(Integer.toString(value2));


                }



            }
        });



        JSlider slider3 = new JSlider();


        JSlider slider4 = new JSlider();


        JSlider slider5 = new JSlider();


        JSlider slider6 = new JSlider();


        //Hier werden alle Komponenten zum Panel hinzugefuegt
        //Mit passendem Zwischenraum
        //schiffAuswahl1.add(Box.createVerticalStrut(5));
        //schiffAuswahl1.add(Box.createVerticalGlue());
        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider2);
        schiffSlider.add(label_slider2);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider3);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider4);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider5);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider6);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());




        //Hier werden alle Panels zum Startfenster hinzugefuegt
        //Mit passendem Zwischenraum
        startbildschirm.add(Box.createHorizontalStrut(5));
        startbildschirm.add(Box.createHorizontalGlue());

        startbildschirm.add(spiel_Start);

        startbildschirm.add(Box.createHorizontalStrut(5));
        startbildschirm.add(Box.createHorizontalGlue());

        startbildschirm.add(spielAuswahl);

        startbildschirm.add(Box.createHorizontalStrut(100));
        startbildschirm.add(Box.createHorizontalGlue());

        startbildschirm.add(schiffAuswahl1);

        startbildschirm.add(Box.createHorizontalStrut(5));
        startbildschirm.add(Box.createHorizontalGlue());

        startbildschirm.add(schiffSlider);

        startbildschirm.add(Box.createHorizontalStrut(100));
        startbildschirm.add(Box.createHorizontalGlue());


        //Startbildschirm sichtbar machen und im Vollbildmodus
        startbildschirm.setExtendedState(JFrame.MAXIMIZED_BOTH);
        startbildschirm.setVisible(true);

    }

}
