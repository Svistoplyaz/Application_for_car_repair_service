package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.PositionForm;
import me.svistoplyas.teamdev.graphics.editForms.ServiceForm;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.Position;
import net.web_kot.teamdev.db.entities.Service;

import java.util.List;

public class PositionView extends AbstractView {

    public PositionView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Название"};
    }

    @Override
    Object[][] getData() {
        try {
            List<Position> positions = mainFrame.model.getPositions();
            Object[][] ans = new Object[positions.size()][];
            int i = 0;
            for (Position position : positions) {
                ans[i] = new Object[]{position.getName()};
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
            return mainFrame.model.getPositions().get(row);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    boolean canAdd() {
        return mainFrame.type;
    }

    @Override
    boolean canEdit() {
        return mainFrame.type;
    }

    @Override
    boolean canDelete() {
        return mainFrame.type;
    }

    @Override
    void performDelete(int row) throws Exception {
        mainFrame.model.getPositions().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new PositionForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Должности";
    }
}
