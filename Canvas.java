package jeu_de_tir;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
/*Create a JPanel on which we draw and listen for mouse events*/
public abstract class Canvas extends JPanel implements KeyListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private static boolean mouseState = false;
    public Canvas()
    {
        /*double buffer to draw on the screen*/
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        /*Adds the mouse listener to JPanel to receive mouse events from this component*/
        this.addMouseListener(this);
    }
    /*This method is overridden in Framework.java and is used for drawing to the screen*/
    public abstract void Draw(Graphics2D g2d);
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        Draw(g2d);
    }
    public static boolean mouseButtonState(int button){return mouseState;}
    private void mouseKeyStatus(MouseEvent e, boolean status)
    {if(e.getButton() == MouseEvent.BUTTON1)
    mouseState=status;}
    /*Methods of the mouse listener*/
    @Override
    public void mousePressed(MouseEvent e){mouseKeyStatus(e,true);}
    @Override
    public void mouseReleased(MouseEvent e){mouseKeyStatus(e,false);}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}