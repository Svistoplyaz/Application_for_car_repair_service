package me.svistoplyas.teamdev.graphics.editForms;

import net.web_kot.teamdev.db.entities.Client;

import javax.swing.*;

public class ClientForm extends AbstractEdit {
    private JTextField fioText;
    private JTextField phoneText;

    public ClientForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //ФИО
        JLabel fioLabel = new JLabel("ФИО клиента");
        fioLabel.setBounds(10, 20, 120, 24);
        add(fioLabel);

        fioText = new JTextField();
        fioText.setBounds(170, 20, 200, 24);
        add(fioText);
        addMark(fioText);

        //Телефон
        JLabel phoneLabel = new JLabel("Телефон клиента");
        phoneLabel.setBounds(10, 20 + 30, 120, 24);
        add(phoneLabel);

        phoneText = new JTextField();
        phoneText.setBounds(170, 20 + 30, 200, 24);
        add(phoneText);
        addMark(phoneText, "Phone");

        if (isEdit)
            fillFields();
    }

    @Override
    void setSize() {
        this.setSize(385 + 35, 200);
    }

    @Override
    public void fillFields() {
        Client client = (Client) data;
        fioText.setText(client.getName());
        phoneText.setText(client.getPhone());
    }

    @Override
    public void performAdd() throws Exception {
        mainFrame.model.createClient(fioText.getText()).setPhone(phoneText.getText()).save();
    }

    @Override
    public void performEdit() throws Exception {
        Client client = (Client) data;
        client.setName(fioText.getText()).setPhone(phoneText.getText()).save();
    }

    @Override
    public boolean otherValidation() {
        return true;
    }
}
