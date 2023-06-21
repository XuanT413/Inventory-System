//package components;
//
//import javax.swing.*;
//import javax.swing.event.TableModelEvent;
//import javax.swing.event.TableModelListener;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumn;
//import javax.swing.table.TableRowSorter;
//import java.awt.*;
//import java.awt.dnd.DropTarget;
//import java.awt.dnd.DropTargetDropEvent;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Vector;
//
//public class TableInit extends JPanel implements TableModelListener, ActionListener {
//    private final Font fontText = new Font("SansSerif", Font.PLAIN, 20);
//    private final JTable table;
//    private final JFrame frame;
//    private final ArrayList<String> delData = new ArrayList<>();
//    private String time = " at "
//            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
//    private Object cellData;
//
//    public TableInit(MyTableModel model, JFrame aFrame) {
//        super();
//
//        frame = aFrame;
//
//        table = new JTable((model));
//        table.setFont(fontText);
//        table.getTableHeader().setFont(fontText);
//        table.getModel().addTableModelListener(this);
//        table.setRowHeight(41);
//        setColumnsWidth(table, 1280, 4.13, 9.87, 25, 6, 6, 39, 10);
//        table.setPreferredScrollableViewportSize(new Dimension(1280, 800));
//        table.setCellSelectionEnabled(true);
//        table.setFillsViewportHeight(true);
//        table.setAutoCreateRowSorter(true);
//        table.addMouseListener(new PopClickListener());
//
//        //Create the scroll pane and add the table to it.
//        JScrollPane scroll = new JScrollPane(table);
//        //Add the scroll pane to this panel.
//
////        table.setDropTarget(new DropTarget(){
////            @Override
////            public synchronized void drop(DropTargetDropEvent dtde) {
////                Point point = dtde.getLocation();
////                int column = table.columnAtPoint(point);
////                int row = table.rowAtPoint(point);
////                // handle drop inside current table
////                super.drop(dtde);
////            }
////        });
////        scroll.setDropTarget(new DropTarget(){
////            @Override
////            public synchronized void drop(DropTargetDropEvent dtde) {
////                // handle drop outside current table (e.g. add row)
////                super.drop(dtde);
////            }
////        });
//        add(scroll);
//    }
//
//    public JTable getTable(){
//        return this.table;
//    }
//
//    public void actionPerformed(ActionEvent e) {
//        DefaultTableModel tm = (DefaultTableModel) table.getModel();
//        if ("Add".equals(e.getActionCommand())) {
//            Vector obj = new Vector(5,0);
//            ArrayList<Object> data = popUp();
//            if (data.isEmpty()) { return; }
//            obj.addElement(table.getRowCount()+1);
//            obj.addElement(new ImageIcon());
//            obj.addElement(data.get(0));
//            obj.addElement(data.get(1));
//            obj.addElement(false);
//            obj.addElement(data.get(2));
//            tm.addRow(obj);
//            InvTable.addEvents("Successfully added \"" + obj.get(2) + "\" to the table" + time);
//        }
//        if ("Remove".equals(e.getActionCommand())) {
//            String removed = "Successfully removed \"";
//            int[] chosen = table.getSelectedRows();
//            for (int i=chosen.length-1; i >= 0; i--){
//                delData.add((String)tm.getValueAt(chosen[i], 2));
//                removed += ((String)tm.getValueAt(chosen[i], 2));
//                tm.removeRow(chosen[i]);
//                System.out.println(removed);
//            }
//            for (int n=0; n < table.getRowCount(); n++){
//                table.setValueAt(n+1, n, 0);
//            }
//            InvTable.addEvents(removed + "\" items from the table" + time);
//        }
//        if ("Notify".equals(e.getActionCommand())) {
//            String items = InvTable.getLowStocks();
//            SmsSender send = new SmsSender();
//            send.sendMessage(InvTable.getLowStocks());
//            InvTable.checkLowStocks(table);
//            InvTable.addEvents("Notified Hugh for these items " + items + time);
//        }
//    }
//
//    public void tableChanged(TableModelEvent e) {
//        DefaultTableModel model = (DefaultTableModel) e.getSource();
//        int row = e.getFirstRow();
//        int column = e.getColumn();
//        if (row == table.getEditingRow() && column == table.getEditingColumn()) {
//            String colName = model.getColumnName(column);
//            String item = (String) model.getValueAt(row, 2);
//            Object data = model.getValueAt(row, column);
//            if (e.getType() != TableModelEvent.INSERT && e.getType() != TableModelEvent.DELETE) {
//                InvTable.printLowStocks((MyTableModel) model, table, this);
//                InvTable.addEvents(String.format("The %s's %s was updated to \"%s\"",
//                        item, colName, data.toString()) + time);
//            }
//        }
//        switch(e.getType()) {
//            case (TableModelEvent.UPDATE):
//            case (TableModelEvent.INSERT):
//                InvTable.printLowStocks((MyTableModel) model, table, this);
//                break;
//            case (TableModelEvent.DELETE):
//                InvTable.removeStocks(delData);
//                delData.clear();
//                break;
//        }
//    }
//
//    public static void setColumnsWidth(JTable table, int tablePreferredWidth,
//                                       double... percentages) {
//        double total = 0;
//        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
//            total += percentages[i];
//        }
//
//        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
//            TableColumn column = table.getColumnModel().getColumn(i);
//            column.setPreferredWidth((int)
//                    (tablePreferredWidth * (percentages[i] / total)));
//        }
//    }
//
//
//    public ArrayList<Object> popUp() {
//        ArrayList<Object> obj = new ArrayList<>();
//        JTextField item = new JTextField(10);
//        JTextField amount = new JTextField(5);
//        JTextField desc = new JTextField(30);
//        JTextField low = new JTextField(5);
//        JButton icon = new JButton();
//
//
//        JPanel myPanel = new JPanel();
//        myPanel.setFont(new Font("Serif", Font.PLAIN, 66 ));
//        myPanel.add(new JLabel("Item's Name: "));
//        myPanel.add(item);
//        myPanel.add(new JLabel("Item's Quantity: "));
//        myPanel.add(amount);
//        myPanel.add(new JLabel("*Lower Bound: "));
//        myPanel.add(low);
//        myPanel.add(new JLabel("Item's Description: "));
//        myPanel.add(desc);
//        myPanel.add(icon);
//
//        JToolTip tt = new JToolTip();
//        tt.setTipText("*The amount the system notifies a product that is low in stock.");
//        myPanel.add(tt);
//        myPanel.setPreferredSize(new Dimension(600, 120));
//
//        int result = JOptionPane.showConfirmDialog(frame, myPanel,
//                "Enter the Item and its properties" , JOptionPane.OK_CANCEL_OPTION);
//        while (result == JOptionPane.OK_OPTION) {
//            try {
//                if (checkDuplicate(item.getText())){ throw new Exception(); }
//                double d = Double.parseDouble(amount.getText());
//                obj.add(d);
//                obj.add(desc.getText());
//                int dd = Integer.parseInt(low.getText());
//                obj.add(0, String.format(item.getText() + "(%d)", dd));
//                break;
//            }
//            catch (Exception e){
//                JOptionPane.showMessageDialog(myPanel,
//                        "Make sure the amount is a number, or your item doesn't already exist. \n Please Try Again.",
//                        "Invalid Inputs",
//                        JOptionPane.ERROR_MESSAGE);
//                obj.clear();
//                result = JOptionPane.showConfirmDialog(frame, myPanel,
//                        "Enter the Item and its properties", JOptionPane.OK_CANCEL_OPTION);
//            }
//        }
//        return obj;
//    }
//
//    public int getLowBound(int i){
//        String s = ((String) table.getValueAt(i, 2));
//        int num = Integer.parseInt(s.split("\\(|\\)")[1]);
//        return num;
//    }
//
//    public boolean checkDuplicate(String name) {
//        for (int i=0; i<table.getRowCount(); i++) {
//            if (name.equalsIgnoreCase((String) table.getValueAt(i, 2))) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
//
