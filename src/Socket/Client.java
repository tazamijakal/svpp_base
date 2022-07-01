package Socket;

import GUI.SpielStart;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;
import static java.lang.Integer.parseInt;

import KI.leichte_KI_zufall;
import KI.mittlere_KI;
import Logik.*;
import Music.AudioPlayerExample2;
import ladenspeichern.AllWeNeed;
import ladenspeichern.Laden;
import ladenspeichern.Speichern;
import org.w3c.dom.Text;

import javax.swing.*;


public class Client implements Serializable{

    public final String ip;
    public final int port;
    public int status;
    public transient BufferedReader in;
    public static Writer out;
    public int size;
    boolean load = false;
    public Spieler player;      // <= me
    //public Spieler player2;     //Opponent
    public JFrame menu;
    public SpielStart GAME;

    /**
     *
     * Konstruktor erzeugt neues Server Objekt.
     *
     * @param port Port des Servers (abgemacht war 50000)
     * @param ip IP um sich mit dem Server Socket zu verbinden
     * @param player Spieler mit Spielfeld fuer Schussabfrage und wichtige Parameter
     * @param GAME SpielStart Objekt fuer Zugriff auf tables um Images setzen zu koennen
     * @param menu JFrame um Spiel abzubrechen und fuer PopUp Nachrichten
     */
    public Client(int port, String ip, Spieler player, SpielStart GAME, JFrame menu){
        this.ip = ip;
        this.port = port;
        this.status = 0;
        this.player = player;
        this.GAME = GAME;
        this.menu = menu;
        //this.player2 = b;
    }

    /**
     *
     * Methode um sich mit Client Socket zu einem Server Socket zu verbinden sowie Kommunikationsprotokoll
     *
     */
    public void connect() {
        Socket s;
        try {
            s = new Socket(this.ip, port);               //will IOException aber einfach durch status selber behandeln
        }
        catch (Exception e){
            this.status = -1;                           //IP Adresse nicht akzeptiert
            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
            return;
        }
        System.out.println("Connection established.");      //wenn keine Exception hat es funktioniert

        try {
            // Ein- und Ausgabestrom des Sockets ermitteln
            // und als BufferedReader bzw. Writer verpacken
            // (damit man zeilen- bzw. zeichenweise statt byteweise arbeiten kann).
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());

            //Kommunikationsprotokoll

            //Test
            //TextServer("hey :)"); //=================================================================================================
            //Test

            //size oder load von Server   => first message
            String first = in.readLine();
            String[] fsplit = first.split(" ");
            if(fsplit[0].equals("load")){
                this.load = true;
                AllWeNeed loadfile = null;
                try {
                    loadfile = Laden.load();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                try{
                    //Wenn geladen wird aber Spieler vorher Server war wird er jetzt zum Client
                    if(loadfile.player.name.equals("Client") || loadfile.player.name.equals("Server")){
                        System.out.println(loadfile.ID);
                        System.out.println(fsplit[1]);
                        if(loadfile.ID.equals(fsplit[1])){
                            loadfile.player.name = "Client";
                            loadfile.player.client = this;
                            loadfile.player.server = null;
                            this.player = loadfile.player;
                            this.size = player.mapSize;
                            this.load = true;
                            System.out.println("Spiel wird geladen");
                            GAME.SpielStarten(player, loadfile);
                        }
                        else{
                            //Falsche ID Game wird geschlossen
                            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
                        }
                    }
                    else if(loadfile.player.name.equals("KI_Client_leicht") || loadfile.player.name.equals("KI_Server_leicht")){
                        System.out.println(loadfile.ID);
                        System.out.println(fsplit[1]);
                        if(loadfile.ID.equals(fsplit[1])){
                            loadfile.player.name = "KI_Client_leicht";
                            loadfile.player.client = this;
                            loadfile.player.server = null;
                            this.player = loadfile.player;
                            this.size = player.mapSize;
                            this.load = true;
                            System.out.println("Spiel wird geladen");
                            //GAME.SpielStarten(player, loadfile);
                        }
                        else{
                            //Falsche ID Game wird geschlossen
                            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
                        }
                    }
                    else if(loadfile.player.name.equals("KI_Client_mittel") || loadfile.player.name.equals("KI_Server_mittel")){
                        System.out.println(loadfile.ID);
                        System.out.println(fsplit[1]);
                        if(loadfile.ID.equals(fsplit[1])){
                            loadfile.player.name = "KI_Client_mittel";
                            loadfile.player.client = this;
                            loadfile.player.server = null;
                            this.player = loadfile.player;
                            this.size = player.mapSize;
                            this.load = true;
                            System.out.println("Spiel wird geladen");
                            //GAME.SpielStarten(player, loadfile);
                        }
                        else{
                            //Falsche ID Game wird geschlossen
                            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
                        }
                    }

                }
                catch(Exception exc){
                    exc.printStackTrace();
                }
                //Spiel laden mit fsplit[1] als ID <= muss noch nachgetragen werden ============================================================================
            }
            else{   //Wir nehmen an Server sendet korrekte Strings

                System.out.println("Opponent: " + first);
                size = parseInt(fsplit[1]);

                //Spielfeldgroesse setzen
                player.mapSize = size;
                player.collisionMap = new int[size][size];
                player.board = new Object[size][size];
                player.visibleBoard = new Object[size][size];
                player.radarMap = new int[size][size];

                //Ping-Pong Prinzip Server wartet auf "ok"
                TextServer("done");

                //Schiffe einlesen     Format: ships 5 4 4 3 3 3 2 2 2 2   (absteigend sortiert)
                String second = in.readLine();
                System.out.println("Opponent: " + second);
                String[] ssplit = second.split(" ");    //Schiffe beginnen ab ssplit[1]
                int[] sc = new int[7];    //entweder 3,4,5,6 oder 2,3,4,5 also ein Feld bleibt 0

                for(int i=1;i<ssplit.length;i++){
                    sc[parseInt(ssplit[i])]++;   //Index von 0-6  Index0: Anzahl 0er Schiffe etc
                }
                //Anzahl 2,3,4,5,6er Schiffe weitergeben
                player.remainingShips = sc;
                int newhp = player.remainingShips[2] + player.remainingShips[3] + player.remainingShips[4] + player.remainingShips[5] + player.remainingShips[6];
                System.out.println(newhp);
                player.sethps(newhp);
                //Server wartet wieder auf "ok"
                TextServer("done");
            }

            if(player.name.equals("Client") && load == false) {
                SwingUtilities.invokeLater(() -> {
                    GAME.Setzen(player, null);
                });
            }
            else if(player.name.equals("KI_Client_leicht") && load == false){
                if(player instanceof leichte_KI_zufall){
                    ((leichte_KI_zufall) player).KIplazieren();
                    TextServer("ready");
                }
            }
            else if(player.name.equals("KI_Client_mittel") && load == false){
                if(player instanceof mittlere_KI){
                    ((mittlere_KI) player).KIplazieren();
                    TextServer("ready");
                }
            }
            // Wenn boolean load == false => neues Spiel erstellen

            SwingWorker<Void, Void> sw1 = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    String third = in.readLine();
                    if(third.equals("ready")){      //Server schickt zuerst "ready"
                        status = 1;
                    }
                    System.out.println("Opponent: " + third);
                    //Warten bis Client auch "ready"
                    TextServer("ready");


                    System.out.println("Client Starting the GAME: ");
                    if(player.name.equals("Client")){
                        runGame();
                    }
                    else if(player.name.equals("KI_Client_leicht") || player.name.equals("KI_Client_mittel")){
                        runGameKI();
                    }
                    return null;
                }
            };
            sw1.execute();
            player.attackToken = false;
        }
        catch(Exception e){
            this.status = -2;
            e.printStackTrace();
            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
            return;
        }

    }

    /**
     *
     * Methode zum Spielablauf, Methode antwortet auf Nachrichten von Server im Ping-Pong Stil.
     *
     */
    public void runGame(){                  //Eigene Methode fuer SwingWorker
        System.out.println(player.hp + "   " + player.hp2);
        try {
            //Ping-Pong Prinzip warten auf Befehle
            while (true) {
                String order = in.readLine();
                System.out.println("Opponent: " + order);
                String[] Osplit = order.split(" ");
                switch (Osplit[0]) {
                    case "answer":  //Antwort fuer Schuss aufs Gegnerische Feld
                        switch (Osplit[1]) {
                            case "0":
                                Runnable k = new Runnable() {
                                    public void run() {
                                        String audioFilePath = "src/Music/Water Splash Sound FX 1.wav";
                                        AudioPlayerExample2 MusicPlayer = new AudioPlayerExample2();
                                        MusicPlayer.Soundplay(audioFilePath);
                                    }
                                };

                                new Thread(k).start();
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 0");

                                player.attackToken = false;
                                GAME.setTable2CellBLUE(player.lastShotX, player.lastShotY);
                                TextServer("pass");    //Nicht getroffen Gegner wieder am Zug =================================================================
                                System.out.println("pass to Opponent");
                                break;
                            case "1":
                                Runnable l = new Runnable() {
                                    public void run() {
                                        String audioFilePath = "src/Music/Explosion vol.4 Artillery explosion Sound effects.wav";
                                        AudioPlayerExample2 MusicPlayer = new AudioPlayerExample2();
                                        MusicPlayer.Soundplay(audioFilePath);
                                    }
                                };

                                new Thread(l).start();
                                //Getroffen (nicht versenkt) Client ist wieder am Zug =================================================================
                                //GUI wieder freischalten oder boolean in Spieler Objekt??!
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 1");
                                player.attackToken = true;
                                GAME.setTable2RedCross(player.lastShotX, player.lastShotY);
                                break;
                            case "2":
                                Runnable j = new Runnable() {
                                    public void run() {
                                        String audioFilePath = "src/Music/Explosion vol.4 Artillery explosion Sound effects.wav";
                                        AudioPlayerExample2 MusicPlayer = new AudioPlayerExample2();
                                        MusicPlayer.Soundplay(audioFilePath);
                                    }
                                };

                                new Thread(j).start();
                                //Getroffen/versenkt    ?Spiel gewonnen? ======================================================================
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 2");
                                player.hp2 = player.hp2 - 1;
                                player.attackToken = true;
                                GAME.setTable2BlackCross(player.lastShotX, player.lastShotY);
                                if (player.hp2 == 0) {
                                    JOptionPane.showMessageDialog(menu, "SPIEL GEWONNEN :D" );
                                    Runnable w = new Runnable() {
                                        public void run() {
                                            String audioFilePath = "src/Music/Various Artists - Hotline Miami  CSGO MVP Music.wav";
                                            AudioPlayerExample2 MusicPlayer = new AudioPlayerExample2();
                                            MusicPlayer.Soundplay(audioFilePath);
                                        }
                                    };
                                    new Thread(w).start();
                                    System.out.println("SPIEL GEWONNEN!!!!!!!!!!!!!!!!!!!!!!");
                                    menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
                                }
                                break;
                        }
                        break;
                    case "pass":    //Client wieder am Zug nachdem Server Wasser getroffen hat
                        //Client/Logik.Spieler ist wieder am Zug <= muss noch nachgetragen werden =======================================================================
                        //TextServer("pass");   //=======Dummy zum ausprobieren
                        player.attackToken = true;
                        break;
                    case "shot":  //Opponent hat aufs eigene Spielfeld geschossen
                        //Ueberpruefen ob Opponent getroffen hat und dann richtiges "answer" zurueckschicken
                        String answer = "";
                        try {
                            int x = parseInt(Osplit[1]);
                            int y = parseInt(Osplit[2]);
                            answer = player.shootYourself(x, y);
                            if(answer.equals("answer 0")){
                                GAME.setTableCellBLUE(x,y);
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
                        TextServer(answer);
                        if (player.hp == 0) {     //Spiel zu ende?
                            JOptionPane.showMessageDialog(menu, "SPIEL VERLOREN :(" );
                            System.out.println("SPIEL VERLOREN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));
                            //Spiel beenden   ===========================================================================
                        }
                        break;
                    case "save":
                        player.attackToken = false;
                        player.load = false;
                        System.out.println(Osplit[1]);
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
                        //Spiel speichern mit Osplit[1] => Server war am Zug ==========================================================================
                        break;
                }
                //TextServer("okay :) Client");
            }
        }
        catch (Exception e){}
    }

    /**
     *
     * Methode zum Spielablauf von KI-Klassen, Methode antwortet auf Nachrichten von Client im Ping-Pong Stil.
     *
     */
    public void runGameKI(){                  //Eigene Methode fuer SwingWorker
        System.out.println(player.hp + "   " + player.hp2);
        try {
            //Ping-Pong Prinzip warten auf Befehle
            while (true) {
                int count = 0;
                System.out.println(count++);
                String order = in.readLine();
                System.out.println("Opponent: " + order);
                String[] Osplit = order.split(" ");
                switch (Osplit[0]) {
                    case "answer":  //Antwort fuer Schuss aufs Gegnerische Feld
                        switch (Osplit[1]) {
                            case "0":
                                //player.answerReader(player.lastShotX, player.lastShotY, "answer 0");
                                player.visibleBoard[player.lastShotX][player.lastShotY] = new Spieler.MisfireObject();
                                player.attackToken = false;
                                //GAME.setTable2CellBLUE(player.lastShotX, player.lastShotY);
                                TextServer("pass");    //Nicht getroffen Gegner wieder am Zug =================================================================
                                System.out.println("pass to Opponent");
                                break;
                            case "1":
                                //Getroffen (nicht versenkt) Client ist wieder am Zug =================================================================
                                //GUI wieder freischalten oder boolean in Spieler Objekt??!
                                //player.answerReader(player.lastShotX, player.lastShotY, "answer 1");
                                player.visibleBoard[player.lastShotX][player.lastShotY] = new Spieler.TrefferObject();
                                //GAME.setTable2RedCross(player.lastShotX, player.lastShotY);
                                player.attackToken = true;
                                if(player instanceof leichte_KI_zufall){
                                    String newshot = ((leichte_KI_zufall) player).KIshoot();
                                    TextServer(newshot);
                                }
                                else if(player instanceof mittlere_KI){
                                    String newshot = ((mittlere_KI) player).KIshoot();
                                    TextServer(newshot);
                                }

                                break;
                            case "2":
                                //Getroffen/versenkt    ?Spiel gewonnen? ======================================================================
                                //player.answerReader(player.lastShotX, player.lastShotY, "answer 2");
                                player.visibleBoard[player.lastShotX][player.lastShotY] = new Spieler.TrefferObject();
                                player.hp2 = player.hp2 - 1;
                                //GAME.setTable2BlackCross(player.lastShotX, player.lastShotY);
                                player.attackToken = true;
                                System.out.println("player.hp2: " + player.hp2);
                                if (player.hp2 == 0) {
                                    System.exit(0);
                                    return;              //Spiel beenden
                                }
                                if(player instanceof leichte_KI_zufall){
                                    String newshot = ((leichte_KI_zufall) player).KIshoot();
                                    TextServer(newshot);
                                }
                                else if(player instanceof mittlere_KI){
                                    String newshot = ((mittlere_KI) player).KIshoot();
                                    TextServer(newshot);
                                }
                                break;
                        }
                        break;
                    case "pass":    //Client wieder am Zug nachdem Server Wasser getroffen hat
                        //Client/Logik.Spieler ist wieder am Zug <= muss noch nachgetragen werden =======================================================================
                        //TextServer("pass");   //=======Dummy zum ausprobieren
                        System.out.println("KI-pass taking new shot");
                        player.attackToken = true;
                        if(player instanceof leichte_KI_zufall){
                            String newshot = ((leichte_KI_zufall) player).KIshoot();
                            TextServer(newshot);
                        }
                        else if(player instanceof mittlere_KI){
                            String newshot = ((mittlere_KI) player).KIshoot();
                            TextServer(newshot);
                        }
                        System.out.println("shot was taken");
                        break;
                    case "shot":  //Opponent hat aufs eigene Spielfeld geschossen
                        //Ueberpruefen ob Opponent getroffen hat und dann richtiges "answer" zurueckschicken
                        String answer = "";
                        try {
                            int x = parseInt(Osplit[1]);
                            int y = parseInt(Osplit[2]);
                            answer = player.shootYourself(x, y);
                            if(answer.equals("answer 0")){
                                GAME.setTableCellBLUE(x,y);
                            }
                            if(answer.equals("answer 1")){
                                //GAME.setTableRedCross(x, y);
                            }
                            if(answer.equals("answer 2")){
                                player.hp = player.hp - 1;
                                System.out.println("playerhp: " + player.hp);
                                //GAME.setTableBlackCross(x, y);
                            }
                        } catch (Exception e) {
                            System.out.println("Array out of bounds");
                        }
                        TextServer(answer);
                        if (player.hp == 0) {     //Spiel zu ende?
                            System.exit(0);
                            return;
                            //Spiel beenden   ===========================================================================
                        }
                        break;
                    case "save":
                        player.attackToken = false;
                        player.load = false;
                        System.out.println(Osplit[1]);
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
                //TextServer("okay :) Client");
            }
        }
        catch (Exception e){}
    }

    /**
     *
     * Methode sendet String Nachrichten zum Verbundenen Server Socket
     *
     */
    public static void TextServer(String text){
        try{
            out.write(String.format("%s%n", text));
            out.flush();
        }
        catch(Exception e){                     //Exception wieder selber behandeln
            e.printStackTrace();                //Fehler Diagnose ausgeben
        }

    }


    /*public static void main(String[] args) {
        Client p1 = new Client(50000,"localhost",new Spieler("client", 21, 7, new int[7]));
        p1.connect();
    }*/

}
