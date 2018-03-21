package me.svistoplyas.teamdev.graphics.editForms;

import me.svistoplyas.teamdev.graphics.TableModel;
import net.web_kot.teamdev.db.Model;
import net.web_kot.teamdev.db.entities.Client;
import net.web_kot.teamdev.db.entities.Mark;
import net.web_kot.teamdev.db.entities.Order;
import net.web_kot.teamdev.db.entities.VehicleModel;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class OrderForm extends AbstractEdit {
    JComboBox<Client> clientCombo;
    JComboBox<String> workerCombo;
    JTextField numberText;
    JComboBox<Mark> markCombo;
    JComboBox<VehicleModel> modelCombo;
    JButton back;
    JButton forward;
    JLabel curLabel;
    JSpinner spinnerIn;
    JDatePicker datePickerIn;
    JSpinner spinnerOut;
    JDatePicker datePickerOut;
    boolean isEdit;

    int firstRow = 10, secondRow = 170;

    public OrderForm(JFrame frame, boolean _isEdit, Object data) {
        super(frame, _isEdit, data);
        isEdit = _isEdit;

        //Клиент
        JLabel clientLabel = new JLabel("Клиент");
        clientLabel.setBounds(firstRow, 20, 120, 24);
        add(clientLabel);

        clientCombo = new JComboBox<>();
        clientCombo.setBounds(secondRow, 20, 200, 24);
        add(clientCombo);
        addMark(clientCombo);

        int previous = clientLabel.getY();

        //Ответственный
        JLabel workerLabel = new JLabel("Ответственный");
        workerLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(workerLabel);

        workerCombo = new JComboBox<>();
        workerCombo.setBounds(secondRow, previous + 30, 200, 24);
        add(workerCombo);
        addMark(workerCombo);

        previous += 30;

        //Рег. номер
        JLabel numberLabel = new JLabel("Телефон клиента");
        numberLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(numberLabel);

        numberText = new JTextField();
        numberText.setBounds(secondRow, previous + 30, 200, 24);
        add(numberText);
        addMark(numberText, "Phone");

        previous += 30;

        //Марка
        JLabel markLabel = new JLabel("Марка");
        markLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(markLabel);

        markCombo = new JComboBox<>();
        markCombo.setBounds(secondRow, previous + 30, 200, 24);
        add(markCombo);
        addMark(markCombo);

        previous += 30;

        //Модель
        JLabel modelLabel = new JLabel("Модель");
        modelLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(modelLabel);

        modelCombo = new JComboBox<>();
        modelCombo.setBounds(secondRow, previous + 30, 200, 24);
        add(modelCombo);
        addMark(modelCombo);

        previous += 30;

        //Статус
        JLabel statusLabel = new JLabel("Статус:");
        statusLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(statusLabel);

        curLabel = new JLabel("Предварительный заказ");
        curLabel.setBounds(secondRow, previous + 30, 200, 24);
        add(curLabel);

        previous += 30;

        back = new JButton("Back");
        back.setBounds(secondRow, previous + 30, 95, 24);
        add(back);

        forward = new JButton("Forward");
        forward.setBounds(275, previous + 30, 95, 24);
        add(forward);

        previous += 30;

        //Таблица с историей статусов
        JTable table = new JTable(new TableModel(new String[]{"Статус", "Дата"}, new Object[0][]));
//        table.setBounds(firstRow, previous + 30, 320, 134);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(firstRow, previous + 30, 360, 134);
        add(scrollPane);

        previous += 140;

        //Дата и время приёма
        JLabel inDateLabel = new JLabel("Дата и время приёма");
        inDateLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(inDateLabel);

        Date date = new Date();
        SpinnerDateModel smIn = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerIn = new javax.swing.JSpinner(smIn);
        JSpinner.DateEditor deIn = new JSpinner.DateEditor(spinnerIn, "HH:mm:ss");
        spinnerIn.setEditor(deIn);
        spinnerIn.setBounds(secondRow, previous + 30, 200, 24);
        add(spinnerIn);

        previous += 30;

        datePickerIn = new JDatePicker(date);
        datePickerIn.setBounds(secondRow, previous + 30, 200, 24);
        add(datePickerIn);

        previous += 30;

        //Дата и время выдачи
        JLabel outDateLabel = new JLabel("Дата и время приёма");
        outDateLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(outDateLabel);

        SpinnerDateModel smOut = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerOut = new javax.swing.JSpinner(smOut);
        JSpinner.DateEditor deOut = new JSpinner.DateEditor(spinnerOut, "HH:mm:ss");
        spinnerOut.setEditor(deOut);
        spinnerOut.setBounds(secondRow, previous + 30, 200, 24);
        add(spinnerOut);

        previous += 30;

        datePickerOut = new JDatePicker(date);
        datePickerOut.setBounds(secondRow, previous + 30, 200, 24);
        add(datePickerOut);

        fillFields(data);
    }

    @Override
    void setSize() {
        this.setSize(600, 600);
    }

    @Override
    public void fillFields(Object data) {
        Order order = (Order) data;

        try {
            for (Client client : mainFrame.model.getClients())
                clientCombo.addItem(client);
            clientCombo.setSelectedItem(order.getClient());

            for (Mark mark : mainFrame.model.getMarks())
                markCombo.addItem(mark);
            markCombo.setSelectedItem(order.getVehicleModel().getMark());

            numberText.setText(order.getRegistrationNumber());

            for (VehicleModel model : mainFrame.model.getVehiclesModels())
                modelCombo.addItem(model);
            modelCombo.setSelectedItem(order.getVehicleModel());

            Date date = order.getStartDate();
            spinnerIn.getModel().setValue(date);
            datePickerIn.getModel().setDate(date.getYear(), date.getMonth(), date.getDay());

            date = order.getFinishDate();
            if (date != null) {
                spinnerOut.getModel().setValue(date);
                datePickerOut.getModel().setDate(date.getYear(), date.getMonth(), date.getDay());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeMark() {
        try {
            for (VehicleModel model : ((Mark) markCombo.getSelectedItem()).getVehiclesModels())
                modelCombo.addItem(model);
            modelCombo.setSelectedItem(0);
        } catch (Exception e) {

        }
    }

    @Override
    public void performAdd() {

    }

    @Override
    public void performEdit() {
        Date date;
        date = ((SpinnerDateModel) spinnerIn.getModel()).getDate();
        DateModel model = datePickerIn.getModel();
        date.setDate(model.getDay());
        date.setMonth(model.getMonth());
        date.setYear(model.getYear());
        System.out.println(date.toString());

    }

}
