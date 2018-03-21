package me.svistoplyas.teamdev.graphics.editForms;

import net.web_kot.teamdev.db.entities.Service;

import javax.swing.*;
import java.sql.SQLException;

public class ServiceForm extends AbstractEdit {
    private JTextField nameText;
    private JTextField priceText;

    public ServiceForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //Название
        JLabel nameLabel = new JLabel("Название услуги");
        nameLabel.setBounds(10, 20, 120, 24);
        add(nameLabel);

        nameText = new JTextField();
        nameText.setBounds(170, 20, 200, 24);
        add(nameText);
        addMark(nameText);

        //Цена
        JLabel priceLabel = new JLabel("Стоимость");
        priceLabel.setBounds(10, 50, 120, 24);
        add(priceLabel);

        priceText = new JTextField();
        priceText.setBounds(170, 50, 200, 24);
        add(priceText);
        addMark(priceText);

        if (isEdit)
            fillFields(data);
    }

    @Override
    void setSize() {
        this.setSize(385 + 35, 180);
    }

    @Override
    public void fillFields(Object data) {
        Service object = (Service) data;
        nameText.setText(object.getName());

        try {
            int price = object.getPrice();
            priceText.setText(price / 100 + "," + price % 100);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performAdd() {

    }

    @Override
    public void performEdit() {

    }
}
