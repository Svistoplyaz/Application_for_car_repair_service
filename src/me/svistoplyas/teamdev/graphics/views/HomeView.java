package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.OrderForm;
import net.web_kot.teamdev.db.entities.Order;
import org.apache.commons.lang3.time.DateUtils;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePanel;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeView extends AbstractView {

    private static final DateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private Date date = new Date();
    private JLabel caption;

    public HomeView(MainFrame _mainFrame) {
        super(_mainFrame);
        
        for(Component c : this.getComponents()) {
            if(c instanceof JScrollPane) c.setBounds(10, 54, 718 + 60 - 220, 378);
            if(c instanceof JLabel) caption = (JLabel) c;
        }

        JDatePanel panel = new JDatePanel();
        panel.setBounds(583, 54, 200, 180);
        
        panel.addActionListener((e) -> {
            DateModel<?> model = panel.getModel();
            
            GregorianCalendar calendar = new GregorianCalendar(model.getYear(), model.getMonth(), model.getDay(), 12, 0);
            date = calendar.getTime();
            
            updateTable();
            updateCaption();
        });
        this.add(panel);

        updateCaption();
    }

    private void updateCaption() {
        caption.setText("Заказы на " + FORMAT.format(date));
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Клиент", "Номер", "Статус", "Ориентир. приём", "Ориентир. выдача"};
    }

    @Override
    Object[][] getData() {
        try {
            if(date == null) date = new Date();
            List<Order> orders = mainFrame.model.getOrders(date);
            Object[][] ans = new Object[orders.size()][];
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            for(int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                ans[i] = new Object[]{
                        order.getClient(), order.getRegistrationNumber(), order.getCurrentStatus(),
                        df.format(order.getStartDate()),df.format(order.getFinishDate()),
                        order};
            }
            
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getState(Order order, Date date) {
        Date start = DateUtils.addMilliseconds(DateUtils.truncate(date, Calendar.DATE), -1);
        Date end = DateUtils.ceiling(date, Calendar.DATE);

        Date check = order.getStartDate();
        if(check.after(start) && check.before(end)) return "Прием";
        return "Выдача";
    }

    @Override
    Object getObject(int row) {
        try {
            return (Order)mainFrame.getView("Home").table.getModel().getValueAt(row, 5);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    boolean canAdd() {
        return true;
    }

    @Override
    boolean canEdit() {
        return true;
    }

    @Override
    boolean canDelete() {
        return mainFrame.type;
    }

    @Override
    void performDelete(int row) throws Exception {
        ((Order)mainFrame.getView("Home").table.getModel().getValueAt(row, 5)).delete();
//        mainFrame.model.getOrders().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new OrderForm(mainFrame, b, o, date);
    }

    @Override
    public String toString() {
        return "Домашняя страница";
    }
}
