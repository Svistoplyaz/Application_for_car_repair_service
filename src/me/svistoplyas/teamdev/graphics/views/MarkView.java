package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.MarkForm;
import me.svistoplyas.teamdev.graphics.editForms.PositionForm;
import net.web_kot.teamdev.db.entities.Mark;

import java.util.List;

public class MarkView extends AbstractView {

    public MarkView(MainFrame _mainFrame) {
        super(_mainFrame, false);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Название"};
    }

    @Override
    Object[][] getData() {
        try {
            List<Mark> marks = mainFrame.model.getMarks();
            Object[][] ans = new Object[marks.size()][];
            int i = 0;
            for (Mark mark : marks) {
                ans[i] = new Object[]{mark.getName()};
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
            return mainFrame.model.getMarks().get(row);
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
        return true;
    }

    @Override
    void performDelete(int row) throws Exception {
        mainFrame.model.getMarks().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new MarkForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Марки";
    }
}

