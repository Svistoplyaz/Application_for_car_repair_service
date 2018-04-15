package me.svistoplyas.teamdev.graphics.otherFrames;

import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.SparePart;

import javax.swing.*;

public class SpareRefill extends AbstractEdit {
    private SparePart data;
    private JTextField priceBuyText;
    private JTextField quantText;
    private JComboBox<SparePart.Unit> quantCombo;

    public SpareRefill(JFrame frame, SparePart data) {
        super(frame, true, data);
        this.setTitle("Пополнение количества зап. частей");
        this.data = data;

        //Цена закупки
        JLabel priceLabel = new JLabel("Цена закупки");
        priceLabel.setBounds(10, 20, 140, 24);
        add(priceLabel);

        priceBuyText = new JTextField();
        priceBuyText.setBounds(215, 20, 160, 24);
        add(priceBuyText);
        addMark(priceBuyText, "Price");

        //Количество
        JLabel quantLabel = new JLabel("Количество");
        quantLabel.setBounds(10, 50, 140, 24);
        add(quantLabel);

        quantText = new JTextField();
        quantText.setBounds(215, 50, 105, 24);
        quantText.setName("Left");
        add(quantText);
        addMark(quantText, "Price");

        quantCombo = new JComboBox<>();
        quantCombo.setBounds(quantText.getX() + quantText.getWidth() + 5, 50, 50, 24);
        add(quantCombo);
        addMark(quantCombo);

        fillFields();
    }

    @Override
    public void setSize() {
        this.setSize(385 + 35, 180);
    }

    @Override
    public void fillFields() {
        quantCombo.removeAllItems();
        SparePart.Unit[] units = SparePart.Unit.values();
        for (SparePart.Unit unit : units) {
            quantCombo.addItem(unit);
        }
        quantCombo.setSelectedItem(data.getUnit());
        quantCombo.setEnabled(false);
    }

    @Override
    public void performAdd() throws Exception {
        //Method not used
    }

    @Override
    public void performEdit() throws Exception {
        data.purchase(Converter.getInstance().convertStrToPrice(quantText.getText()), Converter.getInstance().convertStrToPrice(priceBuyText.getText()));
//        data.setQuantity(data.getQuantity()+Converter.getInstance().convertStrToPrice(quantText.getText())).save();
    }

    @Override
    public boolean otherValidation() {

        try {
            int overflow = Converter.getInstance().convertStrToPrice(quantText.getText());
            if (overflow + data.getPurchasedQuantity() < 0 || overflow < 0) {
                JOptionPane.showMessageDialog(this, "Введено слишком большое число");
                return false;
            }

            overflow = Converter.getInstance().convertStrToPrice(priceBuyText.getText());
            if (overflow < 0) {
                JOptionPane.showMessageDialog(this, "Введена отрицательная цена");
                return false;
            }

            if (quantCombo.getSelectedItem() == SparePart.Unit.pieces) {
                int quant = Converter.getInstance().convertStrToPrice(quantText.getText());
                if (quant % 100 != 0) {
                    JOptionPane.showMessageDialog(this, "Введите целое число штук");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
