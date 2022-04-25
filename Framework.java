package jeu_de_tir;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
/*Framework that controls the game that created it, update it and draw it on the screen*/
public class Framework extends Canvas {
    private static final long serialVersionUID = 1L;
    int niveau=-1;
    /*Image for menu*/
    private BufferedImage MenuImg;
    /*The actual games*/
    private Game0 game0;
    private Game1 game1;
    private Game2 game2;
    /*FPS(Frames per second) How many times per second the game should update?*/
    private final int GAME_FPS = 60;
    /*Pause between updates, in nanoseconds*/
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    /*Elapsed game time in nanoseconds*/
    private long gameTime;
    /*used for calculating elapsed time*/
    private long lastTime;
    /*Current state of the game*/
    public static GameState gameState;
    /*Possible states of the game*/
    public static enum GameState{STARTING, VISUALIZING, LOADING, MAIN_MENU, HELP, PLAYING0,PLAYING1,PLAYING2,SCOREBOARD,GAMEOVER}
    /*Time of one millisecond in nanoseconds, 1 millisecond = 1 000 000 nanoseconds*/
    public static final long milisecInNanosec = 1000000L;
    /*Time of one second in nanoseconds, 1 second = 1 000 000 000 nanoseconds*/
    public static final long secInNanosec = 1000000000L;
    /*Height of the frame*/
    public static int frameHeight;
    /*Width of the frame*/
    public static int frameWidth;
    public Framework ()
    {
        super();
        gameState = GameState.VISUALIZING;
        /*We start game in new thread*/
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    /*Load files-images,sounds,... This method is intended to load files for this class*/
    private void LoadContent()
    {
        try
        {
            URL MenuImgUrl = this.getClass().getResource("/jeu_de_tir/resources/images/menu.png");
            MenuImg = ImageIO.read(MenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen*/
    private void GameLoop()
    {
        /*These 2 variables are used in VISUALIZING state of the game*/
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        /*These variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS*/
        long beginTime, timeTaken, timeLeft;
        while(true)
        {
            beginTime = System.nanoTime();
            switch (gameState)
            {
                case PLAYING0:
                gameTime += System.nanoTime() - lastTime;
                game0.UpdateGame(gameTime, mousePosition());
                lastTime = System.nanoTime();
                break;
                case PLAYING1:
                gameTime += System.nanoTime() - lastTime;
                game1.UpdateGame(gameTime, mousePosition());
                lastTime = System.nanoTime();
                break;
                case PLAYING2:
                gameTime += System.nanoTime() - lastTime;
                game2.UpdateGame(gameTime, mousePosition());
                lastTime = System.nanoTime();
                break;
                case GAMEOVER:
                break;
                case MAIN_MENU:
                break;
                case HELP:
                break;
                case LOADING:
                break;
                case STARTING:
                LoadContent();
                gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                {
                    frameWidth = this.getWidth();
                    frameHeight = this.getHeight();
                    gameState = GameState.STARTING;
                }
                else
                {
                    visualizingTime += System.nanoTime() - lastVisualizingTime;
                    lastVisualizingTime = System.nanoTime();
                }
                break;
                case SCOREBOARD:
                break;
                default:
                break;
            }
            /*Repaint the screen*/
            repaint();
            
            /*we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS*/
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec;
            /*If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work*/
            if (timeLeft < 10)
            timeLeft = 10;
            try {
                /*Provides the necessary delay and also yields control so that other thread can do work*/
                Thread.sleep(timeLeft);
        } catch (InterruptedException ex) { }
        }
    }
    /*Draw the game to the screen. It is called through repaint() method in GameLoop() method*/
    @Override
    public void Draw(Graphics2D g2d)
    {Font fnt= new Font("arial",Font.BOLD,26);
        Font fnt1= new Font("arial",Font.BOLD,30);
        Font fnt2= new Font("arial",Font.BOLD,50);
        Rectangle Help= new Rectangle(frameWidth/2-100,600,150,50);
        Rectangle Quit= new Rectangle(frameWidth/2-100,700,150,50);
        switch (gameState)
        {
            case PLAYING0:
            game0.Draw(g2d, mousePosition());
            break;
            case PLAYING1:
            game1.Draw(g2d, mousePosition());
            break;
            case PLAYING2:
            game2.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
            g2d.drawImage(MenuImg, 0, 0, frameWidth, frameHeight, null);
            Rectangle Menu= new Rectangle(Framework.frameWidth/2-100,500,150,50);
            Rectangle QuitGame= new Rectangle(Framework.frameWidth/2-100,600,150,50);
            g2d.setFont(fnt1);
            g2d.setColor(Color.white);
            g2d.drawString("Menu",Menu.x+30,Menu.y+35);
            g2d.drawString("Quitter",QuitGame.x+20,QuitGame.y+35);
            g2d.drawString("congatulations! you achieved a score of:",Framework.frameWidth/2-290,350);
            if (niveau==0)
            	    g2d.drawString(String.valueOf(Game0.score),Framework.frameWidth/2-40,400);
            if (niveau==1)
                	g2d.drawString(String.valueOf(Game1.score),Framework.frameWidth/2,400);
            if (niveau==2)
                	g2d.drawString(String.valueOf(Game2.score),Framework.frameWidth/2,400);
            g2d.draw(Menu);
            g2d.draw(QuitGame);
            g2d.setColor(Color.black);
            g2d.drawString("Game Over", Framework.frameWidth / 2 - 99, (int)(Framework.frameHeight * 0.4) + 1);
            g2d.setColor(Color.red);
            g2d.drawString("Game Over", Framework.frameWidth / 2 - 100, (int)(Framework.frameHeight * 0.4));
            break;
            case MAIN_MENU:
            g2d.drawImage(MenuImg, 0, 0, frameWidth, frameHeight, null);
            Rectangle PlayNiv1= new Rectangle(frameWidth/2-100,200,150,50);
            Rectangle PlayNiv2= new Rectangle(frameWidth/2-100,300,150,50);
            Rectangle PlayNiv3= new Rectangle(frameWidth/2-100,400,150,50);
            Rectangle ScoreBoard1= new Rectangle(frameWidth/2-100,500,150,50);
            g2d.setFont(fnt2);
            g2d.setColor(Color.white);
            g2d.drawString("Jeu de Tir",frameWidth/2-150,100);
            g2d.setFont(fnt1);
            g2d.drawString("Niveau1",PlayNiv1.x+20,PlayNiv1.y+35);
            g2d.drawString("Niveau2",PlayNiv2.x+20,PlayNiv2.y+35);
            g2d.drawString("Niveau3",PlayNiv3.x+20,PlayNiv3.y+35);
            g2d.drawString("Aide",Help.x+40,Help.y+35);
            g2d.drawString("Quitter",Quit.x+20,Quit.y+35);
            g2d.draw(PlayNiv1);
            g2d.draw(PlayNiv2);
            g2d.draw(PlayNiv3);
            g2d.draw(ScoreBoard1);
            g2d.draw(Help);
            g2d.draw(Quit);
            g2d.setFont(fnt);
            g2d.drawString("scoreboard",ScoreBoard1.x+5,ScoreBoard1.y+35);
            break;
            case HELP:
            g2d.drawImage(MenuImg, 0, 0, frameWidth, frameHeight, null);
            g2d.setFont(fnt1);
            g2d.setColor(Color.white);
            g2d.drawString("L'objectif est de détruire 20 objets le plus vite possible",frameWidth/2-450,100);
            g2d.drawString("Il ya 3 niveaux:",frameWidth/2-450,175);
            g2d.drawString("niveau1: presente des objets statiques",frameWidth/2-450,250);
            g2d.drawString("niveau2: presente des objets dynamiques, bougant suivant",frameWidth/2-450,325);
            g2d.drawString("des lignes horizontales",frameWidth/2-450,400);
            g2d.drawString("niveau3: presente des objets dynamiques plus difficile a tirer",frameWidth/2-450,475);
            g2d.drawString("Retour",Help.x+20,Help.y+35);
            g2d.drawString("Quitter",Quit.x+20,Quit.y+35);
            g2d.draw(Help);
            g2d.draw(Quit);
            break;
            case LOADING:
            g2d.setColor(Color.white);
            g2d.drawString("Chargement", frameWidth / 2 - 50, frameHeight / 2);
            break;
            case SCOREBOARD:
            g2d.drawImage(MenuImg, 0, 0, frameWidth, frameHeight, null);
            g2d.setFont(fnt1);
            g2d.setColor(Color.white);
            g2d.drawString("ScoreBoard",frameWidth/2-100,40);
            g2d.drawString("Retour",Help.x+20,Help.y+35);
            g2d.drawString("Quitter",Quit.x+20,Quit.y+35);
            g2d.drawString("Niveau1",frameWidth/2-575,70);
            g2d.drawString("Niveau2",frameWidth/2-75,70);
            g2d.drawString("Niveau3",frameWidth/2+425,70);
            g2d.draw(Help);
            g2d.draw(Quit);
            try {
                if (ScoreBoard.IsEmpty("C://niv1.txt")==false)
                {List<String> array = null;
                    try {
                        array = ScoreBoard.Read("C://niv1.txt");
                } catch (IOException e) {e.printStackTrace();}
                    for(int i = 1 ; i < array.size(); i++)
                g2d.drawString(array.get(i),frameWidth/2-575,(i+1)*54);}
        } catch (Exception e) {e.printStackTrace();}
            try {
                if (ScoreBoard.IsEmpty("C://niv2.txt")==false)
                {List<String> array = null;
                    try {
                        array = ScoreBoard.Read("C://niv2.txt");
                } catch (IOException e) {e.printStackTrace();}
                    for(int i = 1 ; i < array.size(); i++)
                g2d.drawString(array.get(i),frameWidth/2-75,(i+1)*54);}
        } catch (Exception e) {e.printStackTrace();}
            try {
                if (ScoreBoard.IsEmpty("C://niv3.txt")==false)
                {List<String> array = null;
                    try {
                        array = ScoreBoard.Read("C://niv3.txt");
                } catch (IOException e) {e.printStackTrace();}
                    for(int i = 1 ; i < array.size(); i++)
                g2d.drawString(array.get(i),frameWidth/2+425,(i+1)*54);}
        } catch (Exception e) {e.printStackTrace();}
            break;
            case STARTING:
            break;
            case VISUALIZING:
            break;
            default:
            break;
        }
    }
    /*Starts new game*/
    private void newGame()
    {/*We set gameTime to zero and lastTime to current time for later calculations*/
        gameTime = 0;
        lastTime = System.nanoTime();
        switch (niveau)
        {
            case 0:
            game0 = new Game0();
            break;
            case 1:
            game1 = new Game1();
            break;
            case 2:
            game2 = new Game2();
            break;
        }
    }
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            if(mp != null)
            return this.getMousePosition();
            else
        return new Point(0, 0);}
        catch (Exception e)
        {return new Point(0, 0);}
    }
    /*This method is called when mouse button is clicked*/
    @Override
    public void mouseClicked(MouseEvent e)
    {
        switch (gameState)
        {
            case MAIN_MENU:
            if(e.getButton() == MouseEvent.BUTTON1)
            if((e.getX()>=frameWidth/2-100) && (e.getX()<=150+frameWidth/2-100)){
                if((e.getY()>=200) && (e.getY()<=250))
                {niveau=0;newGame();}
                if((e.getY()>=300) && (e.getY()<=350))
                {niveau=1;newGame();}
                if((e.getY()>=400) && (e.getY()<=450))
                {niveau=2;newGame();}
                if((e.getY()>=500) && (e.getY()<=550))
                gameState = GameState.SCOREBOARD;
                if((e.getY()>=600) && (e.getY()<=650))
                gameState = GameState.HELP;
                if((e.getY()>=700) && (e.getY()<=750))
                System.exit(0);
            }
            break;
            case HELP:
            if(e.getButton() == MouseEvent.BUTTON1)
            if((e.getX()>=frameWidth/2-100) && (e.getX()<=150+frameWidth/2-100)){
                if((e.getY()>=600) && (e.getY()<=650))
                gameState = GameState.MAIN_MENU;
                if((e.getY()>=700) && (e.getY()<=750))
                System.exit(0);
            }
            break;
            case GAMEOVER:
            if(e.getButton() == MouseEvent.BUTTON1)
            if((e.getX()>=frameWidth/2-100) && (e.getX()<=150+frameWidth/2-100)){
                if((e.getY()>=500) && (e.getY()<=550))
                gameState = GameState.MAIN_MENU;
                if((e.getY()>=600) && (e.getY()<=650))
                System.exit(0);
            }
            break;
            case SCOREBOARD:
            if(e.getButton() == MouseEvent.BUTTON1)
            if((e.getX()>=frameWidth/2-100) && (e.getX()<=150+frameWidth/2-100)){
                if((e.getY()>=600) && (e.getY()<=650))
                gameState = GameState.MAIN_MENU;
                if((e.getY()>=700) && (e.getY()<=750))
                System.exit(0);
            }
            break;
            case LOADING:
            break;
            case PLAYING0:
            break;
            case PLAYING1:
            break;
            case PLAYING2:
            break;
            case STARTING:
            break;
            case VISUALIZING:
            break;
            default:
        break;}
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}