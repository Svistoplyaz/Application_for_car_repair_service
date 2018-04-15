package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.TableModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class ProfitView extends AbstractView {

    private JDatePicker from, to;
    private JTable incomeTable;
    private JTable spendingTable;
    private JLabel incomeLabel;
    private JLabel spendingLabel;
    private JLabel profitLabel;

    public ProfitView(MainFrame _mainFrame) {
        super(_mainFrame, true);

        JLabel label = new JLabel("За период:");
        label.setBounds(570, 2, 80, 24);
        this.add(label);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1);

        from = new JDatePicker(calendar);
        from.addActionListener(e -> updateFrame());
        from.setBounds(639, 4, 150, 24);
        this.add(from);

        to = new JDatePicker(new Date());
        to.addActionListener(e -> updateFrame());
        to.setBounds(639, 30, 150, 24);
        this.add(to);

        //Таблица доходов
        incomeTable = new JTable(new TableModel(getIncomeColumnNames(), getIncomeData()));
        incomeTable.setFillsViewportHeight(true);
        incomeTable.getTableHeader().setReorderingAllowed(false);
        incomeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incomeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane incomeScrollPane = new JScrollPane(incomeTable);
        incomeScrollPane.setBounds(10, 60, 718 + 60, 200);
        add(incomeScrollPane);

        //Таблица расходов
        spendingTable = new JTable(new TableModel(getSpendingColumnNames(), getSpendingData()));
        spendingTable.setFillsViewportHeight(true);
        spendingTable.getTableHeader().setReorderingAllowed(false);
        spendingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        spendingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane spendingScrollPane = new JScrollPane(spendingTable);
        spendingScrollPane.setBounds(10, incomeScrollPane.getY() + incomeScrollPane.getHeight() + 15,
                718 + 60, 200);
        add(spendingScrollPane);

        int labelWidth = 240;
        incomeLabel = new JLabel("Доход составляет: ");
        incomeLabel.setBounds(10, spendingScrollPane.getY() + spendingScrollPane.getHeight() + 5, labelWidth,
                30);
        add(incomeLabel);

        spendingLabel = new JLabel("Расход составляет: ");
        spendingLabel.setBounds(incomeLabel.getX() + incomeLabel.getWidth() + 30,
                spendingScrollPane.getY() + spendingScrollPane.getHeight() + 5, labelWidth, 30);
        add(spendingLabel);

        profitLabel = new JLabel("Прибыль составляет: ");
        profitLabel.setBounds(spendingLabel.getX() + spendingLabel.getWidth() + 30,
                spendingScrollPane.getY() + spendingScrollPane.getHeight() + 5, labelWidth, 30);
        add(profitLabel);
    }

    @Override
    String[] getColumnNames() {
        return new String[0];
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
        return "Доходы и расходы";
    }

    private String[] getIncomeColumnNames() {
        return new String[]{"Заказ", "Доход"};
    }

    private Object[][] getIncomeData() {
        return new Object[0][];
    }

    private String[] getSpendingColumnNames() {
        return new String[]{"Зап. часть", "Количество", "Расход"};
    }

    private Object[][] getSpendingData() {
        return new Object[0][];
    }

    private void updateFrame(){
        ((TableModel) incomeTable.getModel()).setData(getIncomeData());
        ((TableModel) spendingTable.getModel()).setData(getSpendingData());

        incomeLabel.setText("Доход составляет: ");
        spendingLabel.setText("Расход составляет: ");
        profitLabel.setText("Прибыль составляет: ");
    }
}
