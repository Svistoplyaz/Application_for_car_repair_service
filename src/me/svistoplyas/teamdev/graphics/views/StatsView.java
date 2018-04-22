package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.PeriodSelector;
import me.svistoplyas.teamdev.graphics.utils.TableUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class StatsView extends AbstractView {

    private PeriodSelector period;

    public StatsView(MainFrame _mainFrame) {
        super(_mainFrame, false);
        
        TableUtils.setFixedColumnWidth(table, 1, 120);
        TableUtils.centerColumn(table, 1);
        
        period = new PeriodSelector();
        period.addActionListener(e -> updateTable());
        period.setLocation(568, 0);
        this.add(period);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Услуга", "Количество"};
    }

    @Override
    Object[][] getData() {
        if(period == null) return new Object[0][];
        try {
            return mainFrame.model.getServicesStat(period.getStart(), period.getFinish());
        } catch(Exception e) {
            e.printStackTrace();
            return new Object[0][];
        }
    }

    @Override
    Object getObject(int row) {
        return null;
    }

    @Override
    boolean canAdd() {
        return false;
    }

    @Override
    boolean canEdit() {
        return false;
    }

    @Override
    boolean canDelete() {
        return false;
    }

    @Override
    void performDelete(int row) {

    }

    @Override
    public String toString() {
        return "Статистика";
    }
}
