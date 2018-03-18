package me.svistoplyas.graphics.views;

import me.svistoplyas.graphics.MainFrame;

public class ProfitView extends AbstractView {

    public ProfitView(MainFrame _mainFrame) {
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
        return "Расходы и доходы";
    }
}
