package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class StatsView extends AbstractView {

    private JDatePicker from, to;

    public StatsView(MainFrame _mainFrame) {
        super(_mainFrame, false);

        JLabel label = new JLabel("За период:");
        label.setBounds(570, 2, 80, 24);
        this.add(label);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1);

        from = new JDatePicker(calendar);
        from.addActionListener(e -> updateTable());
        from.setBounds(639, 4, 150, 24);
        this.add(from);

        to = new JDatePicker(new Date());
        to.addActionListener(e -> updateTable());
        to.setBounds(639, 30, 150, 24);
        this.add(to);
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
