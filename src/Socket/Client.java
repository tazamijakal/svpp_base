package Socket;

import org.w3c.dom.Text;

import java.net.*;
import java.io.*;
import static java.lang.Integer.parseInt;


public class Client {

    public final String ip;
    public final int port;
    public int status;
    public BufferedReader in;
    public static Writer out;
    public int size;
    boolean load = false;


    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.status = 0;
    }

    public void connect() {
        Socket s;
        try {
            s = new Socket(this.ip, port);               //will IOException aber einfach durch status selber behandeln
        }
        catch (Exception e){
            this.status = -1;                           //IP Adresse nicht akzeptiert
            return;
        }
        System.out.println("Connection established.");      //wenn keine Exception hat es funktioniert

        try {
            // Ein- und Ausgabestrom des Sockets ermitteln
            // und als BufferedReader bzw. Writer verpacken
            // (damit man zeilen- bzw. zeichenweise statt byteweise arbeiten kann).
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.out = new OutputStreamWriter(s.getOutputStream());

            //Kommunikationsprotokoll

            //Test
            //TextServer("hey :)"); //=================================================================================================
            //Test

            //size oder load von Server   => first message
            String first = in.readLine();
            String[] fsplit = first.split(" ");
            if(fsplit[0].equals("load")){
                this.load = true;
                //Spiel laden mit fsplit[1] als ID <= muss noch nachgetragen werden ============================================================================
            }
            else{   //Wir nehmen an Server sendet korrekte Strings

                System.out.println("Opponent: " + first);
                size = parseInt(fsplit[1]);

                //Spielfeldgroesse setzen auf fsplit[1] <= muss noch nachgetragen werden ==========================================================================

                //Ping-Pong Prinzip Server wartet auf "ok"
                TextServer("done");

                //Schiffe einlesen     Format: ships 5 4 4 3 3 3 2 2 2 2   (absteigend sortiert)
                String second = in.readLine();
                System.out.println("Opponent: " + second);
                String[] ssplit = second.split(" ");    //Schiffe beginnen ab ssplit[1]
                int[] shipcounts = new int[5];    //entweder 3,4,5,6 oder 2,3,4,5 also ein Feld bleibt 0

                for(int i=1;i<ssplit.length;i++){
                    shipcounts[parseInt(ssplit[i]) - 2]++;   //-2 da Index von 0-4  Index0: Anzahl 2er Schiffe etc
                }
                //Anzahl 2,3,4,5,6er Schiffe weitergeben <= muss noch nachgetragen werden ==================================================================================

                //Server wartet wieder auf "ok"
                TextServer("done");
            }

            //Schiffe auf Spielfeld plazieren <= muss noch nachgetragen werden ==========================================================================================
            //Wenn boolean load = false => neues Spiel erstellen

            if(load == false){
                String third = in.readLine();
                if(third.equals("ready")){      //Server schickt zuerst "ready"
                    this.status = 1;
                }
                System.out.println("Opponent: " + third);
                //Warten bis Client auch "ready"
                TextServer("ready");
            }

            //Ping-Pong Prinzip warten auf Befehle
            while(true){
                String order = in.readLine();
                System.out.println("Opponent: " + order);
                String[] Osplit = order.split(" ");

                switch (Osplit[0]){
                    case "answer":  //Antwort fuer Schuss aufs Gegnerische Feld
                        switch(Osplit[1]){
                            case "0":
                                TextServer("pass");    //Nicht getroffen Gegner wieder am Zug =================================================================
                            case "1":
                                //Getroffen (nicht versenkt) Client ist wieder am Zug =================================================================
                            case "2":
                                //Getroffen/versenkt    ?Spiel gewonnen? ======================================================================
                        }
                    case "pass":    //Client wieder am Zug nachdem Server Wasser getroffen hat
                        //Client/Spieler ist wieder am Zug <= muss noch nachgetragen werden =======================================================================
                    case "shot":    //Opponent hat aufs eigene Spielfeld geschossen
                        //Ueberpruefen ob Opponent getroffen hat und dann richtiges "answer" zurueckschicken ?Spiel zu ende?========================================
                    case "save":
                        //Spiel speichern mit Osplit[1] => Server war am Zug ==========================================================================
                }
                TextServer("okay :)");
            }
        }
        catch(Exception e){
            this.status = -2;
            e.printStackTrace();
            return;
        }
    }

    public static void TextServer(String text){
        try{
            out.write(String.format("%s%n", text));
            out.flush();
        }
        catch(Exception e){                     //Exception wieder selber behandeln
            e.printStackTrace();                //Fehler Diagnose ausgeben
        }

    }

    public static void main(String[] args) {
        Client p1 = new Client("localhost",50000);
        p1.connect();
    }

}
