package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.SpareForm;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.SparePart;
import net.web_kot.teamdev.db.entities.VehicleModel;

import java.util.List;

public class SpareView extends AbstractView {

    public SpareView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Название", "Цена", "Количество", "Единица измерения", "Совместимые модели"};
    }

    @Override
    Object[][] getData() {

        try {
            List<SparePart> spareParts = mainFrame.model.getSpareParts();
            Object[][] ans = new Object[spareParts.size()][];

            int i = 0;
            for (SparePart sparePart : spareParts) {
                String modelStr;
                if (sparePart.isUniversal())
                    modelStr = "Универсальная";
                else {
                    List<VehicleModel> models = sparePart.getCompatibleModels();
                    StringBuilder modelList = new StringBuilder();
                    for (VehicleModel model : models)
                        modelList.append(", ").append(model);
                    modelStr = modelList.toString().substring(2);
                }

                ans[i] = new Object[]{sparePart.getName(), Converter.getInstance().convertPriceToStr(sparePart.getPrice()),
                        sparePart.getBeautifulQuantity(), sparePart.getUnit(), modelStr};
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
            return mainFrame.model.getSpareParts().get(row);
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
    void performDelete(int row) {
        try {
            mainFrame.model.getSpareParts().get(row).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new SpareForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Зап. части";
    }
}
