package me.svistoplyas.graphics.views;

import me.svistoplyas.graphics.MainFrame;
import me.svistoplyas.graphics.editForms.AbstractEdit;
import me.svistoplyas.graphics.editForms.ClientForm;

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
    public AbstractEdit getEdit(boolean b, Object o) {
        return new ClientForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Домашняя страница";
    }
}
