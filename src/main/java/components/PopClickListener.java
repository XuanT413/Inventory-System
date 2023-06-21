package components;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class PopClickListener extends MouseAdapter {
    private int row;
    private int col;
    private CustomTable table;

    public void mousePressed(MouseEvent e) {
        storeCellData(e);
        if (e.isPopupTrigger()) {
            doPop(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        storeCellData(e);
        if (e.isPopupTrigger()) {
            doPop(e);
        }
    }

    private void clrSelection(){
//        table.setEditingColumn(null);
    }

    private void doPop(MouseEvent e) {
        if(row > -1 && col == 1) {
//            table.setEditingColumn(row);
//            table.setEditingRow(col);
            PopUpDemo menu = new PopUpDemo(e);
            menu.show(table, e.getX(), e.getY());
        }
    }

    public void storeCellData(MouseEvent e){
        table = (CustomTable) e.getComponent();
        row = table.rowAtPoint(e.getPoint());
        col = table.columnAtPoint(e.getPoint());
        TableModel model = table.getModel();
        if (row >= 0 && col >=0 ) {
            table.setCellData(model.getValueAt(row, col));
        }
        else {
            table.clearSelection();
        }
        InvTable.debugScr.setText("Editing cell: (row,col) "+table.getEditingRow() + table.getEditingColumn());
        InvTable.debugScr.append("\nSelecting cell: (row,col) "+table.getSelectedRow() + table.getSelectedColumn());
        InvTable.debugScr.append("\n(row,col): "+ row + col);
    }

    class PopUpDemo extends JPopupMenu {
        JMenuItem addImg;
        JMenuItem delImg;

        public PopUpDemo(MouseEvent me) {
            addImg = new JMenuItem("Add/Change Icon");
            addImg.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(PopUpDemo.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String imgName = file.getPath();
                    if (table.getSelectedColumn() == table.getColumn("Icon").getModelIndex()) {
                        ImageIcon ii = new ImageIcon(imgName); // load the image to a imageIcon
                        Image image = ii.getImage(); // transform it
                        Image newImg = image.getScaledInstance(table.getRowHeight(), table.getRowHeight(),
                                                        java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                        ii = new ImageIcon(newImg);  // transform it back
                        table.setValueAt(ii, table.getSelectedRow(), table.getSelectedColumn());
                    }
                }
            });
            delImg = new JMenuItem("Remove Icon");
            delImg.addActionListener(e -> {
                if (table.getSelectedColumn() == table.getColumn("Icon").getModelIndex()) {
                    table.setValueAt(new ImageIcon(), table.getSelectedRow(), table.getSelectedColumn());
                }
            });
            add(addImg);
            add(delImg);
        }
    }
}