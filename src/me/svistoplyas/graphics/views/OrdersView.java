package me.svistoplyas.graphics.views;

import me.svistoplyas.graphics.MainFrame;

public class OrdersView extends AbstractView {

    public OrdersView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Клиент", "Работник", "Регистрационный номер", "Дата начала", "Дата конца", "Марка", "Модель"};
    }

    @Override
    Object[][] getData() {
        return new Object[0][];
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
    public String toString(){
        return "Заказы";
    }
}
