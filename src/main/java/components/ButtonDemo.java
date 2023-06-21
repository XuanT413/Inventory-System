package components;

import javax.swing.*;
import java.awt.*;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class ButtonDemo extends JPanel {
    protected JButton b1, b2, b3, b4;
    protected JMenuBar jmb;
    protected JMenu m1, m2, m3;
    protected JMenuItem jmi1, jmi2, jmi3;
    public ButtonDemo(ActionListener actList) {
//        ImageIcon leftButtonIcon = createImageIcon("images/right.gif");
//        ImageIcon middleButtonIcon = createImageIcon("images/middle.gif");
//        ImageIcon rightButtonIcon = createImageIcon("images/left.gif");

        b1 = new JButton("Add Inventory Items");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        b1.setMnemonic(KeyEvent.VK_A);
        b1.setActionCommand("Add");

        b2 = new JButton("Remove Inventory Item");
        b2.setVerticalTextPosition(AbstractButton.BOTTOM);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        b2.setMnemonic(KeyEvent.VK_R);
        b2.setActionCommand("Remove");

        b3 = new JButton("Notify Peter");
        //Use the default text position of CENTER, TRAILING (RIGHT).
        b3.setMnemonic(KeyEvent.VK_N);
        b3.setActionCommand("Notify");

        b4 = new JButton("Uncheck Items");
        //Use the default text position of CENTER, TRAILING (RIGHT).
        b4.setMnemonic(KeyEvent.VK_U);
        b4.setHorizontalTextPosition(AbstractButton.TRAILING);
        b4.setActionCommand("Uncheck");

        //Listen for actions on buttons 1 and 3.
        b1.addActionListener(actList);
        b2.addActionListener(actList);
        b3.addActionListener(actList);
        b4.addActionListener(actList);

        b1.setToolTipText("Click this button to add an item to the table");
        b2.setToolTipText("Click this button to remove selected items");
        b3.setToolTipText("Click this button to notify Hugh about low-on-stocks items.");
        b4.setToolTipText("Click this button when an item is restocked");

        //Add Components to this container, using the default FlowLayout.
        add(b1);
        add(b2);
        add(b3);
        add(b4);
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
//    protected static ImageIcon createImageIcon(String path) {
//        java.net.URL imgURL = ButtonDemo.class.getResource(path);
//        if (imgURL != null) {
//            return new ImageIcon(imgURL);
//        } else {
//            System.err.println("Couldn't find file: " + path);
//            return null;
//        }
//    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */

}
