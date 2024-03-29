package me.svistoplyas.teamdev.graphics.editForms;

import me.svistoplyas.teamdev.graphics.TableModel;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.SparePart;
import net.web_kot.teamdev.db.entities.VehicleModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpareForm extends AbstractEdit {
    private JTextField nameText;
    private JTextField priceBuyText;
    private JTextField priceSellText;
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

        //Цена закупки
        if(!isEdit) {
            JLabel priceBuyLabel = new JLabel("Цена закупки");
            priceBuyLabel.setBounds(firstRow, previous, 140, 24);
            add(priceBuyLabel);

            priceBuyText = new JTextField();
            priceBuyText.setBounds(secondRow, previous, 200, 24);
            add(priceBuyText);
            addMark(priceBuyText, "Price");
        }

        previous += 30;

        //Цена продажи
        JLabel priceSellLabel = new JLabel("Цена продажи");
        priceSellLabel.setBounds(firstRow, previous, 140, 24);
        add(priceSellLabel);

        priceSellText = new JTextField();
        priceSellText.setBounds(secondRow, previous, 200, 24);
        add(priceSellText);
        addMark(priceSellText, "Price");

        previous += 30;

        //Количество
        if(!isEdit) {
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
        }

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
        this.setSize(385 + 35, 490);
    }

    @Override
    public void fillFields() {
        SparePart sparePart = (SparePart) data;

        try {
            if(!isEdit) {
                quantCombo.removeAllItems();
                SparePart.Unit[] units = SparePart.Unit.values();
                for (SparePart.Unit unit : units) {
                    quantCombo.addItem(unit);
                }
            } else {
                nameText.setText(sparePart.getName());

                priceSellText.setText(Converter.getInstance().convertPriceToStr(sparePart.getPrice(new Date())));
                
                if (sparePart.isUniversal())
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

        //Закупка
        sparePart.purchase(Converter.getInstance().convertStrToPrice(quantText.getText()),
                Converter.getInstance().convertStrToPrice(priceBuyText.getText()));

        duplicateCode(sparePart);

        sparePart.setPrice(Converter.getInstance().convertStrToPrice(priceSellText.getText()));
    }

    @Override
    public void performEdit() throws Exception {
        SparePart sparePart = (SparePart) data;

        //Название
        sparePart.setName(nameText.getText());

        //Универсальность
        sparePart.setUniversal(universalCheck.isSelected());

        duplicateCode(sparePart);

        sparePart.setPrice(Converter.getInstance().convertStrToPrice(priceSellText.getText()));
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
        if (!isEdit && quantCombo.getSelectedItem() == SparePart.Unit.pieces)
            try {
                int quant = Converter.getInstance().convertStrToPrice(quantText.getText());
                if (quant % 100 != 0) {
                    JOptionPane.showMessageDialog(this, "Введите целое число штук");
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        String name = nameText.getText().trim();
        if (data != null && ((SparePart) data).getName().equals(name))
            return true;

        try {
            List<SparePart> parts = mainFrame.model.getSpareParts();
            for (SparePart part : parts)
                if (part.getName().equals(name)) {
                    JOptionPane.showMessageDialog(this, "Запись с таким именем уже существует!");
                    return false;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void setTableEnable(boolean value) {
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
