package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.ClientForm;

public class HomeView extends AbstractView {

    public HomeView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Клиент", "Регистрационный номер"};
    }

    @Override
    Object[][] getData() {
        return new Object[0][];
    }

    @Override
    Object getObject(int row) {
        return null;
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
        return new ClientForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Домашняя страница";
    }
}
