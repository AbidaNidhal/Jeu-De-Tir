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
public class Game0 {
    /*Array list of the targets*/
    private ArrayList<Target> Targets;
    private BufferedImage ScoreImg;
    /*target image*/
    private BufferedImage TargetImg;
    /*Game background image*/
    private BufferedImage backgroundImg;
    /*Shotgun sight image*/
    private BufferedImage sightImg;
    /*Font that we will use to write statistic to the screen*/
    private Font font;
    /*We use this to generate a random number*/
    private Random random;
    /*How many targets the player killed?*/
    private int killedTargets;
    /*Middle height of the sight image*/
    private int sightImgMiddleHeight;
    /*Middle width of the sight image*/
    private int sightImgMiddleWidth;
    private long StartTime;
    /*Last time of the shoot*/
    private long lastTimeShoot;
    private long realscore;
    static long score;
    private long scorefix;
    /*The time which must elapse between shots*/
    private long timeBetweenShots;
    public Game0()
    {
        Framework.gameState = Framework.GameState.LOADING;
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                Initialize();
                LoadContent();
                Framework.gameState = Framework.GameState.PLAYING0;
            }
        };
        threadForInitGame.start();
    }
    /*Set variables and objects for the game*/
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
            for(int i = 0; i <20; i++)
            Targets.add(new Target(100+random.nextInt(1300),100+random.nextInt(600),0,TargetImg));
        }
        catch (IOException ex) {
            Logger.getLogger(Game0.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*Update game logic*/
    public void UpdateGame(long gameTime, Point mousePosition)
    { /*Does player shoots?*/
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        { boolean hit=false;
            /*Checks if it can shoot again*/
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            { /*We go over all the targets and we look if any of them was shot*/
                for(int i = 0; i < Targets.size(); i++)
                { /*We check, if the mouse was over targets when player has shot*/
                    if(new Ellipse2D.Double(Targets.get(i).x,Targets.get(i).y,64,64).contains(mousePosition))
                    { try {
                    	URL Urlhit = this.getClass().getResource("/jeu_de_tir/resources/music/hit.wav");
                            AudioInputStream audioIn;
                            
                            audioIn = AudioSystem.getAudioInputStream(Urlhit);
                            Clip clip;
                            clip = AudioSystem.getClip();
                            clip.open(audioIn);
                        clip.loop(0);}
                        catch (Exception e) {System.out.println("Error");}
                        killedTargets++;
                        hit=true;
                        /*Remove the target from the array list*/
                        Targets.remove(i);
                        /*We found the target that player shoot so we can leave the for loop*/
                        break;
                    }
                }
                if (hit==false){try {                    	URL Urlmiss = this.getClass().getResource("/jeu_de_tir/resources/music/miss.wav");
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
        /*When 20 targets are killed, the game ends*/
        if(killedTargets == 10) {
            Framework.gameState = Framework.GameState.GAMEOVER;
            scorefix=score;
            try {
                if (ScoreBoard.IsEmpty("C://niv1.txt") == true)
                ScoreBoard.reset("C://niv1.txt");
                } catch (Exception e1) {
                e1.printStackTrace();
            }
            ScoreBoard.AddtoScoreList("1/  " + Long.toString(score)+ "\n","C://niv1.txt");
            ScoreBoard.sortScoreboard("C://niv1.txt");
            
        }
    }
    /*Draw the game to the screen*/
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(ScoreImg, 0, 0, 500,25, null);
        /*Here we draw all the targets*/
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