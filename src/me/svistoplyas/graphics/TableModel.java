package me.svistoplyas.graphics;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
    private String[] columnNames = {"1", "2"};
    private Object[][] data = {{"Leonid", "Grisha"}, {"Barmaley", "Peter"}};

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

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
