package Socket;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;
import java.util.Enumeration;
import GUI.AudioPlayer;
import GUI.SpielStart;
import KI.leichte_KI_zufall;
import KI.mittlere_KI;
import Logik.*;
import ladenspeichern.AllWeNeed;
import ladenspeichern.Speichern;
import javax.swing.*;
import static java.lang.Integer.parseInt;


/**
 *
 * Die Klasse Server enthaelt Methoden zum starten eines Server Socket und verbinden mit einem Client Socket, sowohl um Nachrichten an den Client
 * zu senden und Nachrichten die vom Client kommen fuer normale Spieler und KI abzuarbeiten.
 */

public class Server implements Serializable{
    // Verwendete Portnummer
    public final int port;
    public int status;
    public boolean amZug;
    public final String ID;            //Wenn ID == 0 <= neues Spiel
    public transient BufferedReader in;
    static Writer out;
    public Spieler player;  //This is me

    public JFrame menu;
    public SpielStart GAME;
    public AllWeNeed toloadthegame;
    public boolean loadtoken = false;

    /**
     *
     * Konstruktor erzeugt neues Server Objekt.
     *
     * @param port Port des Servers (abgemacht war 50000)
     * @param id ID wichtig fuer laden und speichern
     * @param player Spieler mit Spielfeld fuer Schussabfrage und wichtige Parameter
     * @param GAME SpielStart Objekt fuer Zugriff auf tables um Images setzen zu koennen
     * @param menu JFrame um Spiel abzubrechen und fuer PopUp Nachrichten
     */
    public Server(int port, String id, Spieler player, SpielStart GAME, JFrame menu){
        this.port = port;
        this.ID = id;
        this.status = 0;
        this.player = player;
        this.amZug = true;
        this.GAME = GAME;
        this.menu = menu;
    }

    /**
     *
     * Methode um Server Socket zu oeffnen, damit ein Client sich mit noetigen Parametern verbinden kann sowie Kommunikationsprotokoll
     *
     */
    public void connect() {
        if(player.attackToken == false){
            player.attackToken = true;
        }
        // Server-Socket erzeugen und an diesen Port binden.
        ServerSocket ss = null;
        try{
            ss = new ServerSocket(port);
        }
        catch(Exception e){
            status = -1;                //Port wird nicht angenommen
            e.printStackTrace();
            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
        }

        Socket s = null;

        try {
            // Die eigene(n) IP-Adresse(n) ausgeben,
            // damit der Benutzer sie dem Benutzer des Clients mitteilen kann.
            System.out.println("My IP address(es):");
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            String adress = "";
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (!ia.isLoopbackAddress()) {
                        System.out.print("" + ia.getHostAddress());
                        adress = adress + ia.getHostAddress() + "\n";
                        //JOptionPane.showMessageDialog(menu, "Server ip: " + ia.getHostAddress());
                    }
                }
            }
            String finalAdress = adress;
            JOptionPane.showMessageDialog(menu, "Server ip Adressen: \n" + finalAdress);
            //ip.setText("Server ip Adressen: " + finalAdress);
            System.out.println("");
        }
        catch(Exception e){
            status = -100;              //IP-Adresse error
            e.printStackTrace();
            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
        }

        try{
            // Auf eine Client-Verbindung warten und diese akzeptieren.
            System.out.println("Waiting for client connection ...");
            s = ss.accept();
            System.out.println("Connection established.");
            //menu.setVisible(false);
            //wait.setVisible(false);
        }
        catch(Exception e){
            status = -2;              //Client-Verbindung hat nicht funktioniert
            e.printStackTrace();
            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
        }

        try{
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());

            //Kommunikationsprotokoll
            if(!(this.ID.equals(0+""))){
                TextClient("load " + this.ID);
                if(player.name.equals("Client") || player.name.equals("Server")){
                    GAME.SpielStarten(player, toloadthegame);
                }
                TextClient("ready");
            }
            else{
                TextClient("size " + this.player.mapSize);
                //Warten auf "ok"
                String done = in.readLine();
                System.out.println("Opponent: " + done);

                String ships = "ships ";
                for(int i = player.remainingShips.length-1;i>1;i--){
                    for(int k = 0; k<player.remainingShips[i]; k++){
                        ships = ships + i + " ";
                    }
                }
                int newhp = player.remainingShips[2] + player.remainingShips[3] + player.remainingShips[4] + player.remainingShips[5] + player.remainingShips[6];
                System.out.println(newhp);
                player.sethps(newhp);
                TextClient(ships);

                //Warten auf "ok"
                String okay = in.readLine();
                System.out.println("Opponent: " + okay);

                if(player.name.equals("Server")) {
                    System.out.println("Server wird gesetzt");
                    player.attackToken = false;
                    SwingUtilities.invokeLater(() -> {
                        GAME.Setzen(player, null);
                    });
                }
                else if(player.name.equals("KI_Server_leicht")){
                    if(player instanceof leichte_KI_zufall){
                        ((leichte_KI_zufall) player).KIplazieren();
                        TextClient("ready");
                    }
                }
                else if(player.name.equals("KI_Server_mittel")){
                    if(player instanceof mittlere_KI){
                        ((mittlere_KI) player).KIplazieren();
                        TextClient("ready");
                    }
                }
                SwingWorker<Void, Void> sw33 = new SwingWorker<Void, Void>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        //"ready" check von Server wird intern geschickt sobald alle Schiffe plaziert sind
                        //und "ready" on Client kommt erst nach "ready" von Server
                        String ready = in.readLine();
                        if(ready.equals("ready")){
                            status = 1;
                            player.attackToken = true;
                            System.out.println("Opponent: " + ready);
                            if(player.name.equals("KI_Server_leicht")){
                                if(player instanceof leichte_KI_zufall){
                                    String newshot = ((leichte_KI_zufall) player).KIshoot();
                                    System.out.println(newshot);
                                    player.server.TextClient(newshot);
                                }
                            }
                            else if(player.name.equals("KI_Server_mittel")){
                                if(player instanceof mittlere_KI){
                                    try{
                                        String newshot = ((mittlere_KI) player).KIshoot();
                                        System.out.println(newshot);
                                        player.server.TextClient(newshot);
                                    }
                                    catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        return null;
                    }
                };
                sw33.execute();
            }
        }
        catch(Exception e){
            status = -3;
            e.printStackTrace();
            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
        }
        SwingWorker<Void, Void> sw3 = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                System.out.println("Server Starting the GAME: ");
                System.out.println(player.name);
                System.out.println(player.mapSize);
                if(player.name.equals("Server")){
                    runGame();
                }
                else if(player.name.equals("KI_Server_leicht") || player.name.equals("KI_Server_mittel")){
                    runGameKI();
                }
                return null;
            }
        };
        sw3.execute();
    }

    /**
     *
     * Methode zum Spielablauf, Methode antwortet auf Nachrichten von Client im Ping-Pong Stil.
     *
     */
    public void runGame(){                              //Eigene Methode fuer SwingWorker
        System.out.println(player.attackToken);
        System.out.println(player.hp + "   " + player.hp2);
        //Server ist immer als erstes am Zug aber falls geladen wird und man war nicht am Zug sende pass zum Client
        if(loadtoken == false && toloadthegame != null){
            player.attackToken = false;
            System.out.println("Spiel wurde geladen aber nicht am Zug!!");
            TextClient("pass");
        }
        //Ping-Pong Prinzip warten auf Befehle
        while(true) {
            try {
                String order = in.readLine();
                System.out.println("Opponent: " + order);
                String[] Osplit = order.split(" ");

                switch (Osplit[0]) {
                    case "answer":  //Antwort fuer Schuss aufs Gegnerische Feld
                        switch (Osplit[1]) {
                            case "0":
                                Runnable k = new Runnable() {
                                    public void run() {
                                        AudioPlayer MusicPlayer = new AudioPlayer();
                                        MusicPlayer.Soundplay(getClass().getResource("Music/water.wav"));
                                    }
                                };

                                new Thread(k).start();
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 0");
                                player.attackToken = false;
                                GAME.setTable2CellBLUE(player.lastShotX, player.lastShotY);
                                TextClient("pass");    //Nicht getroffen Gegner wieder am Zug =================================================================
                                GAME.spielerzugdisplay.setBackground(Color.RED);
                                System.out.println("pass to Opponent");
                                break;
                            case "1":
                                Runnable l = new Runnable() {
                                    public void run() {
                                        AudioPlayer MusicPlayer = new AudioPlayer();
                                        MusicPlayer.Soundplay(getClass().getResource("Music/explode.wav"));
                                    }
                                };

                                new Thread(l).start();
                                //Getroffen (nicht versenkt) Server ist wieder am Zug
                                //GUI wieder freischalten
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 1");
                                player.attackToken = true;
                                GAME.setTable2RedCross(player.lastShotX, player.lastShotY);
                                System.out.println("hp2: " + player.hp2);
                                break;
                            case "2":
                                Runnable j = new Runnable() {
                                    public void run() {
                                        AudioPlayer MusicPlayer = new AudioPlayer();
                                        MusicPlayer.Soundplay(getClass().getResource("Music/explode.wav"));
                                    }
                                };

                                new Thread(j).start();
                                //Getroffen/versenkt    ?Spiel gewonnen?
                                player.hp2 = player.hp2 - 1;
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 2");
                                player.attackToken = true;
                                GAME.setTable2BlackCross(player.lastShotX, player.lastShotY);
                                System.out.println("hp2: " + player.hp2);
                                if (player.hp2 == 0) {
                                    Runnable w = new Runnable() {
                                        public void run() {
                                            AudioPlayer MusicPlayer = new AudioPlayer();
                                            MusicPlayer.Soundplay(getClass().getResource("Music/csgo.wav"));
                                        }
                                    };
                                    new Thread(w).start();
                                    JOptionPane.showMessageDialog(menu, "SPIEL GEWONNEN :D" );
                                    System.out.println("SPIEL GEWONNEN!!!!!!!!!!!!!!!!!!!!!!");
                                    menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
                                }
                                break;
                        }
                        break;
                    case "pass":    //Server wieder am Zug nachdem Client Wasser getroffen hat
                        //Server/Logik.Spieler ist wieder am Zug
                        player.attackToken = true;
                        GAME.spielerzugdisplay.setBackground(Color.GREEN);
                        break;
                    case "shot":    //Opponent hat aufs eigene Spielfeld geschossen
                        String answer = "";
                        try {
                            int x = parseInt(Osplit[1]);
                            int y = parseInt(Osplit[2]);
                            answer = player.shootYourself(x, y);
                            if(answer.equals("answer 0")){
                                GAME.setTableCellBLUE(x, y);
                            }
                            if(answer.equals("answer 1")){
                                GAME.setTableRedCross(x, y);
                            }
                            if(answer.equals("answer 2")){
                                player.hp = player.hp - 1;
                                GAME.setTableBlackCross(x, y);
                            }
                        } catch (Exception e) {
                            System.out.println("Array out of bounds");
                        }
                        TextClient(answer);
                        if (player.hp == 0) {
                            JOptionPane.showMessageDialog(menu, "SPIEL VERLOREN :(" );
                            System.out.println("SPIEL VERLOREN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
                            //Spiel beenden
                        }
                        break;
                    case "save":
                        player.attackToken = false;
                        String filename = Osplit[1];
                        AllWeNeed newsave = new AllWeNeed(true, player,null, GAME.getTable(), GAME.getTable2(), null, null, filename);              //Speichern fuer Online versus
                        //Long newid = newsave.nextId();
                        SwingUtilities.invokeLater(() -> {
                            try {
                                Speichern.save(newsave, filename);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        //Spiel speichern mit Osplit[1] => Client war am Zug
                        break;
                }
            }
            catch (Exception e){}
        }
    }

    /**
     *
     * Methode zum Spielablauf von KI-Klassen, Methode antwortet auf Nachrichten von Client im Ping-Pong Stil.
     *
     */
    public void runGameKI(){
        if(loadtoken == false && toloadthegame != null){
            player.attackToken = false;
            System.out.println("Spiel wurde geladen aber nicht am Zug!!");      //eigentlich momentan nicht moeglich mit Spiel gegen KI
            TextClient("pass");
        }
        //Ping-Pong Prinzip warten auf Befehle
        while(true) {
            try {
                String order = in.readLine();
                System.out.println("Opponent: " + order);
                String[] Osplit = order.split(" ");
                player.attackToken = false;
                switch (Osplit[0]) {
                    case "answer":  //Antwort fuer Schuss aufs Gegnerische Feld
                        switch (Osplit[1]) {
                            case "0":
                                System.out.println("yes?");
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 0");
                                player.attackToken = false;
                                //GAME.setTable2CellBLUE(player.lastShotX, player.lastShotY);
                                System.out.println("no!");
                                TextClient("pass");    //Nicht getroffen Gegner wieder am Zug
                                System.out.println("pass to Opponent");
                                break;
                            case "1":
                                //Getroffen (nicht versenkt) Server ist wieder am Zug
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 1");
                                //GAME.setTable2RedCross(player.lastShotX, player.lastShotY);
                                System.out.println("hp2: " + player.hp2);
                                player.attackToken = true;
                                if(player instanceof leichte_KI_zufall){
                                    String newshot = ((leichte_KI_zufall) player).KIshoot();
                                    TextClient(newshot);
                                }
                                else if(player instanceof mittlere_KI){
                                    String newshot = ((mittlere_KI) player).KIshoot();
                                    TextClient(newshot);
                                }
                                break;
                            case "2":
                                //Getroffen/versenkt    ?Spiel gewonnen?
                                player.hp2 = player.hp2 - 1;
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 2");
                                //GAME.setTable2BlackCross(player.lastShotX, player.lastShotY);
                                System.out.println("hp2: " + player.hp2);
                                if (player.hp2 == 0) {
                                    System.exit(0);
                                    return;   //Programm beenden
                                }
                                //player.attackToken = true;
                                if(player instanceof leichte_KI_zufall){
                                    String newshot = ((leichte_KI_zufall) player).KIshoot();
                                    TextClient(newshot);
                                }
                                else if(player instanceof mittlere_KI){
                                    String newshot = ((mittlere_KI) player).KIshoot();
                                    TextClient(newshot);
                                }
                                break;
                        }
                        break;
                    case "pass":    //Server wieder am Zug nachdem Client Wasser getroffen hat
                        //Server/Logik.Spieler ist wieder am Zug <= muss noch nachgetragen werden =======================================================================
                        if(player instanceof leichte_KI_zufall){
                            String newshot = ((leichte_KI_zufall) player).KIshoot();
                            TextClient(newshot);
                        }
                        else if(player instanceof mittlere_KI){
                            String newshot = ((mittlere_KI) player).KIshoot();
                            TextClient(newshot);
                        }
                        break;
                    case "shot":    //Opponent hat aufs eigene Spielfeld geschossen
                        String answer;
                        int x = 0, y = 0;
                        try {
                            x = parseInt(Osplit[1]);
                            y = parseInt(Osplit[2]);}
                        catch (Exception e) {
                            System.out.println("Array out of bounds");
                        }
                            answer = player.shootYourself(x, y);
                            System.out.println("shotanswer: " + answer);
                            if(answer.equals("answer 0")){
                                System.out.println("answer 0!");
                                //GAME.setTableCellBLUE(x, y);
                            }
                            else if(answer.equals("answer 1")){
                                //GAME.setTableRedCross(x, y);
                            }
                            else if(answer.equals("answer 2")){
                                player.hp = player.hp - 1;
                                System.out.println("player.hp: " + player.hp);
                                //GAME.setTableBlackCross(x, y);
                            }

                        TextClient(answer);
                        if (player.hp == 0) {
                            System.exit(0);
                            return; //Spiel beenden
                        }
                        break;
                    case "save":
                        player.attackToken = false;
                        String filename = Osplit[1];
                        AllWeNeed newsave = new AllWeNeed(true, player,null, GAME.getTable(), GAME.getTable2(), null, null, filename);              //Speichern fuer Online versus
                        //Long newid = newsave.nextId();
                        SwingUtilities.invokeLater(() -> {
                            try {
                                Speichern.save(newsave, filename);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        break;
                }
            }
            catch (Exception e){}
        }
    }

    /**
     *
     * Methode "sendet" String Nachrichten zum Verbundenen Client Socket
     * @param text Nachricht die verschickt werden soll
     */
    public static void TextClient(String text){
        try{
            out.write(String.format("%s%n", text));
            out.flush();
        }
        catch(Exception e){                     //Exception wieder selber behandeln
            e.printStackTrace();                //Fehler Diagnose ausgeben
        }
    }
}
