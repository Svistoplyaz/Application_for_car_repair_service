package me.svistoplyas.teamdev.graphics.editForms;

import me.svistoplyas.teamdev.graphics.TableModel;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class OrderForm extends AbstractEdit {
    private JComboBox<Client> clientCombo;
    private JComboBox<String> workerCombo;
    private JTextField numberText;
    private JComboBox<Mark> markCombo;
    private JComboBox<VehicleModel> modelCombo;
    private JButton back;
    private JButton forward;
    private JLabel curLabel;
    private JSpinner spinnerIn;
    private JDatePicker datePickerIn;
    private JSpinner spinnerOut;
    private JDatePicker datePickerOut;
    private boolean isEdit;
    private ArrayList<Order.Status> statusArrayList = new ArrayList<>();
    private boolean generated = false;
    private JTable tableServiceRight;
    private JLabel finalPrice;

    private int firstRow = 10, secondRow = 170, thirdRow = 420, fourthRow = 670;
    private int previous;

    public OrderForm(JFrame frame, boolean _isEdit, Object data, Date date) {
        super(frame, _isEdit, data);
        isEdit = _isEdit;

        //Клиент
        JLabel clientLabel = new JLabel("Клиент");
        clientLabel.setBounds(firstRow, 20, 120, 24);
        add(clientLabel);

        clientCombo = new JComboBox<>();
        clientCombo.setBounds(secondRow, 20, 200, 24);
        add(clientCombo);
        if (isEdit)
            clientCombo.setEnabled(false);
        addMark(clientCombo);

        previous = clientLabel.getY();

        //Ответственный
        JLabel workerLabel = new JLabel("Ответственный");
        workerLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(workerLabel);

        workerCombo = new JComboBox<>();
        workerCombo.setBounds(secondRow, previous + 30, 200, 24);
        add(workerCombo);
        if (isEdit)
            workerCombo.setEnabled(false);
//        addMark(workerCombo);

        previous += 30;

        //Рег. номер
        JLabel numberLabel = new JLabel("Регистационный номер");
        numberLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(numberLabel);

        numberText = new JTextField();
        numberText.setBounds(secondRow, previous + 30, 200, 24);
        add(numberText);
        addMark(numberText, "");

        previous += 30;

        //Марка
        JLabel markLabel = new JLabel("Марка");
        markLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(markLabel);

        markCombo = new JComboBox<>();
        markCombo.setBounds(secondRow, previous + 30, 200, 24);
        add(markCombo);
        if (isEdit)
            markCombo.setEnabled(false);
        markCombo.addActionListener(e -> {
            changeMark();
        });
        addMark(markCombo);

        previous += 30;

        //Модель
        JLabel modelLabel = new JLabel("Модель");
        modelLabel.setBounds(firstRow, previous + 30, 120, 24);
        add(modelLabel);

        modelCombo = new JComboBox<>();
        modelCombo.setBounds(secondRow, previous + 30, 200, 24);
        add(modelCombo);
        if (isEdit)
            modelCombo.setEnabled(false);
        addMark(modelCombo);

        previous += 30;

        //Статус и кнопки его изменения
        try {
            if (data == null)
                setStatusLabel(Order.Status.PRELIMINARY);
            else
                setStatusLabel(((Order) data).getCurrentStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

        previous += 40;

        //Таблица с историей статусов
        JTable table = new JTable(new TableModel(new String[]{"Статус", "Дата"}, getStatusTableData()));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(firstRow, previous + 30, 360, 134);
        add(scrollPane);

        previous += 140;

        //Дата и время приёма
        JLabel inDateLabel = new JLabel("<html>Ориентировочная дата и время приёма");
        inDateLabel.setBounds(firstRow, previous + 30, 120, 54);
        add(inDateLabel);

        // Не твой код
        if (date.before(new Date()) || date.getTime() / 1000 == System.currentTimeMillis() / 1000)
            date = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

        SpinnerDateModel smIn = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerIn = new javax.swing.JSpinner(smIn);
        JSpinner.DateEditor deIn = new JSpinner.DateEditor(spinnerIn, "HH:mm");
        spinnerIn.setEditor(deIn);
        spinnerIn.setBounds(secondRow, previous + 30, 200, 24);
        add(spinnerIn);

        previous += 30;

        datePickerIn = new JDatePicker(date);
        datePickerIn.setBounds(secondRow, previous + 30, 200, 24);
        add(datePickerIn);

        try {
            if (!(data == null || ((Order) data).getCurrentStatus() == Order.Status.PRELIMINARY)) {
                spinnerIn.setEnabled(false);
                datePickerIn.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        previous += 30;

        //Дата и время выдачи
        JLabel outDateLabel = new JLabel("<html>Ориентировочная дата и время выдачи");
        outDateLabel.setBounds(firstRow, previous + 30, 120, 54);
        add(outDateLabel);

        SpinnerDateModel smOut = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        spinnerOut = new javax.swing.JSpinner(smOut);
        JSpinner.DateEditor deOut = new JSpinner.DateEditor(spinnerOut, "HH:mm");
        spinnerOut.setEditor(deOut);
        spinnerOut.setBounds(secondRow, previous + 30, 200, 24);
        add(spinnerOut);

        previous += 30;

        datePickerOut = new JDatePicker(date);
        datePickerOut.setBounds(secondRow, previous + 30, 200, 24);
        add(datePickerOut);

        previous = 20;

        JLabel services = new JLabel("Услуги:");
        services.setBounds(thirdRow, previous, 190, 24);
        add(services);

        previous += 25;

        //Таблица с услугами которых нет в заказе
        JTable tableServiceLeft = new JTable(new TableModel(new String[]{"Услуга", "Цена"}, getDataServiceLeft())) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(getValueAt(row, column).toString());
                }
                return c;
            }
        };
        JScrollPane scrollPaneServiceLeft = new JScrollPane(tableServiceLeft);
        scrollPaneServiceLeft.setBounds(thirdRow, previous, 190, 190);
        ((TableModel) tableServiceLeft.getModel()).addTableModelListener(e -> {

        });
        add(scrollPaneServiceLeft);

        //Таблица с услугами которые есть в заказе
        tableServiceRight = new JTable(new TableModel(new String[]{"Услуга", "Цена"}, getDataServiceRight())) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(getValueAt(row, column).toString());
                }
                return c;
            }
        };
        JScrollPane scrollPaneServiceRight = new JScrollPane(tableServiceRight);
        scrollPaneServiceRight.setBounds(fourthRow, previous, 190, 190);
        add(scrollPaneServiceRight);

        //
        JButton serviceToRight = new JButton("->");
        serviceToRight.setBounds(thirdRow + scrollPaneServiceLeft.getWidth() + 5, previous, 50, 90);
        serviceToRight.addActionListener(e -> {
            int row = tableServiceLeft.getSelectedRow();
            if (row != -1) {
                ((TableModel) tableServiceRight.getModel()).addData(((TableModel) tableServiceLeft.getModel()).getValueAt(row));
                ((TableModel) tableServiceLeft.getModel()).deleteData(row);
//                OrderForm.this.repaint();
                setCurrentPrice();
            }
        });
        add(serviceToRight);

        JButton serviceToLeft = new JButton("<-");
        serviceToLeft.setBounds(thirdRow + scrollPaneServiceLeft.getWidth() + 5, previous + 100, 50, 90);
        serviceToLeft.addActionListener(e -> {
            int row = tableServiceRight.getSelectedRow();
            if (row != -1) {
                Service service = (Service) ((TableModel) tableServiceRight.getModel()).getValueAt(row)[2];
                try {
                    if (data == null || ((Order) data).getCurrentStatus() == Order.Status.PRELIMINARY ||
                            !((Order) data).getServices().contains(service)) {
                        ((TableModel) tableServiceLeft.getModel()).addData(((TableModel) tableServiceRight.getModel()).getValueAt(row));
                        ((TableModel) tableServiceRight.getModel()).deleteData(row);
//                        OrderForm.this.repaint();
                        setCurrentPrice();
                    } else {
                        JOptionPane.showMessageDialog(this, "Нельзя отменить услугу!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(serviceToLeft);

        previous += scrollPaneServiceLeft.getHeight() + 5;

        JLabel spares = new JLabel("Запасные части:");
        spares.setBounds(thirdRow, previous, 190, 24);
        add(spares);

        previous += 25;

        //Таблица с зап. частями которых нет в заказе
        JTable tableSparesLeft = new JTable(new TableModel(new String[]{"Зап. часть", "Количество", "Цена"}, getDataSparesLeft()));
        JScrollPane scrollPaneSparesLeft = new JScrollPane(tableSparesLeft);
        scrollPaneSparesLeft.setBounds(thirdRow, previous, 190, 190);
        add(scrollPaneSparesLeft);

        //Таблица с зап. частями которые есть в заказе
        JTable tableSparesRight = new JTable(new TableModel(new String[]{"Зап. часть", "Количество", "Цена"}, getDataSparesRight()));
        JScrollPane scrollPaneSparesRight = new JScrollPane(tableSparesRight);
        scrollPaneSparesRight.setBounds(fourthRow, previous, 190, 190);
        add(scrollPaneSparesRight);

        //
        JButton SparesToRight = new JButton("->");
        SparesToRight.setBounds(thirdRow + scrollPaneSparesLeft.getWidth() + 5, previous, 50, 90);
        SparesToRight.addActionListener(e -> {
            int row = tableSparesLeft.getSelectedRow();
            if (row != -1) {
                ((TableModel) tableSparesRight.getModel()).addData(((TableModel) tableSparesLeft.getModel()).getValueAt(row));
                ((TableModel) tableSparesLeft.getModel()).deleteData(row);
//                OrderForm.this.repaint();
                setCurrentPrice();
            }
        });
        add(SparesToRight);

        JButton SparesToLeft = new JButton("<-");
        SparesToLeft.setBounds(thirdRow + scrollPaneSparesLeft.getWidth() + 5, previous + 100, 50, 90);
        SparesToLeft.addActionListener(e -> {
            int row = tableSparesRight.getSelectedRow();
            if (row != -1) {
                ((TableModel) tableSparesLeft.getModel()).addData(((TableModel) tableSparesRight.getModel()).getValueAt(row));
                ((TableModel) tableSparesRight.getModel()).deleteData(row);
//                OrderForm.this.repaint();
                setCurrentPrice();
            }
        });
        add(SparesToLeft);

        previous += scrollPaneSparesLeft.getHeight() + 15;

        finalPrice = new JLabel();
        finalPrice.setBounds(thirdRow, previous, 370, 24);
        setCurrentPrice();
        add(finalPrice);

        fillFields();

        generated = true;
    }

    @Override
    void setSize() {
        this.setSize(900, 600);
    }

    @Override
    public void fillFields() {
        Order order = (Order) data;

        try {
            for (Client client : mainFrame.model.getClients())
                clientCombo.addItem(client);
            if (isEdit)
                clientCombo.setSelectedItem(order.getClient());

            markCombo.removeAllItems();
            for (Mark mark : mainFrame.model.getMarks())
                markCombo.addItem(mark);
            if (isEdit)
                markCombo.setSelectedItem(order.getVehicleModel().getMark());

            if (isEdit)
                numberText.setText(order.getRegistrationNumber());

            modelCombo.removeAllItems();
            if (markCombo.getItemCount() > 0) {
                for (VehicleModel model : ((Mark) markCombo.getSelectedItem()).getVehiclesModels())
                    modelCombo.addItem(model);
                if (isEdit)
                    modelCombo.setSelectedItem(order.getVehicleModel());
            }

            if (isEdit) {
                Date date = order.getStartDate();
                spinnerIn.getModel().setValue(date);

                datePickerIn.getModel().setDate(1900 + date.getYear(), date.getMonth(), date.getDate());

                date = order.getFinishDate();
                if (date != null) {
                    spinnerOut.getModel().setValue(date);
                    datePickerOut.getModel().setDate(1900 + date.getYear(), date.getMonth(), date.getDate());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performAdd() {
        Date date;
        date = ((SpinnerDateModel) spinnerIn.getModel()).getDate();
        DateModel model = datePickerIn.getModel();
        date.setDate(model.getDay());
        date.setMonth(model.getMonth());
        date.setYear(model.getYear() - 1900);

        try {
            data = mainFrame.model.createOrder((Client) clientCombo.getSelectedItem(), (VehicleModel) modelCombo.getSelectedItem(), date).save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Order order = (Order) data;

        //Установка регистрационного номера
        order.setRegistrationNumber(numberText.getText());

        //Установка статусов
        for (Order.Status status : statusArrayList) {
            try {
                order.setStatus(status);
                printFile(status);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Установка конечной даты
        date = ((SpinnerDateModel) spinnerOut.getModel()).getDate();
        model = datePickerOut.getModel();
        date.setDate(model.getDay());
        date.setMonth(model.getMonth());
        date.setYear(model.getYear() - 1900);
        order.setFinishDate(date);

        //Услуги
        Object[][] tableData = ((TableModel) tableServiceRight.getModel()).getData();
        ArrayList<Service> services = new ArrayList<>();
        for (Object[] objects : tableData) {
            services.add((Service) objects[2]);
        }
        try {
            order.setServices(services);
            order.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void performEdit() {
        Order order = (Order) data;

        //Установка регистрационного номера
        order.setRegistrationNumber(numberText.getText());

        //Установка статусов
        for (Order.Status status : statusArrayList) {
            try {
                order.setStatus(status);
                printFile(status);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Установка начальной даты
        Date date;
        date = ((SpinnerDateModel) spinnerIn.getModel()).getDate();
        DateModel model = datePickerIn.getModel();
        date.setDate(model.getDay());
        date.setMonth(model.getMonth());
        date.setYear(model.getYear() - 1900);
        order.setStartDate(date);

        //Установка конечной даты
        date = ((SpinnerDateModel) spinnerOut.getModel()).getDate();
        model = datePickerOut.getModel();
        date.setDate(model.getDay());
        date.setMonth(model.getMonth());
        date.setYear(model.getYear() - 1900);
        order.setFinishDate(date);

        //Услуги
        Object[][] tableData = ((TableModel) tableServiceRight.getModel()).getData();
        ArrayList<Service> services = new ArrayList<>();
        for (Object[] objects : tableData) {
            services.add((Service) objects[2]);
        }
        try {
            order.setServices(services);
            order.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean otherValidation() {
        Order order = (Order) data;

        //Установка начальной даты
        Date dateStart;
        dateStart = ((SpinnerDateModel) spinnerIn.getModel()).getDate();
        DateModel model = datePickerIn.getModel();
        dateStart.setDate(model.getDay());
        dateStart.setMonth(model.getMonth());
        dateStart.setYear(model.getYear() - 1900);

        //Установка конечной даты
        Date dateFinish = ((SpinnerDateModel) spinnerOut.getModel()).getDate();
        model = datePickerOut.getModel();
        dateFinish.setDate(model.getDay());
        dateFinish.setMonth(model.getMonth());
        dateFinish.setYear(model.getYear() - 1900);

        //order
        if (order == null || order.getStartDate().getTime() / 1000 / 60 != dateStart.getTime() / 1000 / 60) {
            if (dateStart.before(new Date())) {
                JOptionPane.showMessageDialog(this, "Ориентировочная дата приема раньше текущей");
                return false;
            }
        }

        if (order == null || order.getFinishDate().getTime() / 1000 / 60 != dateFinish.getTime() / 1000 / 60)
            if (dateFinish.before(new Date())) {
                JOptionPane.showMessageDialog(this, "Ориентировочная дата выдачи раньше текущей");
                return false;
            }

        if (dateFinish.before(dateStart)) {
            JOptionPane.showMessageDialog(this, "Ориентировочная дата выдачи раньше приема");
            return false;
        }

        return true;
    }

    private void printFile(Order.Status status) {
        try {
            if (status == Order.Status.FINISHED)
                Desktop.getDesktop().open(((Order) data).formDocument());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeMark() {
        try {
            if (markCombo.getItemCount() > 0) {
                modelCombo.removeAllItems();
                for (VehicleModel model : ((Mark) markCombo.getSelectedItem()).getVehiclesModels())
                    modelCombo.addItem(model);
                modelCombo.setSelectedItem(0);
                repaint();
            }
        } catch (Exception e) {

        }
    }

    private Object[][] getDataServiceLeft() {
        Order order = (Order) data;
        try {
            List<Service> services = mainFrame.model.getServices();
            if (isEdit) {
                List<Service> hasServices = ((Order) data).getServices();

                Object[][] ans = new Object[services.size() - hasServices.size()][];

                int i = 0;
                for (Service service : services) {
                    if (!hasServices.contains(service)) {
                        ans[i] = new Object[]{service.getName(), Converter.getInstance().convertPriceToStrOnlyRubbles(service.getPriceForOrder(order)), service};
                        i++;
                    }
                }

                return ans;
            } else {
                Object[][] ans = new Object[services.size()][];

                int i = 0;
                for (Service service : services) {
                    ans[i] = new Object[]{service.getName(), Converter.getInstance().convertPriceToStrOnlyRubbles(service.getPrice()), service};
                    i++;
                }

                return ans;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object[][] getDataServiceRight() {
        Order order = (Order) data;
        try {
            if (isEdit) {
                List<Service> hasServices = ((Order) data).getServices();

                Object[][] ans = new Object[hasServices.size()][];

                int i = 0;
                for (Service service : hasServices) {
                    ans[i] = new Object[]{service.getName(), Converter.getInstance().convertPriceToStrOnlyRubbles(service.getPriceForOrder(order)), service};
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

    private Object[][] getDataSparesLeft() {
        return new Object[0][];
    }

    private Object[][] getDataSparesRight() {
        return new Object[0][];
    }

    private void setStatusLabel(Order.Status status) {
        //Статус
        if (!generated) {
            JLabel statusLabel = new JLabel("Статус:");
            statusLabel.setBounds(firstRow, previous + 30, 120, 24);
            add(statusLabel);
        }

        try {
            if (!generated)
                curLabel = new JLabel(status.toString());
            else
                curLabel.setText(status.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!generated) {
            curLabel.setBounds(secondRow, previous + 30, 200, 24);
            add(curLabel);
        }

        previous += 30;

        try {
            Order.Status[] statuses = Order.getPossibleStatuses(status);

            if (statuses.length >= 1) {
                if (!generated) {
                    back = new JButton(statuses[0].toString());
                    back.setBounds(firstRow, previous + 30, 150, 24);
                    add(back);
                } else
                    back.setText(statuses[0].toString());
                back.setVisible(true);

                for (ActionListener al : back.getActionListeners()) {
                    back.removeActionListener(al);
                }

                back.addActionListener(e -> {
                    try {
                        statusArrayList.add(statuses[0]);
                        //System.out.println(statuses[0]);
                        OrderForm.this.setStatusLabel(statuses[0]);
                        OrderForm.this.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                if (generated)
                    back.setVisible(false);
            }

            if (statuses.length == 2) {
                if (!generated) {
                    forward = new JButton(statuses[1].toString());
                    forward.setBounds(secondRow, previous + 30, 200, 24);
                    add(forward);
                } else
                    forward.setText(statuses[1].toString());
                forward.setVisible(true);

                for (ActionListener al : forward.getActionListeners()) {
                    forward.removeActionListener(al);
                }

                forward.addActionListener(e -> {
                    try {
                        statusArrayList.add(statuses[1]);
                        //System.out.println(statuses[1]);
                        OrderForm.this.setStatusLabel(statuses[1]);
                        OrderForm.this.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                if (generated)
                    forward.setVisible(false);
            }
        } catch (Exception e) {

        }

        this.repaint();
    }

    private Object[][] getStatusTableData() {
        if (data != null) {
            try {
                ArrayList<Pair<String, String>> arrayList = ((Order) data).getHistory();
                Object[][] ans = new Object[arrayList.size()][];
                int i = 0;
                for (Pair pair : arrayList) {
                    ans[i] = new Object[]{pair.getLeft(), pair.getRight()};
                    i++;
                }
                return ans;
            } catch (Exception e) {
                e.printStackTrace();
                return new Object[0][];
            }
        } else
            return new Object[0][];
    }

    private void setCurrentPrice() {
        finalPrice.setText("Стоимость заказа: " + Converter.getInstance().convertPriceToStr(getCurrentPrice()));
        repaint();
    }

    private int getCurrentPrice() {
        Object[][] tableData = ((TableModel) tableServiceRight.getModel()).getData();
        int len = tableData.length;

        ArrayList<Service> services = new ArrayList<>();
        for (Object[] aTableData : tableData) {
            services.add((Service) aTableData[aTableData.length - 1]);
        }

        try {
            if (data == null)
                return Order.getPrice(null, services);
            else
                return Order.getPrice((Order) data, services);
        } catch (Exception e) {
            return 0;
        }
    }
}
