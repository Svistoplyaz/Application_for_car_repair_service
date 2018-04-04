package me.svistoplyas.teamdev.graphics.editForms;

import net.web_kot.teamdev.db.entities.Mark;

import javax.swing.*;
import java.util.List;

public class MarkForm extends AbstractEdit{
    private JTextField nameText;

    public MarkForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);

        //Название
        JLabel nameLabel = new JLabel("Название марки");
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
        Mark object = (Mark) data;
        nameText.setText(object.getName());

    }

    @Override
    public void performAdd() throws Exception {
        mainFrame.model.createMark(nameText.getText()).save();
    }

    @Override
    public void performEdit() throws Exception {
        Mark object = (Mark) data;
        object.setName(nameText.getText()).save();
    }

    @Override
    public boolean otherValidation() {
        String name = nameText.getText().trim();
        if(data != null && ((Mark)data).getName().equals(name))
            return true;

        try {
            List<Mark> marks = mainFrame.model.getMarks();
            for (Mark mark : marks)
                if (mark.getName().equals(name)) {
                    JOptionPane.showMessageDialog(this, "Запись с таким названием уже существует!");
                    return false;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
