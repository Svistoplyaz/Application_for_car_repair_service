package me.svistoplyas.teamdev.graphics.editForms;

import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.Service;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

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
        addMark(priceText, "Price");

        if (isEdit)
            fillFields();
    }

    @Override
    public void setSize() {
        this.setSize(385 + 35, 180);
    }

    @Override
    public void fillFields() {
        Service object = (Service) data;
        nameText.setText(object.getName());

        try {
            int price = object.getPrice();
            priceText.setText(Converter.getInstance().convertPriceToStr(price));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performAdd() throws Exception {
        mainFrame.model.createService(nameText.getText()).save().setPrice(Converter.getInstance().convertStrToPrice(priceText.getText()));
    }

    @Override
    public void performEdit() throws Exception {
        Service service = (Service) data;
        service.setName(nameText.getText()).save().setPrice(Converter.getInstance().convertStrToPrice(priceText.getText()));
    }

    @Override
    public boolean otherValidation() {
        String name = nameText.getText().trim();
        if (data != null && ((Service) data).getName().equals(name))
            return true;

        try {
            List<Service> services = mainFrame.model.getServices();
            for (Service service : services)
                if (service.getName().equals(name)) {
                    JOptionPane.showMessageDialog(this, "Запись с таким именем уже существует!");
                    return false;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
