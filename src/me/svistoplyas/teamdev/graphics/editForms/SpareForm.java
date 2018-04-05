package me.svistoplyas.teamdev.graphics.editForms;

import me.svistoplyas.teamdev.graphics.TableModel;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.Mark;
import net.web_kot.teamdev.db.entities.SparePart;
import net.web_kot.teamdev.db.entities.VehicleModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpareForm extends AbstractEdit {
    private JTextField nameText;
    private JTextField priceText;
    private JTextField quantText;
    private JComboBox<SparePart.Unit> quantCombo;
    private JCheckBox universalCheck;
    private JTable tableModelLeft;
    private JTable tableModelRight;
    private JButton modelToLeft;
    private JButton modelToRight;

    private boolean isEdit;

    public SpareForm(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit, data);
        this.isEdit = isEdit;

        int previous = 20;
        int firstRow = 10;
        int secondRow = 175;

        //Название
        JLabel nameLabel = new JLabel("Название запасной части");
        nameLabel.setBounds(firstRow, previous, 140, 24);
        add(nameLabel);

        nameText = new JTextField();
        nameText.setBounds(secondRow, previous, 200, 24);
        add(nameText);
        addMark(nameText);

        previous += 30;

        //Цена
        JLabel priceLabel = new JLabel("Цена");
        priceLabel.setBounds(firstRow, previous, 140, 24);
        add(priceLabel);

        priceText = new JTextField();
        priceText.setBounds(secondRow, previous, 200, 24);
        add(priceText);
        addMark(priceText, "Price");

        previous += 30;

        //Количество
        JLabel quantLabel = new JLabel("Количество");
        quantLabel.setBounds(firstRow, previous, 140, 24);
        add(quantLabel);

        quantText = new JTextField();
        quantText.setBounds(secondRow, previous, 145, 24);
        quantText.setName("Left");
        add(quantText);
        addMark(quantText, "Price");

        quantCombo = new JComboBox<>();
        quantCombo.setBounds(quantText.getX() + quantText.getWidth() + 5, previous, 50, 24);
        add(quantCombo);
        addMark(quantCombo);

        previous += 30;

        //Универсальность
        universalCheck = new JCheckBox("Универсальная деталь");
        universalCheck.setBounds(5, previous, 160, 24);
        add(universalCheck);

        previous += 30;

        JLabel services = new JLabel("Совместимые модели:");
        services.setBounds(firstRow, previous, 190, 24);
        add(services);

        previous += 25;

        //Таблица с услугами которых нет в заказе
        tableModelLeft = new JTable(new TableModel(new String[]{"Название"}, getDataModelLeft())) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(getValueAt(row, column).toString());
                }
                return c;
            }
        };
        tableModelLeft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPaneServiceLeft = new JScrollPane(tableModelLeft);
        scrollPaneServiceLeft.setBounds(firstRow, previous, 165, 190);
        add(scrollPaneServiceLeft);

        //Таблица с услугами которые есть в заказе
        tableModelRight = new JTable(new TableModel(new String[]{"Название"}, getDataModelRight())) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(getValueAt(row, column).toString());
                }
                return c;
            }
        };
        tableModelRight.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPaneServiceRight = new JScrollPane(tableModelRight);
        scrollPaneServiceRight.setBounds(235, previous, 165, 190);
        add(scrollPaneServiceRight);

        //
        modelToRight = new JButton("->");
        modelToRight.setBounds(scrollPaneServiceLeft.getX() + scrollPaneServiceLeft.getWidth() + 5, previous, 50, 90);
        modelToRight.addActionListener(e -> {
            int row = tableModelLeft.getSelectedRow();
            if (row != -1) {
                ((TableModel) tableModelRight.getModel()).addData(((TableModel) tableModelLeft.getModel()).getValueAt(row));
                ((TableModel) tableModelLeft.getModel()).deleteData(row);
            }
        });
        add(modelToRight);

        modelToLeft = new JButton("<-");
        modelToLeft.setBounds(scrollPaneServiceLeft.getX() + scrollPaneServiceLeft.getWidth() + 5, previous + 100, 50, 90);
        modelToLeft.addActionListener(e -> {
            int row = tableModelRight.getSelectedRow();
            if (row != -1) {
                try {
                    ((TableModel) tableModelLeft.getModel()).addData(((TableModel) tableModelRight.getModel()).getValueAt(row));
                    ((TableModel) tableModelRight.getModel()).deleteData(row);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(modelToLeft);

        if (universalCheck.isSelected()) {
            setTableEnable(false);
        } else {
            setTableEnable(true);
        }

        universalCheck.addActionListener(e -> {
            if (universalCheck.isSelected()) {
                setTableEnable(false);
            } else {
                setTableEnable(true);
            }
        });

        fillFields();
    }

    @Override
    public void setSize() {
        this.setSize(385 + 35, 460);
    }

    @Override
    public void fillFields() {
        SparePart sparePart = (SparePart) data;

        try {
            quantCombo.removeAllItems();
            SparePart.Unit[] units = SparePart.Unit.values();
            for (SparePart.Unit unit : units) {
                quantCombo.addItem(unit);
            }
            if (isEdit) {
                quantCombo.setSelectedItem(sparePart.getUnit());
                quantCombo.setEnabled(false);

                nameText.setText(sparePart.getName());

                priceText.setText(Converter.getInstance().convertPriceToStr(sparePart.getPrice(new Date())));

                quantText.setText(sparePart.getBeautifulQuantity());

                if(sparePart.isUniversal())
                    universalCheck.setSelected(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void performAdd() throws Exception {
        //Создание новой детали
        data = mainFrame.model.createSparePart(nameText.getText(), (SparePart.Unit) quantCombo.getSelectedItem(),
                universalCheck.isSelected()).save();

        SparePart sparePart = (SparePart) data;

        //Установка количества
        sparePart.setQuantity(Converter.getInstance().convertStrToPrice(quantText.getText()));

        duplicateCode(sparePart);

        sparePart.setPrice(Converter.getInstance().convertStrToPrice(priceText.getText()));
    }

    @Override
    public void performEdit() throws Exception {
        SparePart sparePart = (SparePart) data;

        //Название
        sparePart.setName(nameText.getText());

        //Универсальность
        sparePart.setUniversal(universalCheck.isSelected());

        //Установка количества
        sparePart.setQuantity(Converter.getInstance().convertStrToPrice(quantText.getText()));

        duplicateCode(sparePart);

        sparePart.setPrice(Converter.getInstance().convertStrToPrice(priceText.getText()));
    }

    private void duplicateCode(SparePart sparePart) {
        //Подходящие модели
        Object[][] tableData = ((TableModel) tableModelRight.getModel()).getData();
        ArrayList<VehicleModel> vehicleModels = new ArrayList<>();
        for (Object[] objects : tableData) {
            vehicleModels.add((VehicleModel) objects[1]);
        }
        try {
            sparePart.save();
            sparePart.setCompatibleModels(vehicleModels);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean otherValidation() {
        if (quantCombo.getSelectedItem() == SparePart.Unit.pieces)
            try {
                int quant = Converter.getInstance().convertStrToPrice(quantText.getText());
                if (quant % 100 != 0) {
                    JOptionPane.showMessageDialog(this, "Введите целое число штук");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return true;
    }

    private void setTableEnable(boolean value){
        tableModelLeft.setEnabled(value);
        tableModelRight.setEnabled(value);
        modelToLeft.setEnabled(value);
        modelToRight.setEnabled(value);
    }

    private Object[][] getDataModelLeft() {
        SparePart sparePart = (SparePart) data;
        try {
            List<VehicleModel> vehiclesModels = mainFrame.model.getVehiclesModels();
            if (isEdit) {
                List<VehicleModel> hasModels = sparePart.getCompatibleModels();

                Object[][] ans = new Object[vehiclesModels.size() - hasModels.size()][];

                int i = 0;
                for (VehicleModel vehicleModel : vehiclesModels) {
                    if (!hasModels.contains(vehicleModel)) {
                        ans[i] = new Object[]{vehicleModel, vehicleModel};
                        i++;
                    }
                }

                return ans;
            } else {
                Object[][] ans = new Object[vehiclesModels.size()][];

                int i = 0;
                for (VehicleModel vehicleModel : vehiclesModels) {
                    ans[i] = new Object[]{vehicleModel, vehicleModel};
                    i++;
                }

                return ans;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object[][] getDataModelRight() {
        SparePart sparePart = (SparePart) data;
        try {
            if (isEdit) {
                List<VehicleModel> hasModels = sparePart.getCompatibleModels();

                Object[][] ans = new Object[hasModels.size()][];

                int i = 0;
                for (VehicleModel vehicleModel : hasModels) {
                    ans[i] = new Object[]{vehicleModel, vehicleModel};
                    i++;
                }

                return ans;
            } else {
                return new Object[0][];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
