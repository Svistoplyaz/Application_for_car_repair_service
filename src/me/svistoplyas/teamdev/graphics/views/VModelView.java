package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.VModelForm;
import net.web_kot.teamdev.db.entities.VehicleModel;

import java.util.List;

public class VModelView extends AbstractView {
    public VModelView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Название"};
    }

    @Override
    Object[][] getData() {
        try {
            List<VehicleModel> vehiclesModels = mainFrame.model.getVehiclesModels();
            Object[][] ans = new Object[vehiclesModels.size()][];
            int i = 0;
            for (VehicleModel vehicleModel : vehiclesModels) {
                ans[i] = new Object[]{vehicleModel};
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
            return mainFrame.model.getVehiclesModels().get(row);
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
        mainFrame.model.getVehiclesModels().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new VModelForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Модели автомобилей";
    }
}
