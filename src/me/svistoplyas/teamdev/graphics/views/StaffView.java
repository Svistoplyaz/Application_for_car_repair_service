package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.StaffForm;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.Staff;

import java.util.List;

public class StaffView extends AbstractView {

    public StaffView(MainFrame _mainFrame) {
        super(_mainFrame, false);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"ФИО", "Телефон", "Дата рождения", "Должность"};
    }

    @Override
    Object[][] getData() {
        try {
            List<Staff> staff = mainFrame.model.getStaff();
            Object[][] ans = new Object[staff.size()][];
            int i = 0;
            for (Staff person : staff) {
                ans[i] = new Object[]{person.getName(), person.getPhone(), Converter.getInstance().dateToStr(person.getBirthday()), person.getPosition().getName()};
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
            return mainFrame.model.getStaff().get(row);
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
        mainFrame.model.getStaff().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new StaffForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Персонал";
    }
}
