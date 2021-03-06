package GUI;

import KI.*;
import Logik.Spieler;

import Socket.Client;
import Socket.Server;
import ladenspeichern.AllWeNeed;
import ladenspeichern.Laden;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

/**
 *
 * Klasse Startbildschirm enthaelt alles fuer die graphische Darstellung des Startbildschirms.
 * Hier werden die Einstellungen fuer das Spiel gemacht.
 */

public final class Startbildschirm{

    //Rolle, die Spieler annimmt
    public static String role;

    //Anzahl der Schiffe bei verschiedenen Schiffstypen
    public static int anzahl2 = 0;
    public static int anzahl3 = 0;
    public static int anzahl4 = 0;
    public static int anzahl5 = 0;
    public static int anzahl6 = 0;

    public static String ClientIP;

    public static boolean p1 = false;

    //besetzte Spielfelder
    public static int selectedSpace = 0;

    //maximale Anzahl an Feldern, die besetzt werden duerfen
    public static int maxSpace = 0;




    /**
     *
     * Graphische Oberfläche aufbauen und anzeigen.
     * Fuer Startbildschirm.
     *
     */
    public static void start(){

        SpielStart GAME = new SpielStart();
        Runnable r = new Runnable() {
            public void run() {
                AudioPlayer MusicPlayer = new AudioPlayer();
                MusicPlayer.play(getClass().getResource("Music/title.wav"));
            }
        };

        new Thread(r).start();



        //Erzeugt Fenster mit Titel Schiffeversenken
        JFrame startbildschirm = new JFrame("Schiffeversenken");
        startbildschirm.setIconImage(new ImageIcon(Startbildschirm.class.getResource("PirateBay.png")).getImage());
        startbildschirm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //setzt Layout auf vertikale Box
        startbildschirm.setContentPane(Box.createHorizontalBox());


        // Zwischenraum (horizontal) der Breite 50
        startbildschirm.add(Box.createHorizontalStrut(50));
        startbildschirm.add(Box.createHorizontalGlue());


        //Panel spiel_Start
        // fuer Spielfeldgroesse Slider
        // fuer starten von Spiel machen (play-Button)
        // fuer laden von Spiel
        JPanel spiel_Start = new JPanel();

        //Komponenten werden vertikal angeordnet
        spiel_Start.setLayout(new BoxLayout(spiel_Start, BoxLayout.PAGE_AXIS));

        JLabel Spielfeldgr = new JLabel("Spielfeld (Wert zuerst setzen):");

        //Setzen von Spielfeldgroesse wie vorgegeben einschraenken:
        //Spielfeldgroesse von 5x5 bis 30x30
        JSlider slider_gr = new JSlider(5, 30, 10);//Standard: rang: 0-100, initial value: 50


        slider_gr.setPaintLabels(true); //Anzeige der Zahlen
        slider_gr.setPaintTrack(true); //Balken werden angezeigt
        slider_gr.setSnapToTicks(true); //Zu ganzzahligen Werten "springen"

        //Damit aktueller Wert von Slider angezeigt werden kann
        JLabel label_slider = new JLabel();

        //Anzeige, welche Werte noch nicht gesetzt sind
        //Hinweis in rot
        JLabel spielfeldgr_fehlt = new JLabel("Spielfeldgroesse fehlt");
        spielfeldgr_fehlt.setForeground(Color.RED);
        spielfeldgr_fehlt.setVisible(false);

        JLabel lok_on_fehlt = new JLabel("lokal/online Spiel fehlt");
        lok_on_fehlt.setForeground(Color.RED);
        lok_on_fehlt.setVisible(false);

        JLabel lok_modus_fehlt = new JLabel("Spielmodus fehlt");
        lok_modus_fehlt.setForeground(Color.RED);
        lok_modus_fehlt.setVisible(false);

        JLabel spieler_fehlt = new JLabel("Spieler fehlt");
        spieler_fehlt.setForeground(Color.RED);
        spieler_fehlt.setVisible(false);

        JLabel schiffe_fehlen = new JLabel("Schiffe fehlen");
        schiffe_fehlen.setForeground(Color.RED);
        schiffe_fehlen.setVisible(false);




        //Panel schiffAuswahl1
        //fuer Schiffe Labels machen
        //und Label Selected Space
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

        JLabel displayoutput = new JLabel("");
        displayoutput.setFont(new Font("Calibri", Font.BOLD, 20));
        displayoutput.setForeground(Color.RED);


        JSlider slider2 = new JSlider(0, 30);
        JLabel label_slider2 = new JLabel();
        slider2.setValue(0);
        label_slider2.setText(Integer.toString(slider2.getValue()));

        JSlider slider3 = new JSlider(0, 30);
        JLabel label_slider3 = new JLabel();
        slider3.setValue(0);
        label_slider3.setText(Integer.toString(slider3.getValue()));

        JSlider slider4 = new JSlider(0, 30);
        JLabel label_slider4 = new JLabel();
        slider4.setValue(0);
        label_slider4.setText(Integer.toString(slider4.getValue()));

        JSlider slider5 = new JSlider(0, 30);
        JLabel label_slider5 = new JLabel();
        slider5.setValue(0);
        label_slider5.setText(Integer.toString(slider5.getValue()));

        JSlider slider6 = new JSlider(0, 30);
        JLabel label_slider6 = new JLabel();
        slider6.setValue(0);
        label_slider6.setText(Integer.toString(slider6.getValue()));

        slider2.setEnabled(false);
        slider3.setEnabled(false);
        slider4.setEnabled(false);
        slider5.setEnabled(false);
        slider6.setEnabled(false);

        //Panel fuer Schiffe Slider
        //und Maximum Space
        JPanel schiffSlider = new JPanel();

        //Komponenten werden vertikal angeordnet
        schiffSlider.setLayout(new BoxLayout(schiffSlider, BoxLayout.PAGE_AXIS));



        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                schiffe_fehlen.setVisible(false);
                //gibt Wert zurueck, den Slider gerade hat
                anzahl2 = slider2.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);


                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider2.setText(Integer.toString(slider2.getValue()));
                label_slider2.setText(Integer.toString(anzahl2));

            }
        });


        slider3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                schiffe_fehlen.setVisible(false);
                //gibt Wert zurueck, den Slider gerade hat
                anzahl3 = slider3.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);


                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider3.setText(Integer.toString(slider3.getValue()));
                label_slider3.setText(Integer.toString(anzahl3));

            }
        });

        slider4.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                schiffe_fehlen.setVisible(false);
                //gibt Wert zurueck, den Slider gerade hat
                anzahl4 = slider4.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);


                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider4.setText(Integer.toString(slider4.getValue()));
                label_slider4.setText(Integer.toString(anzahl4));

            }
        });


        slider5.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                schiffe_fehlen.setVisible(false);
                //gibt Wert zurueck, den Slider gerade hat
                anzahl5 = slider5.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);


                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider5.setText(Integer.toString(slider5.getValue()));
                label_slider5.setText(Integer.toString(anzahl5));

            }
        });


        slider6.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                schiffe_fehlen.setVisible(false);
                //gibt Wert zurueck, den Slider gerade hat
                anzahl6 = slider6.getValue();
                selectedSpace = (anzahl2*2 + anzahl3*3 + anzahl4*4 + anzahl5*5 + anzahl6*6);
                currentship.setText("Selected Space: " + selectedSpace);


                //Wenn sich slider auf einen anderen Wert bewegt soll dieser
                //Wert im Label angezeigt werden
                label_slider6.setText(Integer.toString(slider6.getValue()));
                label_slider6.setText(Integer.toString(anzahl6));

            }
        });


        slider_gr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                spielfeldgr_fehlt.setVisible(false);

                //gibt Wert zurueck, den Slider gerade hat
                int value = slider_gr.getValue();
                maxship.setText("Maximum Space: " + (value*value/3));
                maxSpace = (value*value/3);


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
                //ab Spielfeldgroesse 20x20:
                //2er Schiffe deaktiviert, 6er Schiffe aktiviert

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
        start.setFont(new Font("Calibri", Font.BOLD, 20));
        start_Text.setFont(new Font("Calibri", Font.BOLD, 20));
        JButton play = new JButton("Play");
        play.setPreferredSize(new Dimension(20, 50));
        play.setBackground(Color.GREEN);

        //Auswahl Spielmodus lokal
        //Erzeugung ComboBox
        String[] list_Lokal = {"[choose]", "Spieler vs Spieler", "Spieler vs KI_leicht", "Spieler vs KI_mittel"};
        JComboBox<String> auswahl_lokal = new JComboBox<String>(list_Lokal);
        auswahl_lokal.setBackground(Color.lightGray);

        //Auswahl Spieler-Typ
        //bei Spielmodus online
        //Erzeugung ComboBox
        String[] list_spieler = {"[choose]", "Client", "Server", "KI_Client_leicht", "KI_Client_mittel", "KI_Server_leicht", "KI_Server_mittel"};
        JComboBox<String> auswahl_spieler = new JComboBox<String>(list_spieler);
        auswahl_spieler.setBackground(Color.lightGray);
        auswahl_spieler.setEnabled(false);
        auswahl_lokal.setEnabled(false);


        //Erzeugung RadioButton rb_lokal und rb_online
        JRadioButton rb_lokal = new JRadioButton("Lokales Spiel");
        JRadioButton rb_online = new JRadioButton("Online Spiel");
        rb_lokal.setFont(new Font("Calibri", Font.BOLD, 20));
        rb_online.setFont(new Font("Calibri", Font.BOLD, 20));



        auswahl_lokal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Meldung, dass Eingabe fehlt kann wieder ausgeblendet werden
                if(rb_lokal.isSelected() && !auswahl_lokal.getSelectedItem().equals("[choose]"))
                {
                    lok_modus_fehlt.setVisible(false);
                }
            }
        });



        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //rote Schrift, wenn etwas nicht ausgewaehlt
                    if(GAME.spielfeldgr <= 0)
                    {
                        spielfeldgr_fehlt.setVisible(true);
                    }
                    if(!rb_lokal.isSelected() && !rb_online.isSelected())
                    {
                        lok_on_fehlt.setVisible(true);
                    }
                    if(rb_online.isSelected() && !rb_lokal.isSelected() && auswahl_spieler.getSelectedItem().equals("[choose]"))
                    {
                        spieler_fehlt.setVisible(true);
                    }
                    if(rb_lokal.isSelected() && !rb_online.isSelected() && auswahl_lokal.getSelectedItem().equals("[choose]"))
                    {
                        lok_modus_fehlt.setVisible(true);
                    }
                    if(slider2.getValue() == 0 && slider3.getValue() == 0 && slider4.getValue() == 0 && slider5.getValue() == 0 && slider6.getValue() == 0)
                    {
                        schiffe_fehlen.setVisible(true);
                    }


                    if (selectedSpace == 0 && !((role.equals("Client")) || role.equals("KI_Client_leicht") || role.equals("KI_Client_mittel"))) {
                        System.out.println("Do nothing");
                    } else if (role != null && (GAME.radioButton_l || GAME.radioButton_o) && selectedSpace <= maxSpace) {
                        startbildschirm.setVisible(false);
                        if (GAME.radioButton_o) {
                            int[] ships = {0, 0, anzahl2, anzahl3, anzahl4, anzahl5, anzahl6};
                            int hp = anzahl2 + anzahl3 + anzahl4 + anzahl5 + anzahl6;
                            if (role.equals("Server")) {
                                Spieler player = new Spieler("Server", GAME.spielfeldgr, hp, ships, GAME);
                                Server server = new Server(50000, 0 + "", player, GAME, startbildschirm);
                                player.serverSetter(server);
                                displayoutput.setText("Waiting for Client...");
                                SwingUtilities.invokeLater(() -> {
                                    server.connect();
                                });
                            }
                            if (role.equals("Client")) {
                                Spieler player = new Spieler("Client", GAME.spielfeldgr, hp, null, GAME);
                                Client client = new Client(50000, "localhost", player, GAME, startbildschirm);
                                player.clientSetter(client);
                                SwingUtilities.invokeLater(() -> {
                                    client.connect();
                                });
                            }
                            if (role.equals("KI_Server_leicht")) {
                                leichte_KI_zufall ki = new leichte_KI_zufall("KI_Server_leicht", GAME.spielfeldgr, hp, ships, null);
                                Server kiserver = new Server(50000, 0 + "", ki, GAME, startbildschirm);
                                ki.serverSetter(kiserver);
                                SwingUtilities.invokeLater(() -> {
                                    kiserver.connect();
                                });
                            }
                            if (role.equals("KI_Server_mittel")) {
                                mittlere_KI ki = new mittlere_KI("KI_Server_mittel", GAME.spielfeldgr, hp, ships, null);
                                Server kiserver = new Server(50000, 0 + "", ki, GAME, startbildschirm);
                                ki.serverSetter(kiserver);
                                SwingUtilities.invokeLater(() -> {
                                    kiserver.connect();
                                });
                            }
                            if (role.equals("KI_Client_leicht")) {
                                leichte_KI_zufall ki = new leichte_KI_zufall("KI_Client_leicht", GAME.spielfeldgr, hp, ships, null);
                                Client kiclient = new Client(50000, ClientIP, ki, GAME, startbildschirm);
                                ki.clientSetter(kiclient);
                                SwingUtilities.invokeLater(() -> {
                                    kiclient.connect();
                                });
                            }
                            if (role.equals("KI_Client_mittel")) {
                                mittlere_KI ki = new mittlere_KI("KI_Client_mittel", GAME.spielfeldgr, hp, ships, null);
                                Client kiclient = new Client(50000, ClientIP, ki, GAME, startbildschirm);
                                ki.clientSetter(kiclient);
                                SwingUtilities.invokeLater(() -> {
                                    kiclient.connect();
                                });
                            }
                        }


                    } else if (GAME.radioButton_l && selectedSpace <= maxSpace) {
                        if (auswahl_lokal.getSelectedItem().equals("Spieler vs KI_leicht")) {
                            startbildschirm.setVisible(false);
                            int hp = anzahl2 + anzahl3 + anzahl4 + anzahl5 + anzahl6;
                            int[] ships = {0, 0, anzahl2, anzahl3, anzahl4, anzahl5, anzahl6};
                            Spieler sp1 = new Spieler("Spieler1", GAME.spielfeldgr, hp, ships, GAME);
                            leichte_KI_zufall KI = new leichte_KI_zufall("KI_leicht", GAME.spielfeldgr, hp, ships, GAME);
                            SwingUtilities.invokeLater(() -> {
                                sp1.GAME.Setzen(sp1, KI);
                            });
                        }
                        else if (auswahl_lokal.getSelectedItem().equals("Spieler vs KI_mittel")) {
                            startbildschirm.setVisible(false);
                            int hp = anzahl2 + anzahl3 + anzahl4 + anzahl5 + anzahl6;
                            int[] ships = {0, 0, anzahl2, anzahl3, anzahl4, anzahl5, anzahl6};
                            Spieler sp1 = new Spieler("Spieler1", GAME.spielfeldgr, hp, ships, GAME);
                            mittlere_KI KI = new mittlere_KI("KI_mittel", GAME.spielfeldgr, hp, ships, GAME);
                            SwingUtilities.invokeLater(() -> {
                                sp1.GAME.Setzen(sp1, KI);
                            });
                        }
                        else if (auswahl_lokal.getSelectedItem().equals("Spieler vs Spieler")) {
                            startbildschirm.setVisible(false);
                            int hp = anzahl2 + anzahl3 + anzahl4 + anzahl5 + anzahl6;
                            int[] ships = {0, 0, anzahl2, anzahl3, anzahl4, anzahl5, anzahl6};
                            Spieler sp1 = new Spieler("Spieler1", GAME.spielfeldgr, hp, ships, GAME);
                            Spieler sp2 = new Spieler("Spieler2", GAME.spielfeldgr, hp, ships, GAME);

                            SwingUtilities.invokeLater(() -> {
                                sp1.GAME.Setzen(sp1, sp2);
                            });
                        }
                    } else {
                        System.out.println("Finish all parameters");
                        displayoutput.setVisible(true);
                        displayoutput.setText("Finish all parameters");
                    }

                }
                catch(NullPointerException nullexc)
                {
                    displayoutput.setVisible(true);
                    displayoutput.setText("Finish all parameters");
                }


            }
        });



        JButton laden = new JButton("Spiel laden");
        laden.setPreferredSize(new Dimension(20, 50));
        laden.setBackground(Color.CYAN);

        laden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AllWeNeed loadfile = null;
                try {
                    loadfile = Laden.load();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                try{
                    startbildschirm.setVisible(false);
                    if(loadfile != null && (loadfile.player.name.equals("Client") || loadfile.player.name.equals("Server"))){
                        loadfile.player.name = "Server";
                        loadfile.player.client = null;
                        Server server = new Server(50000, loadfile.ID + "", loadfile.player, GAME, startbildschirm);
                        server.loadtoken = loadfile.player.attackToken;
                        server.toloadthegame = loadfile;
                        loadfile.player.serverSetter(server);
                        SwingUtilities.invokeLater(() -> {server.connect();});
                    }
                    else if(loadfile != null && (loadfile.player.name.equals("KI_Client_leicht") || loadfile.player.name.equals("KI_Server_leicht"))){
                        loadfile.player.name = "KI_Server_leicht";
                        loadfile.player.client = null;
                        Server server = new Server(50000, loadfile.ID + "", loadfile.player, GAME, startbildschirm);
                        server.loadtoken = loadfile.player.attackToken;
                        server.toloadthegame = loadfile;
                        loadfile.player.serverSetter(server);
                        SwingUtilities.invokeLater(() -> {server.connect();});
                    }
                    else if(loadfile != null && (loadfile.player.name.equals("KI_Server_mittel") || loadfile.player.name.equals("KI_Client_mittel"))){
                        loadfile.player.name = "KI_Server_mittel";
                        loadfile.player.client = null;
                        Server server = new Server(50000, loadfile.ID + "", loadfile.player, GAME, startbildschirm);
                        server.loadtoken = loadfile.player.attackToken;
                        server.toloadthegame = loadfile;
                        loadfile.player.serverSetter(server);
                        SwingUtilities.invokeLater(() -> {server.connect();});
                    }
                    else if(loadfile != null && (loadfile.player.name.equals("Spieler1"))){
                        SpielStartLokal newlocal = new SpielStartLokal();
                        newlocal.table = loadfile.table;
                        newlocal.table2 = loadfile.table2;
                        newlocal.table3 = loadfile.table3;
                        newlocal.table4 = loadfile.table4;
                        newlocal.Spieler1 = loadfile.player;
                        newlocal.Spieler2 = loadfile.player2;
                        newlocal.mapSize = loadfile.player.mapSize;
                        newlocal.SpielStartenLokal(null, null);
                    }
                }
                catch(Exception exc){
                    exc.printStackTrace();
                }

            }
        });



        //Hier werden alle Komponenten zum Panel spiel_Start hinzugefuegt
        //Mit passendem Zwischenraum
        spiel_Start.add(Box.createVerticalStrut(10));
        spiel_Start.add(Box.createVerticalGlue());

        spiel_Start.add(Spielfeldgr);
        spiel_Start.add(slider_gr);
        spiel_Start.add(label_slider);
        spiel_Start.add(spielfeldgr_fehlt);

        spiel_Start.add(Box.createVerticalStrut(5));
        spiel_Start.add(Box.createVerticalGlue());

        spiel_Start.add(start);
        spiel_Start.add(start_Text);
        spiel_Start.add(play);

        spiel_Start.add(Box.createVerticalStrut(5));
        spiel_Start.add(Box.createVerticalGlue());

        spiel_Start.add(displayoutput);

        spiel_Start.add(Box.createVerticalStrut(5));
        spiel_Start.add(Box.createVerticalGlue());

        spiel_Start.add(laden);

        spiel_Start.add(Box.createVerticalStrut(10));
        spiel_Start.add(Box.createVerticalGlue());



        //Panel mit Teil der Komponenten:
        //Fuer Spielmodus online/lokal, Spieler-Typ usw.
        JPanel spielAuswahl = new JPanel();

        //Komponenten werden vertikal angeordnet
        spielAuswahl.setLayout(new BoxLayout(spielAuswahl, BoxLayout.PAGE_AXIS));




        auswahl_spieler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((auswahl_spieler.getSelectedItem().equals("Client") || auswahl_spieler.getSelectedItem().equals("KI_Client_leicht") || auswahl_spieler.getSelectedItem().equals("KI_Client_mittel")) && rb_online.isEnabled()){
                    ClientIP = JOptionPane.showInputDialog(startbildschirm, "SERVER IP?", null);
                    System.out.println(ClientIP);

                    spieler_fehlt.setVisible(false);
                }

                //Hinweis in rot welche Werte fehlen
                //Hinweis nicht sichtbar, wenn Spieler-Typ ausgewaehlt
                if(auswahl_spieler.getSelectedItem().equals("Server") || auswahl_spieler.getSelectedItem().equals("KI_Server_leicht") || auswahl_spieler.getSelectedItem().equals("KI_Server_mittel"))
                {
                    spieler_fehlt.setVisible(false);
                }

                Startbildschirm.role = (String) auswahl_spieler.getSelectedItem();
            }
        });





        //Auswahl lokales Spiel
        //Es kann entweder ein lokales Spiel oder ein online Spiel ausgewählt werden
        if(!GAME.radioButton_o)
        {
            rb_lokal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (GAME.radioButton_l)
                    {
                        GAME.radioButton_l = false;
                        rb_online.setEnabled(true);
                        auswahl_lokal.setEnabled(false);
                        lok_modus_fehlt.setVisible(false);
                    }
                    else
                    {
                        auswahl_spieler.setEnabled(false);
                        GAME.radioButton_l = true;
                        rb_online.setEnabled(false);
                        auswahl_lokal.setEnabled(true);

                        lok_on_fehlt.setVisible(false);
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
                        auswahl_spieler.setEnabled(false);
                        GAME.radioButton_o = false;
                        rb_lokal.setEnabled(true);

                        spieler_fehlt.setVisible(false);
                    }
                    else
                    {
                        auswahl_spieler.setEnabled(true);
                        GAME.radioButton_o = true;
                        rb_lokal.setEnabled(false);
                        auswahl_lokal.setEnabled(false);

                        lok_on_fehlt.setVisible(false);
                    }


                }
            });
        }
        else
        {
            rb_online.setEnabled(false);
        }


        //Hier werden alle Komponenten zum Panel spielAuswahl hinzugefuegt
        //Mit passendem Zwischenraum
        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());

        spielAuswahl.add(rb_online);
        spielAuswahl.add(auswahl_spieler);
        spielAuswahl.add(spieler_fehlt);

        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());

        spielAuswahl.add(rb_lokal);
        spielAuswahl.add(auswahl_lokal);
        spielAuswahl.add(lok_modus_fehlt);

        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());

        spielAuswahl.add(lok_on_fehlt);

        spielAuswahl.add(Box.createVerticalStrut(10));
        spielAuswahl.add(Box.createVerticalGlue());





        //Hier werden alle Komponenten zum Panel schiffAuswahl1 hinzugefuegt
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

        schiffAuswahl1.add(schiffe_fehlen);

        schiffAuswahl1.add(Box.createHorizontalStrut(5));
        schiffAuswahl1.add(Box.createHorizontalGlue());



        //Hier werden alle Komponenten zum Panel schiffSlider hinzugefuegt
        //Mit passendem Zwischenraum
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

        schiffSlider.add(schiffe_fehlen);

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
        //startbildschirm.setBackground(Color.LIGHT_GRAY);
        startbildschirm.setExtendedState(JFrame.MAXIMIZED_BOTH);
        startbildschirm.setResizable(true);
        startbildschirm.setVisible(true);

    }

}
