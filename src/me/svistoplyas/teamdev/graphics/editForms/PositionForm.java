package me.svistoplyas.teamdev.graphics.editForms;

import net.web_kot.teamdev.db.entities.Position;
import net.web_kot.teamdev.db.entities.Service;

import javax.swing.*;
import java.util.List;

public class PositionForm extends AbstractEdit {
    private JTextField nameText;

    public PositionForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //Название
        JLabel nameLabel = new JLabel("Название должности");
        nameLabel.setBounds(10, 20, 120, 24);
        add(nameLabel);

        nameText = new JTextField();
        nameText.setBounds(170, 20, 200, 24);
        add(nameText);
        addMark(nameText);

        if (isEdit)
            fillFields();
    }

    @Override
    void setSize() {
        this.setSize(385 + 35, 150);
    }

    @Override
    public void fillFields() {
        Position object = (Position) data;
        nameText.setText(object.getName());

    }

    @Override
    public void performAdd() throws Exception {
        mainFrame.model.createPosition(nameText.getText()).save();
    }

    @Override
    public void performEdit() throws Exception {
        Position object = (Position) data;
        object.setName(nameText.getText()).save();
    }

    @Override
    public boolean otherValidation() {
        String name = nameText.getText().trim();
        if(data != null && ((Position)data).getName().equals(name))
            return true;

        try {
            List<Position> positions = mainFrame.model.getPositions();
            for (Position position : positions)
                if (position.getName().equals(name)) {
                    JOptionPane.showMessageDialog(this, "Запись с таким названием уже существует!");
                    return false;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
