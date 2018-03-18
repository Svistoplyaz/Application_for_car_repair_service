package me.svistoplyas.graphics.views;

import me.svistoplyas.graphics.MainFrame;

public class ClientsView extends AbstractView {

    public ClientsView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Имя", "Номер телефона"};
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
        return true;
    }

    @Override
    public String toString(){
        return "Клиенты";
    }
}
