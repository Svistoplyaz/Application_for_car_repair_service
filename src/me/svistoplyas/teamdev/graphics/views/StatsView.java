package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.PeriodSelector;

public class StatsView extends AbstractView {

    private PeriodSelector period;

    public StatsView(MainFrame _mainFrame) {
        super(_mainFrame, false);

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
        return new Object[0][];
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
