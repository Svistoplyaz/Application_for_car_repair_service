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
        this.fireTableDataChanged();
    }

    public Object[][] getData() {
        return data;
    }

    public void addData(Object[] newData) {
        int len = data.length;
        Object[][] updatedData = new Object[len + 1][];
        for (int i = 0; i < len; i++) {
            updatedData[i] = data[i];
        }
        updatedData[len] = newData;
        setData(updatedData);
    }

    public void deleteData(int row) {
        int len = data.length;
        Object[][] updatedData = new Object[len - 1][];
        for (int i = 0; i < row; i++) {
            updatedData[i] = data[i];
        }

        for (int i = row + 1; i < len; i++) {
            updatedData[i - 1] = data[i];
        }
        setData(updatedData);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
