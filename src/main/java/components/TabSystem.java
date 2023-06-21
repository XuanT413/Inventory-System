package components;

/*
 * TabComponentDemo.java requires one additional file:
 *   ButtonTabComponent.java
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/*
 * Creating and using TabComponentsDemo example
 */
public class TabSystem extends JFrame {

    private final int tabNumber = 4;
    private final JTabbedPane pane = new JTabbedPane();
    private JMenuItem tabComponentsItem;
    private JMenuItem scrollLayoutItem;
    private final Dimension tabSize = new Dimension(1280, 720);

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new TabSystem("Inventory Management");
            }
        });
    }

    public TabSystem(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        initMenu();
        setLayout(new BorderLayout());
        getContentPane().add(pane, BorderLayout.CENTER);
        runTest();
//        scrollLayoutItem.setSelected(false);
//        setPreferredSize(tabSize);
//        setLocationRelativeTo(null);
    }

    public void setPane(int i, Component comp){
        pane.setComponentAt(i, comp);
    }
    public Dimension getTabSize(){
        return tabSize;
    }

    public JTabbedPane getTabs(){
        return pane;
    }

    public void runTest() {
        pane.removeAll();
        for (int i = 0; i < tabNumber; i++) {
            pane.add("Table " +i ,new JPanel());
//            initTabComponent(i);
        }
//        tabComponentsItem.setSelected(false);
        pane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    }


    private void initTabComponent(int i) {
        pane.setTabComponentAt(i,
                new ButtonTabComponent(pane));
    }

    //Setting menu

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        //create Options menu
        tabComponentsItem = new JCheckBoxMenuItem("Use TabComponents", true);
        tabComponentsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
        tabComponentsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < pane.getTabCount(); i++) {
                    if (tabComponentsItem.isSelected()) {
                        initTabComponent(i);
                    } else {
                        pane.setTabComponentAt(i, null);
                    }
                }
            }
        });
        scrollLayoutItem = new JCheckBoxMenuItem("Set ScrollLayout");
        scrollLayoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        scrollLayoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (pane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
                    pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                } else {
                    pane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
                }
            }
        });
        JMenuItem resetItem = new JMenuItem("Reset JTabbedPane");
        resetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK));
        resetItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runTest();
            }
        });
        resetItem.setEnabled(false);

        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.add(tabComponentsItem);
        optionsMenu.add(scrollLayoutItem);
        optionsMenu.add(resetItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }
}
