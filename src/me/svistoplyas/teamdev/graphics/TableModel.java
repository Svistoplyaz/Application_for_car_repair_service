package me.svistoplyas.teamdev.graphics;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
    private String[] columnNames;
    private Object[][] data;

    public TableModel(String[] _columnNames, Object[][] _data) {
        columnNames = _columnNames;
        data = _data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public Object[] getValueAt(int rowIndex) {
        return data[rowIndex];
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
