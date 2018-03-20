package me.svistoplyas.teamdev.graphics.editForms;

import me.svistoplyas.teamdev.graphics.TableModel;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class OrderForm extends AbstractEdit {
    JSpinner spinnerIn;
    JDatePicker datePickerIn;
    JSpinner spinnerOut;
    JDatePicker datePickerOut;

    int firstrow = 10, secondrow = 170;


    public OrderForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //Клиент
        JLabel clientLabel = new JLabel("Клиент");
        clientLabel.setBounds(firstrow, 20, 120, 24);
        add(clientLabel);

        JComboBox<String> clientCombo = new JComboBox<>();
        clientCombo.setBounds(secondrow, 20, 200, 24);
        add(clientCombo);
        addMark(clientCombo);

        int previous = clientLabel.getY();

        //Ответственный
        JLabel workerLabel = new JLabel("Ответственный");
        workerLabel.setBounds(firstrow, previous + 30, 120, 24);
        add(workerLabel);

        JComboBox<String> workerCombo = new JComboBox<>();
        workerCombo.setBounds(secondrow, previous + 30, 200, 24);
        add(workerCombo);
        addMark(workerCombo);

        previous += 30;

        //Рег. номер
        JLabel numberLabel = new JLabel("Телефон клиента");
        numberLabel.setBounds(firstrow, previous + 30, 120, 24);
        add(numberLabel);

        JTextField numberText = new JTextField();
        numberText.setBounds(secondrow, previous + 30, 200, 24);
        add(numberText);
        addMark(numberText, "Phone");

        previous += 30;

        //Марка
        JLabel markLabel = new JLabel("Марка");
        markLabel.setBounds(firstrow, previous + 30, 120, 24);
        add(markLabel);

        JComboBox<String> markCombo = new JComboBox<>();
        markCombo.setBounds(secondrow, previous + 30, 200, 24);
        add(markCombo);
        addMark(markCombo);

        previous += 30;

        //Модель
        JLabel modelLabel = new JLabel("Модель");
        modelLabel.setBounds(firstrow, previous + 30, 120, 24);
        add(modelLabel);

        JComboBox<String> modelCombo = new JComboBox<>();
        modelCombo.setBounds(secondrow, previous + 30, 200, 24);
        add(modelCombo);
        addMark(modelCombo);

        previous += 30;

        //Статус
        JLabel statusLabel = new JLabel("Статус:");
        statusLabel.setBounds(firstrow, previous + 30, 120, 24);
        add(statusLabel);

        JLabel curLabel = new JLabel("Предварительный заказ");
        curLabel.setBounds(secondrow, previous + 30, 200, 24);
        add(curLabel);

        previous += 30;

        JButton back = new JButton("Back");
        back.setBounds(secondrow, previous + 30, 95, 24);
        add(back);

        JButton forward = new JButton("Forward");
        forward.setBounds(275, previous + 30, 95, 24);
        add(forward);

        previous += 30;

        //Таблица с историей статусов
        JTable table = new JTable(new TableModel(new String[]{"Статус", "Дата"}, new Object[0][]));
//        table.setBounds(firstrow, previous + 30, 320, 134);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(firstrow, previous + 30, 360, 134);
        add(scrollPane);

        previous += 140;

        //Дата и время приёма
        JLabel inDateLabel = new JLabel("Дата и время приёма");
        inDateLabel.setBounds(firstrow, previous + 30, 120, 24);
        add(inDateLabel);

        Date date = new Date();
        SpinnerDateModel smIn = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerIn = new javax.swing.JSpinner(smIn);
        JSpinner.DateEditor deIn = new JSpinner.DateEditor(spinnerIn, "HH:mm:ss");
        spinnerIn.setEditor(deIn);
        spinnerIn.setBounds(secondrow, previous + 30, 200, 24);
        add(spinnerIn);

        previous += 30;

        datePickerIn = new JDatePicker(date);
        datePickerIn.setBounds(secondrow, previous + 30, 200, 24);
        add(datePickerIn);

        previous += 30;

        //Дата и время выдачи
        JLabel outDateLabel = new JLabel("Дата и время приёма");
        outDateLabel.setBounds(firstrow, previous + 30, 120, 24);
        add(outDateLabel);

        SpinnerDateModel smOut = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerOut = new javax.swing.JSpinner(smOut);
        JSpinner.DateEditor deOut = new JSpinner.DateEditor(spinnerOut, "HH:mm:ss");
        spinnerOut.setEditor(deOut);
        spinnerOut.setBounds(secondrow, previous + 30, 200, 24);
        add(spinnerOut);

        previous += 30;

        datePickerOut = new JDatePicker(date);
        datePickerOut.setBounds(secondrow, previous + 30, 200, 24);
        add(datePickerOut);

    }

    @Override
    void setSize() {
        this.setSize(600, 600);
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
