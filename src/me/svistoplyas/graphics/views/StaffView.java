package me.svistoplyas.graphics.views;

import me.svistoplyas.graphics.MainFrame;

public class StaffView extends AbstractView {

    public StaffView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[0];
    }

    @Override
    Object[][] getData() {
        return new Object[0][];
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
    public String toString(){
        return "Сотрудники";
    }
}
