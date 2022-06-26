package GUI;

import Logik.Spieler;
import Logik.Game;
import Logik.Game2KI;

import Socket.Client;
import Socket.Server;

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

public final class Startbildschirm{

    public static String role;

    public static int anzahl2 = 0;
    public static int anzahl3 = 0;
    public static int anzahl4 = 0;
    public static int anzahl5 = 0;
    public static int anzahl6 = 0;

    public static boolean lok_Spsp = false;

    public static boolean p1 = false;
    public static boolean p2 = false;

    public static int selectedSpace = 0;
    public static int maxSpace = 0;
    public static Spieler sp1;
    public static Spieler sp2;





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



        //Panel für Schiffe Labels machen:
        JPanel schiffAuswahl1 = new JPanel();
        //Komponenten werden vertikal angeordnet
        schiffAuswahl1.setLayout(new BoxLayout(schiffAuswahl1, BoxLayout.PAGE_AXIS));

        JLabel Schiffe2 = new JLabel("2er Schiffe:");
        JLabel Schiffe3 = new JLabel("3er Schiffe:");
        JLabel Schiffe4 = new JLabel("4er Schiffe:");
        JLabel Schiffe5 = new JLabel("5er Schiffe:");
        JLabel Schiffe6 = new JLabel("6er Schiffe:");
        JLabel maxship = new JLabel("Maximum Space: 0");
        JLabel currentship = new JLabel("Selected Space: 0");

        JSlider slider2 = new JSlider();
        JLabel label_slider2 = new JLabel();
        slider2.setValue(0);
        label_slider2.setText(Integer.toString(slider2.getValue()));

        JSlider slider3 = new JSlider();
        JLabel label_slider3 = new JLabel();
        slider3.setValue(0);
        label_slider3.setText(Integer.toString(slider3.getValue()));

        JSlider slider4 = new JSlider();
        JLabel label_slider4 = new JLabel();
        slider4.setValue(0);
        label_slider4.setText(Integer.toString(slider4.getValue()));

        JSlider slider5 = new JSlider();
        JLabel label_slider5 = new JLabel();
        slider5.setValue(0);
        label_slider5.setText(Integer.toString(slider5.getValue()));

        JSlider slider6 = new JSlider();
        JLabel label_slider6 = new JLabel();
        slider6.setValue(0);
        label_slider6.setText(Integer.toString(slider6.getValue()));

        slider2.setEnabled(false);
        slider3.setEnabled(false);
        slider4.setEnabled(false);
        slider5.setEnabled(false);
        slider6.setEnabled(false);

        //Panel für Schiffe Slider machen:
        JPanel schiffSlider = new JPanel();
        //Komponenten werden vertikal angeordnet
        schiffSlider.setLayout(new BoxLayout(schiffSlider, BoxLayout.PAGE_AXIS));



        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                //int value2 = slider2.getValue();
                anzahl2 = slider2.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);
                //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)

                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider2.setText(Integer.toString(slider2.getValue()));
                label_slider2.setText(Integer.toString(anzahl2));

            }
        });


        slider3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                //int value3 = slider3.getValue();
                anzahl3 = slider3.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);
                //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)

                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider3.setText(Integer.toString(slider3.getValue()));
                label_slider3.setText(Integer.toString(anzahl3));

            }
        });

        slider4.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                //int value4 = slider4.getValue();
                anzahl4 = slider4.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);
                //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)

                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider4.setText(Integer.toString(slider4.getValue()));
                label_slider4.setText(Integer.toString(anzahl4));

            }
        });


        slider5.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                //int value5 = slider5.getValue();
                anzahl5 = slider5.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);
                //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)

                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider5.setText(Integer.toString(slider5.getValue()));
                label_slider5.setText(Integer.toString(anzahl5));

            }
        });


        slider6.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                //int value6 = slider6.getValue();
                anzahl6 = slider6.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);
                //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)

                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider6.setText(Integer.toString(slider6.getValue()));
                label_slider6.setText(Integer.toString(anzahl6));

            }
        });


        slider_gr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                int value = slider_gr.getValue();
                maxship.setText("Maximum Space: " + (value*value/3));
                maxSpace = (value*value/3);
                //gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)

                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider.setText(Integer.toString(slider_gr.getValue()));
                label_slider.setText(Integer.toString(value));

                slider2.setEnabled(true);
                slider3.setEnabled(true);
                slider4.setEnabled(true);
                slider5.setEnabled(true);
                slider6.setEnabled(true);


                GAME.spielfeldgr = value;

                //Ausschalten Schiffsauswahl fuer bestimmte Spielfeldgroesse
                //ab Spielfeldgroesse 20x20

                if(GAME.spielfeldgr < 20)
                {
                    slider2.setEnabled(true);
                    Schiffe2.setEnabled(true);
                    label_slider2.setEnabled(true);

                    slider6.setEnabled(false);
                    Schiffe6.setEnabled(false);
                    label_slider6.setEnabled(false);
                    slider2.setValue(0);
                    slider3.setValue(0);
                    slider4.setValue(0);
                    slider5.setValue(0);
                    slider6.setValue(0);
                    anzahl2 = anzahl3 = anzahl4 = anzahl5 = anzahl6 = 0;
                }
                else
                {
                    slider6.setEnabled(true);
                    Schiffe6.setEnabled(true);
                    label_slider6.setEnabled(true);

                    slider2.setEnabled(false);
                    Schiffe2.setEnabled(false);
                    label_slider2.setEnabled(false);
                    slider2.setValue(0);
                    slider3.setValue(0);
                    slider4.setValue(0);
                    slider5.setValue(0);
                    slider6.setValue(0);
                    anzahl2 = anzahl3 = anzahl4 = anzahl5 = anzahl6 = 0;
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

                if(role != null && (GAME.radioButton_l || GAME.radioButton_o) && selectedSpace <= maxSpace){
                    startbildschirm.setVisible(false);
                    int hp = (GAME.spielfeldgr * GAME.spielfeldgr) / 3;
                    if(GAME.radioButton_o)
                    {
                        if(role.equals("Server")){
                            //Anzahl Schiffe
                            //int[] ships = {0, 0, 2, 2, 0, 0};
                            int[] ships = {0, 0, anzahl2, anzahl3, anzahl4, anzahl5, anzahl6};
                            Spieler player = new Spieler("Server", GAME.spielfeldgr, hp, ships);
                            Server server = new Server(50000, 0, player, GAME, startbildschirm);
                            player.serverSetter(server);
                            SwingUtilities.invokeLater(() -> {server.connect();});
                        }
                        if(role.equals("Client")){
                            Spieler player = new Spieler("Client", GAME.spielfeldgr, hp, null);
                            Client client = new Client(50000, "localhost", player, GAME, startbildschirm);
                            player.clientSetter(client);
                            SwingUtilities.invokeLater(() -> {client.connect();});
                        }
                    }


                }
                else if(GAME.radioButton_l /*&& lok_Spsp*/)
                {
                    startbildschirm.setVisible(false);
                    int hp = (GAME.spielfeldgr * GAME.spielfeldgr) / 3;
                    int[] ships = {0, 0, anzahl2, anzahl3, anzahl4, anzahl5, anzahl6};
                    //int k = 1;
                    //Spieler player1 = new Spieler("Spieler1", GAME.spielfeldgr, hp, ships);
                    //Spieler player2 = new Spieler("Spieler2", GAME.spielfeldgr, hp, ships);

                    sp1 = new Spieler("Spieler1", GAME.spielfeldgr, hp, ships);
                    sp2 = new Spieler("Spieler2", GAME.spielfeldgr, hp, ships);

                    if(!p1 && !p2)
                    {
                        p1 = true;
                        SwingUtilities.invokeLater(() -> {GAME.Setzen(sp1);});


                    }
                    if(p1 && !p2)
                    {
                        p2 = true;
                        SwingUtilities.invokeLater(() -> {GAME.Setzen(sp2);});

                    }


                }
                else{
                    System.out.println("Finish all parameters");
                }

                /*SwingUtilities.invokeLater(
                        () -> { GAME.SpielStarten(player, GAME); }
                );*/

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
        String[] list_spieler = {"[choose]", "Client", "Server", "KI"};
        JComboBox<String> auswahl_spieler = new JComboBox<String>(list_spieler);

        auswahl_spieler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Startbildschirm.role = (String) auswahl_spieler.getSelectedItem();
            }
        });

        //Erzeugung RadioButton rb_lokal und rb_online
        JRadioButton rb_lokal = new JRadioButton("Lokales Spiel");
        JRadioButton rb_online = new JRadioButton("Online Spiel");

        //Auswahl Spielmodus lokal
        //Erzeugung ComboBox
        String[] list_Lokal = {"[choose]", "Spieler vs Spieler", "Spieler vs KI"};
        JComboBox<String> auswahl_lokal = new JComboBox<String>(list_Lokal);

        //Auswahl Spielmodus Online
        //Erzeugung ComboBox
        String[] list_online = {"[choose]", "Spieler vs Spieler", "Spieler vs KI", "KI vs KI"};
        JComboBox<String> auswahl_online = new JComboBox<String>(list_online);



        //Auswahl lokales Spiel
        //Es kann entweder ein lokales Spiel oder ein online Spiel ausgewählt werden
        if(!GAME.radioButton_o)
        {
            rb_lokal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (GAME.radioButton_l)
                    {
                        //rb_lokal.getAction();
                        GAME.radioButton_l = false;
                        rb_online.setEnabled(true);
                        auswahl_online.setEnabled(true);
                    }
                    else
                    {
                        GAME.radioButton_l = true;
                        rb_online.setEnabled(false);
                        auswahl_online.setEnabled(false);

                    }

                }
            });
        }
        else
        {
            rb_lokal.setEnabled(false);
            auswahl_lokal.setEnabled(false);
        }




        //Auswahl Online Spiel
        //Es kann entweder ein lokales Spiel oder ein online Spiel ausgewählt werden
        if(!GAME.radioButton_l)
        {
            rb_online.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (GAME.radioButton_o)
                    {
                        GAME.radioButton_o = false;
                        rb_lokal.setEnabled(true);
                        auswahl_lokal.setEnabled(true);
                    }
                    else
                    {
                        GAME.radioButton_o = true;
                        rb_lokal.setEnabled(false);
                        auswahl_lokal.setEnabled(false);
                    }


                }
            });
        }
        else
        {
            rb_online.setEnabled(false);
            auswahl_online.setEnabled(false);
        }




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



        //Hier werden alle Komponenten zum Panel hinzugefuegt
        //Mit passendem Zwischenraum
        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());

        schiffAuswahl1.add(currentship);

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




        //Hier werden alle Komponenten zum Panel hinzugefuegt
        //Mit passendem Zwischenraum
        //schiffAuswahl1.add(Box.createVerticalStrut(5));
        //schiffAuswahl1.add(Box.createVerticalGlue());
        schiffSlider.add(Box.createVerticalStrut(20));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(maxship);

        schiffSlider.add(Box.createHorizontalStrut(5));
        schiffSlider.add(Box.createVerticalStrut(20));
        schiffSlider.add(Box.createHorizontalGlue());

        schiffSlider.add(slider2);
        schiffSlider.add(label_slider2);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider3);
        schiffSlider.add(label_slider3);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider4);
        schiffSlider.add(label_slider4);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider5);
        schiffSlider.add(label_slider5);

        schiffSlider.add(Box.createVerticalStrut(5));
        schiffSlider.add(Box.createVerticalGlue());

        schiffSlider.add(slider6);
        schiffSlider.add(label_slider6);

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
        startbildschirm.pack();

        //Startbildschirm sichtbar machen und im Vollbildmodus
        startbildschirm.setExtendedState(JFrame.MAXIMIZED_BOTH);
        startbildschirm.setResizable(true);
        startbildschirm.setVisible(true);

    }

}
