package jeu_de_tir;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
public class Target {
    /*How much time must pass in order to create a new target?*/
    public static long timeBetweenTargets = Framework.secInNanosec;
    /*Last time when the target was created*/
    public static long lastTargetTime = 0;
    /*lines: Where is starting location for the target? Speed of the target?*/
    public static int[][] targetLines = {
        {Framework.frameWidth, (int)(Framework.frameHeight * 0.1), -1},
        {Framework.frameWidth, (int)(Framework.frameHeight * 0.3), -2},
        {Framework.frameWidth, (int)(Framework.frameHeight * 0.5), -3},
        {Framework.frameWidth, (int)(Framework.frameHeight * 0.7), -4},
        {-264, (int)(Framework.frameHeight * 0.2), 4},
        {-264, (int)(Framework.frameHeight * 0.4), 3},
        {-264, (int)(Framework.frameHeight * 0.6), 2},
        {-264, (int)(Framework.frameHeight * 0.8), 1}
    };
    /*target image*/
    private BufferedImage targetImg;
    /*How fast the target should move?*/
    private int speed;
    /* x coordinate of the target*/
    public int x;
    /*y coordinate of the target*/
    public int y;
    /*Indicate which is next target line*/
    public static int nextTargetLines = 0;
    /*Creates new target*/
    public Target(int x, int y, int speed, BufferedImage targetImg)
    {
        this.speed = speed;
        this.targetImg = targetImg;
        this.x = x;
        this.y = y;
    }
    /*Move the target*/
    public void Update(){x += speed;}
    public void Update1(){x += speed;
        if( x%500<=250){y += speed;}
if( x%500>=250){y -= speed;}}
    /*Draw the target on the screen*/
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(targetImg, x, y, null);
    }
}