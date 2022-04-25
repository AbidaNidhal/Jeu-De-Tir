package jeu_de_tir;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class Game1 {
    private ArrayList<Target> Targets;
    private BufferedImage ScoreImg;
    private BufferedImage TargetImg;
    private BufferedImage backgroundImg;
    private BufferedImage sightImg;
    private Font font;
    private Random random;
    private int killedTargets;
    private int sightImgMiddleHeight;
    private int sightImgMiddleWidth;
    private long StartTime;
    private long lastTimeShoot;
    private long realscore;
    static long score;
    private long scorefix;
    private long timeBetweenShots;
    public Game1()
    {
        Framework.gameState = Framework.GameState.LOADING;
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                Initialize();
                LoadContent();
                Framework.gameState = Framework.GameState.PLAYING1;
            }
        };
        threadForInitGame.start();
    }
    private void Initialize()
    {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        Targets = new ArrayList<Target>();
        killedTargets = 0;
        score = 0;
        scorefix=0;
        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
        StartTime=System.nanoTime();
    }
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/jeu_de_tir/resources/images/Background.png");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            URL ScoreImgUrl = this.getClass().getResource("/jeu_de_tir/resources/images/Score.png");
            ScoreImg = ImageIO.read(ScoreImgUrl);
            URL TargetImgUrl = this.getClass().getResource("/jeu_de_tir/resources/images/target.png");
            TargetImg = ImageIO.read(TargetImgUrl);
            URL sightImgUrl = this.getClass().getResource("/jeu_de_tir/resources/images/Crosshair.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth()/2;
            sightImgMiddleHeight = sightImg.getHeight()/2;
        }
        catch (IOException ex) {
            Logger.getLogger(Game0.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void UpdateGame(long gameTime, Point mousePosition)
    {/*Creates a new target, if it's the time, and add it to the array list*/
        if(System.nanoTime() - Target.lastTargetTime >= Target.timeBetweenTargets)
        {/*create new target and add it to the array list*/
            Targets.add(new Target(Target.targetLines[Target.nextTargetLines][0] + random.nextInt(200), Target.targetLines[Target.nextTargetLines][1], Target.targetLines[Target.nextTargetLines][2], TargetImg));
         /*Here we increase nexttargetLines so that next target will be created in next line*/
            Target.nextTargetLines++;
            if(Target.nextTargetLines >= Target.targetLines.length)
            Target.nextTargetLines = 0;
            Target.lastTargetTime = System.nanoTime();
        }

        /*Update all of the targets*/
        for(int i = 0; i < Targets.size(); i++)
        {  /*Move the target*/
            Targets.get(i).Update();
            /*Checks if the target leaves the screen and remove it if it does*/
            if((Targets.get(i).x<-300) || (Targets.get(i).x>1800 ))
            {
                Targets.remove(i);
            }
        }
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {boolean hit=false;
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                for(int i = 0; i < Targets.size(); i++)
                {
                    if(new Ellipse2D.Double(Targets.get(i).x,Targets.get(i).y,64,64).contains(mousePosition))
                    {try {                     	URL Urlhit = this.getClass().getResource("/jeu_de_tir/resources/music/hit.wav");
                            AudioInputStream audioIn;
                            audioIn = AudioSystem.getAudioInputStream(Urlhit);
                            Clip clip;
                            clip = AudioSystem.getClip();
                            clip.open(audioIn);
                        clip.loop(0);}
                        catch (Exception e) {System.out.println("Error");}
                        killedTargets++;
                        hit=true;
                        Targets.remove(i);
                        break;
                    }
                }
                if (hit==false){try { URL Urlmiss = this.getClass().getResource("/jeu_de_tir/resources/music/miss.wav");
                        AudioInputStream audioIn;
                        audioIn = AudioSystem.getAudioInputStream(Urlmiss);
                        Clip clip;
                        clip = AudioSystem.getClip();
                        clip.open(audioIn);
                    clip.loop(0);}
            catch (Exception e) {System.out.println("Error");}}
                lastTimeShoot = System.nanoTime();
            }
        }
        if(killedTargets == 10) {
            Framework.gameState = Framework.GameState.GAMEOVER;
            scorefix=score;
            try {
                if (ScoreBoard.IsEmpty("C://niv2.txt") == true)
                ScoreBoard.reset("C://niv2.txt");
                } catch (Exception e1) {
                e1.printStackTrace();
            }
            ScoreBoard.AddtoScoreList("1/  " + Long.toString(score)+ "\n","C://niv2.txt");
            ScoreBoard.sortScoreboard("C://niv2.txt");
        }
    }
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(ScoreImg, 0, 0, 500,25, null);
        for(int i = 0; i < Targets.size(); i++)
        {Targets.get(i).Draw(g2d);}
        g2d.drawImage(sightImg, mousePosition.x-sightImgMiddleWidth, mousePosition.y-sightImgMiddleHeight, null);
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        g2d.drawString("KILLS:"+killedTargets,25,20);
        realscore=100-(System.nanoTime()-StartTime)/1000000000;
        score=(realscore>=0 && scorefix==0)?realscore:scorefix;
        g2d.drawString("SCORE:"+score,250,20);
    }
}