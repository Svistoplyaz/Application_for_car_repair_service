package me.svistoplyas.teamdev.graphics.editForms;

import me.svistoplyas.teamdev.graphics.TableModel;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.*;
import net.web_kot.teamdev.db.wrappers.OrderSpareParts;
import org.apache.commons.lang3.tuple.Pair;
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
    private JComboBox<Staff> workerCombo;
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
    
    private JTable tableSparesLeft, tableSparesRight;
    private OrderSpareParts orderParts;
    private List<SparePart> compatible = null;
    
    private int firstRow = 10, secondRow = 170, thirdRow = 420, fourthRow = 770;
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
        modelCombo.addActionListener(e -> updateCompatibleParts(true));
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
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        JScrollPane scrollPaneServiceLeft = new JScrollPane(tableServiceLeft);
        scrollPaneServiceLeft.setBounds(thirdRow, previous, 290, 190);
        tableServiceLeft.getTableHeader().setReorderingAllowed(false);
        tableServiceLeft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ((TableModel) tableServiceLeft.getModel()).addTableModelListener(e -> {

        });
        tableServiceLeft.getColumnModel().getColumn(1).setMaxWidth(50);
        add(scrollPaneServiceLeft);

        //Таблица с услугами которые есть в заказе
        tableServiceRight = new JTable(new TableModel(new String[]{"Услуга", "Цена"}, getDataServiceRight())) {
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
        JScrollPane scrollPaneServiceRight = new JScrollPane(tableServiceRight);
        scrollPaneServiceRight.setBounds(fourthRow, previous, 290, 190);
        tableServiceRight.getTableHeader().setReorderingAllowed(false);
        tableServiceRight.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableServiceRight.getColumnModel().getColumn(1).setMaxWidth(50);
        add(scrollPaneServiceRight);

        //
        JButton serviceToRight = new JButton("->");
        serviceToRight.setBounds(scrollPaneServiceLeft.getX() + scrollPaneServiceLeft.getWidth() + 5, previous, 50, 90);
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
        serviceToLeft.setBounds(scrollPaneServiceLeft.getX() + scrollPaneServiceLeft.getWidth() + 5, previous + 100, 50, 90);
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
        tableSparesLeft = new JTable(new TableModel(new String[]{"Запасная часть", "Цена", "Cклад"}, getDataSparesLeft(false))) {
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
        JScrollPane scrollPaneSparesLeft = new JScrollPane(tableSparesLeft);
        scrollPaneSparesLeft.setBounds(thirdRow, previous, 290, 190);
        tableSparesLeft.getColumnModel().getColumn(1).setMaxWidth(60);
        tableSparesLeft.getColumnModel().getColumn(2).setMaxWidth(60);
        tableSparesLeft.getTableHeader().setReorderingAllowed(false);
        tableSparesLeft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(scrollPaneSparesLeft);

        //Таблица с зап. частями которые есть в заказе
        tableSparesRight = new JTable(new TableModel(new String[]{"Запасная часть", "Кол-во", "Стоим."}, getDataSparesRight())) {
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
        JScrollPane scrollPaneSparesRight = new JScrollPane(tableSparesRight);
        scrollPaneSparesRight.setBounds(fourthRow, previous, 290, 190);
        tableSparesRight.getColumnModel().getColumn(1).setMaxWidth(60);
        tableSparesRight.getColumnModel().getColumn(2).setMaxWidth(60);
        tableSparesRight.getTableHeader().setReorderingAllowed(false);
        tableSparesRight.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(scrollPaneSparesRight);

        //
        JButton SparesToRight = new JButton("->");
        SparesToRight.setBounds(scrollPaneSparesLeft.getX() + scrollPaneSparesLeft.getWidth() + 5, previous, 50, 90);
        SparesToRight.addActionListener(e -> {
            int row = tableSparesLeft.getSelectedRow();
            if (row != -1) {
                javax.swing.table.TableModel model = tableSparesLeft.getModel();
                addSparePart((SparePart)model.getValueAt(row, 3), (int)model.getValueAt(row, 4));
                setCurrentPrice();
            }
        });
        add(SparesToRight);

        JButton SparesToLeft = new JButton("<-");
        SparesToLeft.setBounds(scrollPaneSparesLeft.getX() + scrollPaneSparesLeft.getWidth() + 5, previous + 100, 50, 90);
        SparesToLeft.addActionListener(e -> {
            int row = tableSparesRight.getSelectedRow();
            if (row != -1) {
                javax.swing.table.TableModel model = tableSparesRight.getModel();
                removeSparePart((SparePart)model.getValueAt(row, 3), (int)model.getValueAt(row, 4));
                setCurrentPrice();
            }
        });
        add(SparesToLeft);

        previous += scrollPaneSparesLeft.getHeight() + 15;

        finalPrice = new JLabel();
        finalPrice.setBounds(thirdRow, previous, 370, 24);
        add(finalPrice);

        fillFields();
        setCurrentPrice();
        updateCompatibleParts(false);

        generated = true;
    }

    @Override
    public void setSize() {
        this.setSize(1100, 600);
    }

    @Override
    public void fillFields() {
        Order order = (Order) data;
        
        try {
            clientCombo.removeAllItems();
            for (Client client : mainFrame.model.getClients())
                clientCombo.addItem(client);
            if (isEdit)
                clientCombo.setSelectedItem(order.getClient());

            workerCombo.removeAllItems();
            for (Staff person : mainFrame.model.getStaff())
                workerCombo.addItem(person);
            if (isEdit)
                workerCombo.setSelectedItem(order.getResponsible());

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

            if(order != null) {
                Order.Status cur = order.getCurrentStatus();
                if (cur == Order.Status.CANCELED || cur == Order.Status.CLOSED || cur == Order.Status.FINISHED) {
                    for (Component comp : getAllComponents(this)) {
                        comp.setEnabled(false);
                    }
                    if (cur == Order.Status.FINISHED) {
                        back.setEnabled(true);
                        spinnerOut.setEnabled(true);
                        datePickerOut.setEnabled(true);
                        save.setEnabled(true);
                    }
                    exit.setEnabled(true);
                }
            }
            
            orderParts = order == null ? new OrderSpareParts(mainFrame.model) : order.getSpareParts();
            ((TableModel) tableSparesRight.getModel()).setData(getDataSparesRight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performAdd() {
        Date date;
        date = Converter.getInstance().convertSpinnerAndDataPicker(spinnerIn, datePickerIn);

        try {
            data = mainFrame.model.createOrder((Client) clientCombo.getSelectedItem(), (Staff) workerCombo.getSelectedItem(),
                    (VehicleModel) modelCombo.getSelectedItem(), date).save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Order order = (Order) data;

        //Установка регистрационного номера
        order.setRegistrationNumber(numberText.getText());

        //Установка конечной даты
        date = Converter.getInstance().convertSpinnerAndDataPicker(spinnerOut, datePickerOut);
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

        try {
            ((Order) data).setSpareParts(orderParts);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        setStatuses();
    }

    @Override
    public void performEdit() {
        Order order = (Order) data;

        order.setResponsible((Staff) workerCombo.getSelectedItem());

        //Установка регистрационного номера
        order.setRegistrationNumber(numberText.getText());

        //Установка начальной даты
        Date date;
        date = Converter.getInstance().convertSpinnerAndDataPicker(spinnerIn, datePickerIn);
        order.setStartDate(date);

        //Установка конечной даты
        date = Converter.getInstance().convertSpinnerAndDataPicker(spinnerOut, datePickerOut);
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

        try {
            ((Order) data).setSpareParts(orderParts);
        } catch(Exception e) {
            e.printStackTrace();
        }

        setStatuses();
    }

    //Установка статусов
    private void setStatuses() {
        Order order = (Order) data;
        for (Order.Status status : statusArrayList) {
            try {
                order.setStatus(status);
                printFile(status);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean otherValidation() {
        Order order = (Order) data;

        Date dateStart;
        dateStart = Converter.getInstance().convertSpinnerAndDataPicker(spinnerIn, datePickerIn);

        Date dateFinish = Converter.getInstance().convertSpinnerAndDataPicker(spinnerOut, datePickerOut);

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
                Desktop.getDesktop().open(((Order) data).formDocument(true));
            else if (status == Order.Status.CONFIRMED)
                Desktop.getDesktop().open(((Order) data).formDocument(false));
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
            e.printStackTrace();
        }
    }
    
    private void updateCompatibleParts(boolean force) {
        ((TableModel) tableSparesLeft.getModel()).setData(getDataSparesLeft(force));
    }

    private Object[][] getDataServiceLeft() {
        Order order = (Order) data;
        try {
            List<Service> services = mainFrame.model.getServices();
            if (isEdit) {
                List<Service> hasServices = order.getServices();

                Object[][] ans = new Object[services.size() - hasServices.size()][];

                int i = 0;
                for (Service service : services) {
                    if (!hasServices.contains(service)) {
                        ans[i] = new Object[]{service.getName(), Converter.getInstance().
                                convertPriceToStrOnlyRubbles(service.getPriceForOrder(order)), service};
                        i++;
                    }
                }

                return ans;
            } else {
                Object[][] ans = new Object[services.size()][];

                int i = 0;
                for (Service service : services) {
                    ans[i] = new Object[]{service.getName(), Converter.getInstance().
                            convertPriceToStrOnlyRubbles(service.getPrice()), service};
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
                List<Service> hasServices = order.getServices();

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

    private Object[][] getDataSparesLeft(boolean force) {
        VehicleModel vehicleModel = (VehicleModel)modelCombo.getSelectedItem();
        if(vehicleModel == null) return new Object[0][];
        
        try {
            if(compatible == null || force) compatible = mainFrame.model.getCompatibleSpareParts(vehicleModel);
            ArrayList<Object[]> data = new ArrayList<>();
            
            for(SparePart part : compatible) {
                int real = part.getRealQuantity(this.data == null ? -1 : ((Order)this.data).getId());
                
                int quantity = real - (orderParts == null ? 0 : orderParts.getAmount(part));
                if(quantity <= 0) continue;
                
                data.add(new Object[] {
                        part.getName(), 
                        Converter.getInstance().convertPriceToStr(part.getPrice()), 
                        Converter.getInstance().beautifulQuantity(quantity, part.getUnit()) + " " + part.getUnit(), 
                        part,
                        quantity
                });
            }
            
            Object[][] result = new Object[data.size()][];
            for(int i = 0; i < result.length; i++) result[i] = data.get(i);
            return result;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new Object[0][];
    }

    private Object[][] getDataSparesRight() {
        try {
            return orderParts.getData();
        } catch(Exception ignored) { }
        return new Object[0][];
    }
    
    private int requestQuantity(SparePart part, int maximum) {
        Object rr = JOptionPane.showInputDialog(
                this,
                "Введите количество \"" + part.getName() + "\" (максимум: " + 
                        Converter.getInstance().beautifulQuantity(maximum, part.getUnit()) + " " + part.getUnit() + ")",
                "Количество",
                JOptionPane.QUESTION_MESSAGE, null, null, "1"
        );
        if(rr == null) return -1;
        String result = rr.toString();
        
        int quantity;
        try {
            quantity = Converter.getInstance().convertStrToPrice(result);
            if(part.getUnit() == SparePart.Unit.pieces && quantity % 100 != 0) {
                JOptionPane.showMessageDialog(this, "Введите целое число штук");
                return -1;
            }
        } catch(Exception ignored) {
            JOptionPane.showMessageDialog(this, "Введено недопустимое значение");
            return -1;
        }
        
        if(quantity > maximum) {
            JOptionPane.showMessageDialog(this, "Введено деталей больше доступного количества");
            return -1;
        }

        if(quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Введено отрицательно число или 0");
            return -1;
        }
        
        return quantity;
    }
    
    private void addSparePart(SparePart part, int maximum) {
        int quantity = requestQuantity(part, maximum);
        if(quantity == -1) return;
        
        try {
            orderParts.addSparePart(part, quantity);
            ((TableModel) tableSparesRight.getModel()).setData(getDataSparesRight());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        updateCompatibleParts(false);
    }
    
    private void removeSparePart(SparePart part, int maximum) {
        int quantity = requestQuantity(part, maximum);
        if(quantity == -1) return;

        try {
            try {
                orderParts.removeSparePart(part, quantity);
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            
            ((TableModel) tableSparesRight.getModel()).setData(getDataSparesRight());
        } catch(Exception e) {
            e.printStackTrace();
        }

        updateCompatibleParts(false);
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
        int price = getCurrentServicesPrice() + (orderParts == null ? 0 : orderParts.getPrice());
        finalPrice.setText("Стоимость заказа: " + Converter.getInstance().convertPriceToStr(price));
        repaint();
    }

    private int getCurrentServicesPrice() {
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

    public static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }
}
