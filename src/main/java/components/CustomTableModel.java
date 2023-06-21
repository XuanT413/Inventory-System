package components;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class CustomTableModel extends DefaultTableModel {
    public static String[] columnNames;
    public CustomTableModel(String[] header) {
        super(header, 0);
        columnNames = header;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
            return getValueAt(0, columnIndex).getClass();
        }
        return super.getColumnClass(columnIndex);
    }

    public Vector<String> getColNames() {
        Vector<String> colNames = new Vector(columnNames.length,0);
        for(int i=0; i < columnNames.length;i++){
            colNames.add(columnNames[i]);
        }
        return colNames;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 1;
    }
}

