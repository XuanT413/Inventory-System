package components;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class CustomTable extends JTable implements ActionListener {
    private final Font fontText = new Font("SansSerif", Font.PLAIN, 20);
    private final ArrayList<String> delData = new ArrayList<>();
    private Object cellData;

    public CustomTable(CustomTableModel model){
        super(model);
        setFont(fontText);
        getTableHeader().setFont(fontText);
        setRowHeight(this.getFont().getSize()*3);
        setColumnsWidth(this, 1280, 4.13, 6.12, 28, 6, 6, 30.75, 19);
        setDefaultRenderer(String.class, new MyRenderer());
        setDefaultEditor(String.class, new MyEditor());
        setPreferredScrollableViewportSize(new Dimension(1280, 800));
        setFillsViewportHeight(true);
        addMouseListener(new PopClickListener());

//        addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                getCellEditor().stopCellEditing();
//                clearSelection();
//            }
//        });
//    }
//    public TableCellRenderer getCellRenderer(int row, int col){
//        //Wrap text in a specific cell
//        if (col == 2 || col == 5 || col == 6){
//            return new DefaultTableCellRenderer();
//        } else {
//            return new MyRenderer();
//        }
//    }

//    public TableCellEditor getCellEditor(int row, int col) {
//        //Wrap text in a specific cell
//        return new MyEditor();
    }

    public void setCellData(Object obj){
        cellData = obj;
    }

    public Object getCellData(){
        return cellData;
    }

    public void setColumnsWidth(JTable table, int tablePreferredWidth,
                                       double... percentages) {
        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int)
                    (tablePreferredWidth * (percentages[i] / total)));
        }
    }

    public void actionPerformed(ActionEvent e) {
        DefaultTableModel tm = (DefaultTableModel) this.getModel();
        if ("Add".equals(e.getActionCommand())) {
            Vector obj = new Vector(5,0);
            ArrayList<Object> data = popUp();
            if (data.isEmpty()) { return; }
            obj.addElement(this.getRowCount()+1);
            obj.addElement(new ImageIcon());
            obj.addElement(data.get(0));
            obj.addElement(data.get(1));
            obj.addElement(false);
            obj.addElement(data.get(2));
            obj.addElement("");
            tm.addRow(obj);
            InvTable.addEvents("Successfully added \"" + obj.get(2) + "\" to the table" + InvTable.time);
        }
        if ("Remove".equals(e.getActionCommand())) {
            String removed = "Successfully removed \"";
            int[] chosen = this.getSelectedRows();
            for (int i=chosen.length-1; i >= 0; i--){
                delData.add((String)tm.getValueAt(chosen[i], 2));
                removed += ((String)tm.getValueAt(chosen[i], 2));
                tm.removeRow(chosen[i]);
                System.out.println("test removed: "+ removed);
            }
            for (int n=0; n < this.getRowCount(); n++){
                this.setValueAt(n+1, n, 0);
            }
            InvTable.addEvents(removed + "\" items from the table" + InvTable.time);
        }
        if ("Notify".equals(e.getActionCommand())) {
            String items = InvTable.getLowStocks();
            SmsSender send = new SmsSender();
            send.sendMessage(InvTable.getLowStocks());
            InvTable.checkLowStocks(this);
            InvTable.addEvents("Notified Hugh for these items " + items + InvTable.time);
        }
    }

    public ArrayList<Object> popUp() {
        ArrayList<Object> obj = new ArrayList<>();
        JTextField item = new JTextField(10);
        JTextField amount = new JTextField(5);
        JTextField desc = new JTextField(30);
        JTextField low = new JTextField(5);
        JButton icon = new JButton();

        JPanel myPanel = new JPanel();
        myPanel.setFont(new Font("Serif", Font.PLAIN, 66 ));
        myPanel.add(new JLabel("Item's Name: "));
        myPanel.add(item);
        myPanel.add(new JLabel("Item's Quantity: "));
        myPanel.add(amount);
        myPanel.add(new JLabel("*Lower Bound: "));
        myPanel.add(low);
        myPanel.add(new JLabel("Item's Description: "));
        myPanel.add(desc);
        myPanel.add(icon);

        JToolTip tt = new JToolTip();
        tt.setTipText("*The amount the system notifies a product that is low in stock.");
        myPanel.add(tt);
        myPanel.setPreferredSize(new Dimension(600, 120));

        int result = JOptionPane.showConfirmDialog(this, myPanel,
                "Enter the Item and its properties" , JOptionPane.OK_CANCEL_OPTION);
        while (result == JOptionPane.OK_OPTION) {
            try {
                if (checkDuplicate(item.getText())){ throw new Exception(); }
                double d = Double.parseDouble(amount.getText());
                obj.add(d);
                obj.add(desc.getText());
                int dd = Integer.parseInt(low.getText());
                obj.add(0, String.format(item.getText() + "(%d)", dd));
                break;
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(myPanel,
                        "Make sure the amount is a number, or your item doesn't already exist. \n Please Try Again.",
                        "Invalid Inputs",
                        JOptionPane.ERROR_MESSAGE);
                obj.clear();
                result = JOptionPane.showConfirmDialog(this, myPanel,
                        "Enter the Item and its properties", JOptionPane.OK_CANCEL_OPTION);
            }
        }
        return obj;
    }

    public int getLowBound(int i){
        String s = ((String) this.getValueAt(i, 2));
        int num = Integer.parseInt(s.split("\\(|\\)")[1]);
        return num;
    }

    public ArrayList<String> getDelData(){
        return delData;
    }

    public void clearDelData(){
        delData.clear();
    }

    public boolean checkDuplicate(String name) {
        for (int i=0; i<this.getRowCount(); i++) {
            if (name.equalsIgnoreCase((String) this.getValueAt(i, 2))) {
                return true;
            }
        }
        return false;
    }

    //

    private class MyRenderer extends JTextArea implements TableCellRenderer {
        private final Font fontText = new Font("SansSerif", Font.PLAIN, 20);
        public MyRenderer() {
            setFont(fontText);
            setOpaque(true);
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable table,Object value,
                                                       boolean isSelected, boolean hasFocus, int row,int column) {
            this.setText((String) value);
            return this;
        }

    }

    //

    private class MyEditor extends AbstractCellEditor implements TableCellEditor {
        JTextArea comp = new JTextArea();
        JTable table;
        int row, col;

        public MyEditor() {
            comp.setLineWrap(true);
            comp.setWrapStyleWord(true);
            comp.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    table.setRowHeight(row, (int) (comp.getPreferredSize().getHeight()));
                }
            });
            comp.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    table.setRowHeight(row, table.getColumnModel().getColumn(col).getPreferredWidth());
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            this.col = column;
            setCellSelectionEnabled(true);
            comp.setText((String) value);
            comp.setFont(table.getFont());
            return comp;
        }

        public Object getCellEditorValue() {
            return comp.getText();
        }
    }
}
