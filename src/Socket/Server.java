package Socket;

import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;
import java.util.Enumeration;

import GUI.SpielStart;
import GUI.Startbildschirm;
import Logik.*;

import javax.swing.*;

import static java.lang.Integer.parseInt;


public class Server {
    // Verwendete Portnummer
    public final int port;
    public int status;
    public boolean amZug;
    public final int ID;            //Wenn ID == 0 <= neues Spiel
    BufferedReader in;
    static Writer out;
    public Spieler player;  //This is me
    //public Spieler player2; //Opponent oder schon in player?

    public JFrame menu;
    public SpielStart GAME;

    public Server(int p, int id, Spieler a, SpielStart GAME, JFrame menu){
        this.port = p;
        this.ID = id;
        this.status = 0;
        this.player = a;
        this.amZug = true;
        this.GAME = GAME;
        this.menu = menu;
    }
    
    
    public void connect(){
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
        }

        Socket s = null;

        try {
            // Die eigene(n) IP-Adresse(n) ausgeben,
            // damit der Benutzer sie dem Benutzer des Clients mitteilen kann.
            System.out.println("My IP address(es):");
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (!ia.isLoopbackAddress()) {
                        System.out.print(" " + ia.getHostAddress());
                    }
                }
            }
            System.out.println("");
        }
        catch(Exception e){
            status = -100;              //IP-Adresse error
            e.printStackTrace();
        }

        try{
            // Auf eine Client-Verbindung warten und diese akzeptieren.
            System.out.println("Waiting for client connection ...");
            s = ss.accept();
            System.out.println("Connection established.");
        }
        catch(Exception e){
            status = -2;              //Client-Verbindung hat nicht funktioniert
            e.printStackTrace();
        }

        try{
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());

            //Kommunikationsprotokoll
            if(this.ID != 0){
                TextClient("load " + this.ID);
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

                TextClient(ships);

                //Warten auf "ok"
                String okay = in.readLine();
                System.out.println("Opponent: " + okay);

                //Dummy "ready"      ============================================
                TextClient("ready");

                //"ready" check von Server wird intern geschickt sobald alle Schiffe plaziert sind
                //und "ready" on Client kommt erst nach "ready" von Server

                String ready = in.readLine();
                if(ready.equals("ready")){
                    status = 1;
                    System.out.println("Opponent: " + ready);
                }

                //Spiel starten
                //Wenn Client am Zug war (load) pass texten
                //TextClient("pass");
                System.out.println("Server Starting the GAME: ");
                System.out.println(player.name);
                System.out.println(player.mapSize);

                //SwingUtilities.invokeLater(() -> {SpielStart.SpielStarten(player);});
                //menu.setVisible(true);
                //SwingUtilities.invokeLater(() -> {SpielStart.SpielStarten(player);});

                SwingWorker<Void, Void> sw3 = new SwingWorker<Void, Void>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        runGame();
                        return null;
                    }
                };
                sw3.execute();

                SwingUtilities.invokeLater(() -> {GAME.SpielStarten(player);});
                System.out.println("TEST");
            }
        }
        catch(Exception e){
            status = -3;
            e.printStackTrace();
        }
        
    }

    public void runGame(){                              //Eigene Methode fuer SwingWorker
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
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 0");
                                player.attackToken = false;
                                GAME.setTable2CellBLUE(player.lastShotX, player.lastShotY);
                                TextClient("pass");    //Nicht getroffen Gegner wieder am Zug =================================================================
                                System.out.println("pass to Opponent");
                            case "1":
                                //Getroffen (nicht versenkt) Server ist wieder am Zug =================================================================
                                //GUI wieder freischalten oder boolean in Spieler Objekt??!
                                player.hp2 = player.hp2 - 1;
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 1");
                                player.attackToken = true;
                            case "2":
                                //Getroffen/versenkt    ?Spiel gewonnen? ======================================================================
                                player.hp2 = player.hp2 - 1;
                                player.answerReader(player.lastShotX, player.lastShotY, "answer 2");
                                player.attackToken = true;
                                if (player.hp2 == 0) {
                                    System.out.println("SPIEL GEWONNEN!!!!!!!!!!!!!!!!!!!!!!");
                                }
                        }
                        break;
                    case "pass":    //Server wieder am Zug nachdem Client Wasser getroffen hat
                        //Server/Logik.Spieler ist wieder am Zug <= muss noch nachgetragen werden =======================================================================
                        //Dummy zum testen
                        //TextClient("pass");
                        player.attackToken = true;
                        break;
                    case "shot":    //Opponent hat aufs eigene Spielfeld geschossen
                        String answer = "";
                        try {
                            int x = parseInt(Osplit[1]);
                            int y = parseInt(Osplit[2]);
                            answer = player.shootYourself(x, y);
                        } catch (Exception e) {
                            System.out.println("Array out of bounds");
                        }
                        TextClient(answer);
                        if (player.hp == 0) {
                            System.out.println("SPIEL VERLOREN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            //Spiel beenden
                        }
                        break;
                    case "save":
                        player.attackToken = false;
                        //Spiel speichern mit Osplit[1] => Client war am Zug ==========================================================================
                        break;
                }
                //TextClient("okay :) Server");

            }
            catch (Exception e){}
        }

    }

    public static void TextClient(String text){
        try{
            out.write(String.format("%s%n", text));
            out.flush();
        }
        catch(Exception e){                     //Exception wieder selber behandeln
            e.printStackTrace();                //Fehler Diagnose ausgeben
        }

    }

    /*public static void main(String[] args) {
        int[] a = {0,0,1,2,3,4,2};
        Server s1 = new Server(50000,0, new Spieler("server", 21, 7, a));        //port + ID
        s1.connect();
    }*/

}
