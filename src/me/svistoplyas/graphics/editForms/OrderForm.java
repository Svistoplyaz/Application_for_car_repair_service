package me.svistoplyas.graphics.editForms;

import javafx.util.Pair;
import me.svistoplyas.graphics.TableModel;
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

    public OrderForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //Клиент
        JLabel clientLabel = new JLabel("Клиент");
        clientLabel.setBounds(10, 20, 120, 24);
        add(clientLabel);

        JComboBox<String> clientCombo = new JComboBox<>();
        clientCombo.setBounds(170, 20, 200, 24);
        add(clientCombo);
        addMark(clientCombo,380, 20);

        //Ответственный
        JLabel workerLabel = new JLabel("Ответственный");
        workerLabel.setBounds(10, 50, 120, 24);
        add(workerLabel);

        JComboBox<String> workerCombo = new JComboBox<>();
        workerCombo.setBounds(170, 50, 200, 24);
        add(workerCombo);
        addMark(workerCombo,380, 50);

        //Рег. номер
        JLabel numberLabel = new JLabel("Телефон клиента");
        numberLabel.setBounds(10, 80, 120, 24);
        add(numberLabel);

        JTextField numberText = new JTextField();
        numberText.setBounds(170, 80, 200, 24);
        add(numberText);
        addMark(numberText,380, 80);

        //Марка
        JLabel markLabel = new JLabel("Марка");
        markLabel.setBounds(10, 110, 120, 24);
        add(markLabel);

        JComboBox<String> markCombo = new JComboBox<>();
        markCombo.setBounds(170, 110, 200, 24);
        add(markCombo);
        addMark(markCombo,380, 110);

        //Модель
        JLabel modelLabel = new JLabel("Модель");
        modelLabel.setBounds(10, 140, 120, 24);
        add(modelLabel);

        JComboBox<String> modelCombo = new JComboBox<>();
        modelCombo.setBounds(170, 140, 200, 24);
        add(modelCombo);
        addMark(modelCombo,380, 140);

        //Статус
        JLabel statusLabel = new JLabel("Статус:");
        statusLabel.setBounds(10, 170, 120, 24);
        add(statusLabel);

        JLabel curLabel = new JLabel("Предварительный заказ");
        curLabel.setBounds(170, 170, 200, 24);
        add(curLabel);

        JButton back = new JButton("Back");
        back.setBounds(10, 200, 170, 24);
        add(back);

        JButton forward = new JButton("Forward");
        forward.setBounds(200, 200, 170, 24);
        add(forward);

        //Таблица с историей статусов
        JTable table = new JTable(new TableModel(new String[]{"Статус", "Дата"}, new Object[0][]));
        table.setBounds(10, 230, 320, 200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 230, 360, 200);
        add(scrollPane);

        //Дата и время приёма
        JLabel inDateLabel = new JLabel("Дата и время приёма");
        inDateLabel.setBounds(10, 440, 120, 24);
        add(inDateLabel);

        Date date = new Date();
        SpinnerDateModel smIn = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerIn = new javax.swing.JSpinner(smIn);
        JSpinner.DateEditor deIn = new JSpinner.DateEditor(spinnerIn, "HH:mm:ss");
        spinnerIn.setEditor(deIn);
        spinnerIn.setBounds(170, 440, 200, 24);
        add(spinnerIn);

        datePickerIn = new JDatePicker(date);
        datePickerIn.setBounds(10, 470, 200, 24);
        add(datePickerIn);

        //Дата и время выдачи
        JLabel outDateLabel = new JLabel("Дата и время приёма");
        outDateLabel.setBounds(10, 500, 120, 24);
        add(outDateLabel);

        SpinnerDateModel smOut = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerOut = new javax.swing.JSpinner(smOut);
        JSpinner.DateEditor deOut = new JSpinner.DateEditor(spinnerOut, "HH:mm:ss");
        spinnerOut.setEditor(deOut);
        spinnerOut.setBounds(170, 500, 200, 24);
        add(spinnerOut);

        datePickerOut = new JDatePicker(date);
        datePickerOut.setBounds(10, 530, 200, 24);
        add(datePickerOut);

    }

    @Override
    void setSize() {
        this.setSize(600, 700);
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
