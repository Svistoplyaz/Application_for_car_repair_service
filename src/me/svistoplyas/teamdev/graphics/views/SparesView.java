package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.SparePart;
import net.web_kot.teamdev.db.entities.VehicleModel;

import java.util.List;

public class SparesView extends AbstractView {

    public SparesView(MainFrame _mainFrame) {
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
//            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            int i = 0;
            for (SparePart sparePart : spareParts) {
//                String worker = "";


//                if (d != null) finishDate = Converter.getInstance().dateToStr(d);
                List<VehicleModel> models = sparePart.getCompatibleModels();
                StringBuilder modelList = new StringBuilder();
                for(VehicleModel model : models)
                    modelList.append(", ").append(model);
                String modelStr = modelList.toString().substring(2);

                ans[i] = new Object[]{sparePart.getName(), sparePart.getPrice(), sparePart.getQuantity(),
                        sparePart.getUnit(), modelStr};
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
    void performDelete(int row) {
        try {
            mainFrame.model.getSpareParts().get(row).delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Зап. части";
    }
}
