package me.svistoplyas.graphics.editForms;

import javax.swing.*;

public class ServiceForm extends AbstractEdit {
    public ServiceForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //Название
        JLabel nameLabel = new JLabel("Название услуги");
        nameLabel.setBounds(10,20, 120,24);
        add(nameLabel);

        JTextField nameText = new JTextField();
        nameText.setBounds(170,20, 200,24);
        add(nameText);
    }

    @Override
    void setSize() {
        this.setSize(385,150);
    }
}
