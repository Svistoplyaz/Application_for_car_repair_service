package me.svistoplyas.teamdev.graphics.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class TableUtils {
    
    public static void centerColumn(JTable table, int column) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
    }
    
    public static void setFixedColumnWidth(JTable table, int column, int width) {
        table.getColumnModel().getColumn(column).setMaxWidth(width);
        table.getColumnModel().getColumn(column).setMinWidth(width);
    }
    
}
