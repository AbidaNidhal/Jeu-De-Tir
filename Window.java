package jeu_de_tir;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
/*Creates frame and set its properties*/
public class Window extends JFrame{
    private static final long serialVersionUID = 1L;
    private Window()
    {
        /*Sets the title for this frame*/
        this.setTitle("Jeu de Tir");
        /*Sets size of the frame*/
        this.setSize(1500, 800);
        /*Puts frame to center of the screen*/
        this.setLocationRelativeTo(null);
        /*frame cannot be resizable by the user*/
        this.setResizable(false);
        /*Exit the application when user close frame*/
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new Framework());
        this.setVisible(true);
        /*background music*/
        try { 
        	URL Url = this.getClass().getResource("/jeu_de_tir/resources/music/bgmusic.wav");
        	AudioInputStream audioIn;
            audioIn = AudioSystem.getAudioInputStream(Url);
            Clip clip;
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        clip.loop(Clip.LOOP_CONTINUOUSLY);}
        catch (Exception e) {e.printStackTrace();}
    }
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        }
        );
    }
}