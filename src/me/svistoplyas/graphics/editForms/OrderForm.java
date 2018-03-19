package me.svistoplyas.graphics.editForms;

import me.svistoplyas.graphics.TableModel;

import javax.swing.*;

public class OrderForm extends AbstractEdit {
    public OrderForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //Клиент
        JLabel clientLabel = new JLabel("Клиент");
        clientLabel.setBounds(10,20, 120,24);
        add(clientLabel);

        JComboBox<String> clientCombo = new JComboBox<>();
        clientCombo.setBounds(170,20, 200,24);
        add(clientCombo);

        //Ответственный
        JLabel workerLabel = new JLabel("Ответственный");
        workerLabel.setBounds(10,50, 120,24);
        add(workerLabel);

        JComboBox<String> workerCombo = new JComboBox<>();
        workerCombo.setBounds(170,50, 200,24);
        add(workerCombo);

        //Рег. номер
        JLabel numberLabel = new JLabel("Телефон клиента");
        numberLabel.setBounds(10,80, 120,24);
        add(numberLabel);

        JTextField numberText = new JTextField();
        numberText.setBounds(170,80, 200,24);
        add(numberText);

        //Марка
        JLabel markLabel = new JLabel("Марка");
        markLabel.setBounds(10,110, 120,24);
        add(markLabel);

        JComboBox<String> markCombo = new JComboBox<>();
        markCombo.setBounds(170,110, 200,24);
        add(markCombo);

        //Модель
        JLabel modelLabel = new JLabel("Модель");
        modelLabel.setBounds(10,140, 120,24);
        add(modelLabel);

        JComboBox<String> modelCombo = new JComboBox<>();
        modelCombo.setBounds(170,140, 200,24);
        add(modelCombo);

        //Статус
        JLabel statusLabel = new JLabel("Статус:");
        statusLabel.setBounds(10,170, 120,24);
        add(statusLabel);

        JLabel curLabel = new JLabel("Предварительный заказ");
        curLabel.setBounds(170,170, 200,24);
        add(curLabel);

        JButton back = new JButton("Back");
        back.setBounds(10, 200, 150, 24);
        add(back);

        JButton forward = new JButton("Forward");
        forward.setBounds(170, 200, 160, 24);
        add(forward);

        //Таблица с историей статусов
        JTable table = new JTable(new TableModel(new String[]{"Статус", "Дата"}, null));
        table.setBounds(10, 230, 320, 200);

    }

    @Override
    void setSize() {
        this.setSize(600,700);
    }
}
