package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.ServiceForm;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.Service;

import java.util.List;

public class ServicesView extends AbstractView {

    public ServicesView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Название", "Стоимость"};
    }

    @Override
    Object[][] getData() {
        try {
            List<Service> services = mainFrame.model.getServices();
            Object[][] ans = new Object[services.size()][];
            int i = 0;
            for (Service service : services) {
//                int price = service.getPrice();
//                String priceStr = price / 100 + " руб. " + price % 100 + " коп.";
                ans[i] = new Object[]{service.getName(), Converter.getInstance().convertPriceToStr(service.getPrice())};
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
            return mainFrame.model.getServices().get(row);
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
        mainFrame.model.getServices().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new ServiceForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Услуги";
    }
}
