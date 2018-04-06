package me.svistoplyas.teamdev.graphics.editForms;

import javafx.geometry.Pos;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.Position;
import net.web_kot.teamdev.db.entities.Staff;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.util.Date;
import java.util.List;

//@SuppressWarnings("All")
@SuppressWarnings("deprecation")
public class StaffForm extends AbstractEdit {
    private JTextField fioText;
    private JTextField phoneText;
    private JDatePicker birthday;
    private JComboBox<Position> positionCombo;

    private boolean isEdit;

    public StaffForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);
        this.isEdit = isEdit;

        //ФИО
        JLabel fioLabel = new JLabel("ФИО работника");
        fioLabel.setBounds(10, 20, 120, 24);
        add(fioLabel);

        fioText = new JTextField();
        fioText.setBounds(170, 20, 200, 24);
        add(fioText);
        addMark(fioText);

        //Телефон
        JLabel phoneLabel = new JLabel("Телефон работника");
        phoneLabel.setBounds(10, 20 + 30, 120, 24);
        add(phoneLabel);

        phoneText = new JTextField();
        phoneText.setBounds(170, 20 + 30, 200, 24);
        add(phoneText);
        addMark(phoneText, "Phone");

        //День рождения
        JLabel brithdayLabel = new JLabel("День рождения");
        brithdayLabel.setBounds(10, 50 + 30, 120, 24);
        add(brithdayLabel);

        birthday = new JDatePicker(new Date());
        birthday.setBounds(170, 50 + 30, 200, 24);
        add(birthday);

        //Должность
        JLabel markLabel = new JLabel("Должность");
        markLabel.setBounds(10, 80 + 30, 120, 24);
        add(markLabel);

        positionCombo = new JComboBox<>();
        positionCombo.setBounds(170, 80 + 30, 200, 24);
        add(positionCombo);
        addMark(positionCombo);

        fillFields();
    }

    @Override
    public void setSize() {
        this.setSize(385 + 35, 250);
    }

    @Override
    public void fillFields() {
        Staff person = (Staff) data;

        try {
            positionCombo.removeAllItems();
            for (Position client : mainFrame.model.getPositions())
                positionCombo.addItem(client);
            if (isEdit)
                positionCombo.setSelectedItem(person.getPosition());
            else {
                positionCombo.setSelectedIndex(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isEdit) {
            fioText.setText(person.getName());

            phoneText.setText(person.getPhone());

            Date date = person.getBirthday();
            birthday.getModel().setDate(1900 + date.getYear(), date.getMonth(), date.getDate());
        }
    }

    @Override
    public void performAdd() throws Exception {
        mainFrame.model.createStaff((Position) positionCombo.getSelectedItem(), fioText.getText(), phoneText.getText(),
                Converter.getInstance().convertDataPicker(birthday)).save();
    }

    @Override
    public void performEdit() throws Exception {
        Staff person = (Staff) data;
        person.setName(fioText.getText()).setPhone(phoneText.getText())
                .setBirthday(Converter.getInstance().convertDataPicker(birthday))
                .setPosition((Position) positionCombo.getSelectedItem()).save();
    }

    @Override
    public boolean otherValidation() {
        if (!Converter.getInstance().convertDataPicker(birthday).before(new Date())) {
            JOptionPane.showMessageDialog(this, "Дата рождения позже текущей!");
            return false;
        }

        try {
            List<Staff> staff = mainFrame.model.getStaff();
            for (Staff person : staff) {
                if(data != null && person.getId() == ((Staff)data).getId()) continue;
                if (person.getName().equals(fioText.getText().trim()) &&
                        person.getPhone().equals(phoneText.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Такая запись уже существует!");
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
}
