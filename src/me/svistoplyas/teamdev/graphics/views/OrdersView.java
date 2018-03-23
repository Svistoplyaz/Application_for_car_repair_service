package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.OrderForm;
import net.web_kot.teamdev.db.entities.Client;
import net.web_kot.teamdev.db.entities.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrdersView extends AbstractView {

    public OrdersView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Клиент", "Работник", "Регистрационный номер", "Начат", "Закрыт", "Марка", "Модель", "Статус"};
    }

    @Override
    Object[][] getData() {
        try {
            List<Order> orders = mainFrame.model.getOrders();
            Object[][] ans = new Object[orders.size()][];
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            int i = 0;
            for (Order order : orders) {
                String worker = "";

                String finishDate = "-";
                Date d = order.getRealFinishDate();
                if (d != null) finishDate = df.format(d);

                ans[i] = new Object[]{order.getClient(), worker, order.getRegistrationNumber(),
                        df.format(order.getRealStartDate()), finishDate,
                        order.getVehicleModel().getMark(), order.getVehicleModel(),
                        order.getCurrentStatus()};
                i++;
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
        } catch (Exception e) {
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
        return new OrderForm(mainFrame, b, o, new Date());
    }

    @Override
    public String toString() {
        return "Заказы";
    }
}
