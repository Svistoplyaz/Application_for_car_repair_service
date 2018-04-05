package me.svistoplyas.teamdev.graphics.otherFrames;

import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.SparePart;

import javax.swing.*;
import java.util.Date;

public class SpareRefill extends AbstractEdit {
    private SparePart data;
    private JTextField quantText;
    private JComboBox<SparePart.Unit> quantCombo;

    public SpareRefill(JFrame frame, SparePart data) {
        super(frame, true, data);
        this.setTitle("Пополнение количества зап. частей");
        this.data = data;

        //Количество
        JLabel quantLabel = new JLabel("Количество");
        quantLabel.setBounds(10, 20, 140, 24);
        add(quantLabel);

        quantText = new JTextField();
        quantText.setBounds(215, 20, 105, 24);
        quantText.setName("Left");
        add(quantText);
        addMark(quantText, "Price");

        quantCombo = new JComboBox<>();
        quantCombo.setBounds(quantText.getX() + quantText.getWidth() + 5, 20, 50, 24);
        add(quantCombo);
        addMark(quantCombo);

        fillFields();
    }

    @Override
    public void setSize() {
        this.setSize(385 + 35, 150);
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
        data.setQuantity(data.getQuantity()+Converter.getInstance().convertStrToPrice(quantText.getText())).save();
    }

    @Override
    public boolean otherValidation() {

            try {
                int overflow = Converter.getInstance().convertStrToPrice(quantText.getText());
                if(overflow + data.getPrice(new Date()) < 0 || overflow < 0){
                    JOptionPane.showMessageDialog(this, "Введено слишком большое число");
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
