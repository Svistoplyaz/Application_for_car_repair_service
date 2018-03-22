package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.OrderForm;
import net.web_kot.teamdev.db.entities.Order;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePanel;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeView extends AbstractView {
    
    private Date date = new Date();
    
    public HomeView(MainFrame _mainFrame) {
        super(_mainFrame);
        
        for(Component c : this.getComponents())
            if(c instanceof JScrollPane) c.setBounds(10, 54, 718 + 60 - 220, 378);
        
        JDatePanel panel = new JDatePanel();
        panel.setBounds(583, 54, 200, 180);
        
        panel.addActionListener((e) -> {
            DateModel<?> model = panel.getModel();
            
            GregorianCalendar calendar = new GregorianCalendar(model.getYear(), model.getMonth(), model.getDay());
            date = calendar.getTime();
            
            updateTable();
        });
        this.add(panel);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Клиент", "Регистрационный номер", "Статус"};
    }

    @Override
    Object[][] getData() {
        try {
            if(date == null) date = new Date();
            List<Order> orders = mainFrame.model.getOrders(date);
            Object[][] ans = new Object[orders.size()][];
            
            for(int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                ans[i] = new Object[]{order.getClient(), order.getRegistrationNumber(), order.getCurrentStatus()};
            }
            
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    Object getObject(int row) {
        try {
            return mainFrame.model.getOrders().get(row);
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
        mainFrame.model.getOrders().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new OrderForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Домашняя страница";
    }
}
