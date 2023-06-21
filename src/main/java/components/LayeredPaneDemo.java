package components;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/*
 * LayeredPaneDemo.java requires
 * images/dukeWaveRed.gif.
 */

public class LayeredPaneDemo extends JPanel
        implements ActionListener,
        MouseMotionListener {
    private int numPane = 0;
    private final Color[] layerColors = { Color.yellow.brighter(), Color.magenta.brighter(),
            Color.cyan.brighter(),   Color.red.brighter(),
            Color.green.brighter() };

    private final CustomLayeredPane layeredPane;
    private final JButton addPane, loadjson, testjson;
    private final int offset = 111;
    private final Point origin = new Point(20, 20);

    public LayeredPaneDemo()    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //Create and load the duke icon.
//        final ImageIcon icon = createImageIcon("images/dukeWaveRed.gif");

        //Create and set up the layered pane.
        layeredPane = new CustomLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1280, 720));
        layeredPane.setBorder(BorderFactory.createTitledBorder(
                "Bulletin Board"));
        layeredPane.addMouseMotionListener(this);

        JComponent parent = this;

        testjson = new JButton("+ save json");
//        testjson.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                storeJSON("testjson");
//            }
//        });

        loadjson = new JButton("+ load json");
        loadjson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readJSON("testjson");
            }
        });

        addPane = new JButton("+ Add New Notes");
        addPane.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField jtf = new JTextField();
                int result = JOptionPane.showConfirmDialog(parent, jtf,
                        "Enter the name of the notes: " , JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    numPane++;
                    JTextPane area = createColoredPane(jtf.getText(),
                                                        layerColors[numPane%layerColors.length], origin);
                    origin.x += (origin.x + offset) % 20;
                    origin.y += (origin.y + offset) % 20;
                    new DraggingEvent(area);
                    layeredPane.add(area, new Integer(numPane));
                    storeJSON(area, "testjson");
                }
            }
        });

//        Add control pane and layered pane to this JPanel.
        add(Box.createRigidArea(new Dimension(0, 1)));
        add(createControlPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(layeredPane);

    }

    //Create and set up a colored label.
    private JTextPane createColoredPane(String text,
                                        Color color,
                                        Point origin) {
        String[] initStyles =
                { "regular", "italic", "bold", "small", "large",
                        "regular", "button", "regular", "icon",
                        "regular"
                };

        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();
        addStylesToDocument(doc);
//        textPane.setFont(new Font("Monospaced", Font.BOLD|Font.ITALIC, 20));
        try {
            doc.insertString(doc.getLength(), "*" + text,
                        doc.getStyle("bold"));
            doc.insertString(doc.getLength(), "\n*",
                    doc.getStyle("regular"));

        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }

        textPane.setOpaque(true);
        textPane.setBackground(color);
        textPane.setForeground(Color.black);
        textPane.setBorder(BorderFactory.createLineBorder(Color.black));
        textPane.setBounds(origin.x, origin.y, 240, 240);
        return textPane;
    }

    protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "Monospaced");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        s = doc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);

        s = doc.addStyle("button", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
    }

    public void storeJSON(Object object, String savef) {
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(new File(savef + ".json"), object);
            // convert book object to JSON file
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readJSON (String savef) {
        ObjectOutputStream oos = null;
        try {
            File file = new File(savef +".json");
            oos = new ObjectOutputStream(new FileOutputStream(file));
            for (int i =1; i<numPane; i++) {
                Object[] data = {layeredPane.getComponentsInLayer(i)};
                oos.writeObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    //Create the control pane for the top of the frame.
    private JPanel createControlPanel() {
        JPanel controls = new JPanel();
        controls.add(addPane);
        controls.add(testjson);
        controls.add(loadjson);
        controls.setBorder(BorderFactory.createTitledBorder(
                "Board settings"));
        return controls;
    }

    //Make Duke follow the cursor.
    public void mouseMoved(MouseEvent e) {
//        dukeLabel.setLocation(e.getX()-XFUDGE, e.getY()-YFUDGE);
    }
    public void mouseDragged(MouseEvent e) {} //do nothing

    //Handle user interaction with the check box and combo box.
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("LayeredPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new LayeredPaneDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}