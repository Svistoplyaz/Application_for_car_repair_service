package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.utils.Converter;

public class SparesView extends AbstractView {

    public SparesView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Название", "Цена", "Количество", "Единиц", "Модели"};
    }

    @Override
    Object[][] getData() {

//        try {
//            List<Order> orders = mainFrame.model.getOrders();
//            Object[][] ans = new Object[orders.size()][];
////            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//
//            int i = 0;
//            for (Order order : orders) {
//                String worker = "";
//
//                if (d != null) finishDate = Converter.getInstance().dateToStr(d);
//
//                ans[i] = new Object[]{order.getClient(), worker, order.getRegistrationNumber(),
//                        Converter.getInstance().dateToStr(order.getRealStartDate()), finishDate,
//                        order.getVehicleModel().getMark(), order.getVehicleModel(),
//                        order.getCurrentStatus()};
//                i++;
//            }
//
//            return ans;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    Object getObject(int row) {
        return null;
    }

    @Override
    boolean canAdd() {
        return false;
    }

    @Override
    boolean canEdit() {
        return false;
    }

    @Override
    boolean canDelete() {
        return false;
    }

    @Override
    void performDelete(int row) {

    }

    @Override
    public String toString() {
        return "Зап. части";
    }
}
