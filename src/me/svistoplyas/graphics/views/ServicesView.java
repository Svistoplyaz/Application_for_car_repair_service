package me.svistoplyas.graphics.views;

import me.svistoplyas.graphics.MainFrame;
import me.svistoplyas.graphics.editForms.AbstractEdit;
import me.svistoplyas.graphics.editForms.ServiceForm;

public class ServicesView extends AbstractView {

    public ServicesView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Название"};
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
    public AbstractEdit getEdit(boolean b, Object o) {
        return new ServiceForm(mainFrame, b, o);
    }

    @Override
    public String toString(){
        return "Услуги";
    }
}
