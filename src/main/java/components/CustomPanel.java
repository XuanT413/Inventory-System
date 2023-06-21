package components;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;

public class CustomPanel extends JPanel {
    public CustomTable table;
    public CustomTableModel mtm;
    private static int modelCount = 0;
    public int modelIndex;
    private final String dir = "C:\\Users\\Public\\Documents\\InvManagement\\";

    public CustomPanel(String[] names){
        modelCount++;
        modelIndex = modelCount-1;
        mtm = getModel(names);

        table = new CustomTable(mtm);
        table.getModel().addTableModelListener(new TableModelListener() {
//            @Override
            public void tableChanged(TableModelEvent e) {
                DefaultTableModel model = (DefaultTableModel) e.getSource();
                int row = e.getFirstRow();
                int column = e.getColumn();
                System.out.println("table row count: " + table.getRowCount());

                if (row == table.getEditingRow() && column == table.getEditingColumn()) {
                    Object data = model.getValueAt(row, column);
                    String colName = model.getColumnName(column);
                    String item = (String) model.getValueAt(row, 2);
                    if (e.getType() != TableModelEvent.INSERT && e.getType() != TableModelEvent.DELETE
                        && !data.equals(table.getCellData())) {
                        InvTable.printLowStocks(table);
                        InvTable.addEvents(String.format("The %s's %s was updated to \"%s\"",
                                item, colName, data) + InvTable.time);
                    }
                }
                switch(e.getType()) {
                    case (TableModelEvent.UPDATE):
                    case (TableModelEvent.INSERT):
                        InvTable.printLowStocks(table);
                        break;
                    case (TableModelEvent.DELETE):
                        InvTable.removeStocks(table.getDelData());
                        table.clearDelData();
                        break;
                }
            }
        });


        setLayout(new BorderLayout());

        ButtonDemo buttons = new ButtonDemo(table);
        buttons.setOpaque(true); //content panes must be opaque
        add(buttons, BorderLayout.SOUTH);

        TableFilterDemo tfd = new TableFilterDemo(table, mtm);
        tfd.setOpaque(true); //content panes must be opaque
        add(tfd, BorderLayout.NORTH);

    }

    private CustomTableModel getModel(String[] names) {
        CustomTableModel model = new CustomTableModel(names);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream( checkDir("data" +modelIndex+ ".txt")));
            Vector data = (Vector) ois.readObject();
            ois.close();
            ois = new ObjectInputStream(new FileInputStream( checkDir("colName" +modelIndex+ ".txt")));
            Vector<String> cols = (Vector<String>) ois.readObject();
            model.setDataVector(data, cols);
        } catch (Exception e) {
            e.printStackTrace();
        }  finally{
            if(ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                }
        }
        return model;
    }

    private String checkDir(String filename){
        File directory = new File(dir);
        if (! directory.exists()){
            directory.mkdirs();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        return dir.concat(filename);
    }

    public void storeTableModel() {
        ObjectOutputStream oos = null;
        CustomTableModel model = mtm;
        try {
            oos = new ObjectOutputStream(new FileOutputStream( checkDir("data" +modelIndex+ ".txt")));
            oos.writeObject(model.getDataVector());
            oos.close();
            oos = new ObjectOutputStream(new FileOutputStream( checkDir("colName" +modelIndex+ ".txt")));
            oos.writeObject(model.getColNames());
            oos.close();
            oos = new ObjectOutputStream(new FileOutputStream( checkDir("history.txt")));
            oos.writeObject(InvTable.rightPane.getText());
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

}
