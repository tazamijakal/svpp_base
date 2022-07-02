package GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;

public class AudioPlayer implements LineListener {

    /**
     * playback completes or not
     */
    boolean playCompleted;

    /**
     * Abspielen einer Audio datei
     *
     * @param audioFilePath Pfad der Audio datei
     */
    public void play(URL audioFilePath) {       //URL damit die jar ran kommt
        //File audioFile = new File(audioFilePath);

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFilePath);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.addLineListener(this);

            audioClip.open(audioStream);

                audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl =
                    (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f); // Reduce volume by 10 decibels.

            audioClip.start();

            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            audioClip.close();

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }

    }

    public void Soundplay(URL audioFilePath) {          //URL fuer jar
        //File audioFile = new File(audioFilePath);

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFilePath);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.addLineListener(this);

            audioClip.open(audioStream);

            FloatControl gainControl =
                    (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f); // Reduce volume by 10 decibels.

            audioClip.start();

            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            audioClip.close();

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    public static void playtitle(){
        Runnable k = new Runnable() {
            public void run() {
                AudioPlayer MusicPlayer = new AudioPlayer();
                System.out.println(getClass().getResource("title.wav"));
                MusicPlayer.play(getClass().getResource("tile.wav"));
            }
        };
        new Thread(k).start();
    }

    public static void playwater(){
        Runnable k = new Runnable() {
            public void run() {
                AudioPlayer MusicPlayer = new AudioPlayer();
                System.out.println(getClass().getResource("water.wav"));
                MusicPlayer.play(getClass().getResource("water.wav"));
            }
        };

        new Thread(k).start();
    }

    public static void playexplode(){
        Runnable k = new Runnable() {
            public void run() {
                //String audioFilePath = System.getProperty("user.dir") + "/src/Music/Water Splash Sound FX 1.wav";
                AudioPlayer MusicPlayer = new AudioPlayer();
                System.out.println(getClass().getResource("explode.wav"));
                MusicPlayer.play(getClass().getResource("explode.wav"));
            }
        };
        new Thread(k).start();
    }

    /**
     * Listener fuer start und stop
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();

        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");

        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }
    }
}

