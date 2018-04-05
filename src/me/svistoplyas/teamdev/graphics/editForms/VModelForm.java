package me.svistoplyas.teamdev.graphics.editForms;

import net.web_kot.teamdev.db.entities.Mark;
import net.web_kot.teamdev.db.entities.VehicleModel;

import javax.swing.*;
import java.util.Date;

public class VModelForm extends AbstractEdit {
    private JTextField nameText;
    private JComboBox<Integer> yearCombo;
    private JComboBox<Mark> markCombo;

    private boolean isEdit;

    public VModelForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);
        this.isEdit = isEdit;

        //Название модели
        JLabel fioLabel = new JLabel("Название модели");
        fioLabel.setBounds(10, 20, 120, 24);
        add(fioLabel);

        nameText = new JTextField();
        nameText.setBounds(170, 20, 200, 24);
        add(nameText);
        addMark(nameText);

        //Год выпуска
        JLabel phoneLabel = new JLabel("Год выпуска");
        phoneLabel.setBounds(10, 20 + 30, 120, 24);
        add(phoneLabel);

        yearCombo = new JComboBox<>();
        yearCombo.setBounds(170, 20 + 30, 200, 24);
        add(yearCombo);
        addMark(yearCombo);

        //Марка
        JLabel markLabel = new JLabel("Марка");
        markLabel.setBounds(10, 50 + 30, 120, 24);
        add(markLabel);

        markCombo = new JComboBox<>();
        markCombo.setBounds(170, 50 + 30, 200, 24);
        add(markCombo);
        addMark(markCombo);
        if (isEdit)
            markCombo.setEnabled(false);

        fillFields();
    }

    @Override
    public void setSize() {
        this.setSize(385 + 35, 250);
    }

    @Override
    public void fillFields() {
        VehicleModel vModel = (VehicleModel) data;

        try {
            yearCombo.removeAllItems();
            int currentYear = new Date().getYear() + 1900;
            for (int i = currentYear; i > 1917; i--)
                yearCombo.addItem(i);
            if (isEdit)
                yearCombo.setSelectedItem(vModel.getYear());
            else {
                yearCombo.setSelectedIndex(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            markCombo.removeAllItems();
            for (Mark mark : mainFrame.model.getMarks())
                markCombo.addItem(mark);
            if (isEdit)
                markCombo.setSelectedItem(vModel.getMark());
            else {
                markCombo.setSelectedIndex(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isEdit) {
            nameText.setText(vModel.getName());
        }
    }

    @Override
    public void performAdd() throws Exception {
        mainFrame.model.createVehicleModel((Mark) markCombo.getSelectedItem(), nameText.getText(), (Integer) yearCombo.getSelectedItem()).save();
    }

    @Override
    public void performEdit() throws Exception {
        VehicleModel vModel = (VehicleModel) data;
        vModel.setName(nameText.getText()).setYear((Integer) yearCombo.getSelectedItem()).save();
    }

    @Override
    public boolean otherValidation() {
        return true;
    }
}
