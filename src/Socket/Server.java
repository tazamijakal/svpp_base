package Socket;

import java.net.*;
import java.io.*;

public class Server {
    // Verwendete Portnummer
    public final int port;
    public int status;
    public final int ID;
    BufferedReader in;
    static Writer out;

    public Server(int p, int id){
        this.port = p;
        this.ID = id;
        this.status = 0;
    }
    
    
    public void connect(){
        
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
                TextClient("size " + 10 /*Spielfeldgroesse ==============*/); //===========================================================
                //Warten auf "ok"
                String done = in.readLine();
                System.out.println("Opponent: " + done);

                //"ships" + *shipscount* <= muss noch nachgetragen werden =======================================================================

                //Dummy statement zum testen
                TextClient("ships 5 5 5 4 4 4 3 3 2");

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
                TextClient("okay :)");

                //Ping-Pong Prinzip warten auf Befehle
                while(true){
                    String order = in.readLine();
                    System.out.println("Opponent: " + order);
                    String[] Osplit = order.split(" ");

                    switch (Osplit[0]){
                        case "answer":  //Antwort fuer Schuss aufs Gegnerische Feld
                            switch(Osplit[1]){
                                case "0":
                                    TextClient("pass");    //Nicht getroffen Gegner wieder am Zug =================================================================
                                case "1":
                                    //Getroffen (nicht versenkt) Server ist wieder am Zug =================================================================
                                case "2":
                                    //Getroffen/versenkt    ?Spiel gewonnen? ======================================================================
                            }
                        case "pass":    //Server wieder am Zug nachdem Client Wasser getroffen hat
                            //Server/Spieler ist wieder am Zug <= muss noch nachgetragen werden =======================================================================
                        case "shot":    //Opponent hat aufs eigene Spielfeld geschossen
                            //Ueberpruefen ob Opponent getroffen hat und dann richtiges "answer" zurueckschicken ?Spiel zu ende?========================================
                        case "save":
                            //Spiel speichern mit Osplit[1] => Client war am Zug ==========================================================================
                    }
                    TextClient("okay :)");

                }
            }
        }
        catch(Exception e){
            status = -3;
            e.printStackTrace();
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

    public static void main(String[] args) {
        Server s1 = new Server(50000,0);        //port + ID
        s1.connect();
    }

}
