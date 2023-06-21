package components;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DraggingEvent implements MouseListener, MouseMotionListener {
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;
    private final JComponent jcomp;

    public DraggingEvent(JComponent jc){
        jcomp = jc;
        jcomp.addMouseMotionListener(this);
        jcomp.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        myX = jcomp.getX();
        myY = jcomp.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;
        jcomp.setLocation(myX + deltaX, myY + deltaY);
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

}
