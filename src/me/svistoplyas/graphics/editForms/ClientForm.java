package me.svistoplyas.graphics.editForms;

import javax.swing.*;

public class ClientForm extends AbstractEdit {

    public ClientForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //ФИО
        JLabel fioLabel = new JLabel("ФИО клиента");
        fioLabel.setBounds(10,20, 120,24);
        add(fioLabel);

        JTextField fioText = new JTextField();
        fioText.setBounds(170,20, 200,24);
        add(fioText);

        //Телефон
        JLabel phoneLabel = new JLabel("Телефон клиента");
        phoneLabel.setBounds(10,20 + 30, 120,24);
        add(phoneLabel);

        JTextField phoneText = new JTextField();
        phoneText.setBounds(170,20 + 30, 200,24);
        add(phoneText);
    }

    @Override
    void setSize() {
        this.setSize(385,200);
    }
}
