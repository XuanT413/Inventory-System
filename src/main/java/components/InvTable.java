package components;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InvTable{

    public static JTextArea debugScr = new JTextArea();
    private static JFrame frame;
    private static JTextArea leftPane;
    private static final ArrayList<String> lowStock = new ArrayList<>();
    private static final ArrayList<Point> lowPoint = new ArrayList<>();
    private static CustomPanel cp1;
    private static CustomPanel cp2;
    private static LayeredPaneDemo layerPane;
    public static JTextArea rightPane;
    public static String time = " at "
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    private static final String dir = "C:\\Users\\Public\\Documents\\InvManagement\\";

    private static void createAndShowGUI(TabSystem jframe) {
        cp1 = new CustomPanel(new String[]{"Index","Icon", "Item", "Quantity", "Notified", "Description", "Notes"});
        cp2 = new CustomPanel(new String[]{"Index","Icon", "Item", "Quantity", "Notified", "Description", "Notes"});
        layerPane = new LayeredPaneDemo();


        jframe.setPane(0, cp1);
        jframe.setPane(1, cp2);
        jframe.setPane(2, layerPane);
        jframe.setPane(3, debugScr);

        frame = jframe;

        rightPane = new JTextArea();
        rightPane.setFont(new Font("Serif", Font.BOLD, 16));
        rightPane.setLineWrap(true);
        rightPane.setText(getEvents());
        rightPane.setOpaque(true);
        rightPane.setEditable(false);
        rightPane.setWrapStyleWord(true);
        DefaultCaret caretR = (DefaultCaret)rightPane.getCaret();
        caretR.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrPaneR = (new JScrollPane(rightPane));
        scrPaneR.setPreferredSize(new Dimension(300,175));
        frame.getContentPane().add((scrPaneR), BorderLayout.EAST);

        leftPane = new JTextArea();
        leftPane.setFont(new Font("Serif", Font.BOLD, 16));
        leftPane.setText("Items need to be restocked: \n");
        leftPane.setOpaque(true);
        leftPane.setEditable(false);
        leftPane.setWrapStyleWord(true);
        DefaultCaret caretL = (DefaultCaret)leftPane.getCaret();
        caretL.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrPaneL = (new JScrollPane(leftPane));
        scrPaneL.setPreferredSize(new Dimension(300,175));
        frame.getContentPane().add(scrPaneL, BorderLayout.WEST);

        printLowStocks(cp1.table);
        printLowStocks(cp2.table);

        frame.setJMenuBar(new MenuSystem());
        frame.setResizable(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);
        frame.setVisible(true);
    }

    private static String checkDir(String filename){
        File directory = new File(dir);
        if (! directory.exists()){
            directory.mkdirs();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        return dir.concat(filename);
    }

    private static String getEvents(){
        String events = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(checkDir("history.txt")));
            events = (String) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally{
            if(ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                }
        }
        return events;
    }

    public static void printLowStocks(CustomTable table){
        if (table.getRowCount()>0) {
            for (int i = 0; i < table.getRowCount(); i++) {
                String name = (String) table.getValueAt(i, table.getColumn("Item").getModelIndex());
                if ((double) table.getValueAt(i, table.getColumn("Quantity").getModelIndex()) <= table.getLowBound(i)
                        && !(boolean) table.getValueAt(i, table.getColumn("Notified").getModelIndex())
                        && !lowStock.contains(name)) {
                    lowStock.add(name);
                    lowPoint.add(new Point(i, table.getColumn("Notified").getModelIndex()));
                }
            }
            leftPane.setText("Items need to be restocked: \n" + getLowStocks());
        }
    }

    public static void removeStocks(ArrayList<String> ars){
        lowStock.removeAll(ars);
        leftPane.setText("Items need to be restocked: \n"+getLowStocks());
    }

    public static void addEvents(String s) {
        rightPane.append("-" +s +"\n" );
    }

    public static String getLowStocks() {
        String ret = "";
        for (String s : lowStock) {
            ret += s.split("\\(|\\)")[0];
            ret += "\n";
        }
        return ret;
    }

    public static void checkLowStocks(JTable table) {
        for (Point p : lowPoint) {
            table.setValueAt(true, p.x, p.y);
        }
    }

    public static void createDumpFile(String error){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream( checkDir("fError.txt")));
            oos.writeObject(error);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally{
            if(oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                }
        }
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI(new TabSystem("Inventory Table"));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "This programm has encountered an error. Closing program",
                            "Exception found", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                    createDumpFile(e.toString());
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                cp1.storeTableModel();
                cp2.storeTableModel();
            }
        });
    }
}
