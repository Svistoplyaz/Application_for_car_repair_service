package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Test {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        File f = new File("test.db");
        f.delete();
        
        Database db = new Database(f).setDebug(true);
        Model model = db.getModel();
        
        SparePart part = model.createSparePart("Воздушный фильтр", SparePart.Unit.liters, false).save();
        SparePart other = model.createSparePart("Масло", SparePart.Unit.pieces, true).save();
        
        for(SparePart p : model.getSpareParts()) System.out.println(p);
        
        part.setQuantity(1000).save();
        System.out.println(part.getQuantity());
        System.out.println();
        
        part.setPrice(100);
        Thread.sleep(1000);
        
        part.setPrice(200);
        long time = System.currentTimeMillis();
        Thread.sleep(1000);
        
        part.setPrice(300);
        Thread.sleep(1000);
        
        System.out.println(part.getPrice());
        System.out.println(part.getPrice(new Date(time + 500)));
        System.out.println();
        
        Mark mark = model.createMark("Toyota").save();
        VehicleModel model1 = model.createVehicleModel(mark, "Corolla", 2012).save();
        VehicleModel model2 = model.createVehicleModel(mark, "Corolla", 2014).save();
        VehicleModel model3 = model.createVehicleModel(mark, "Mark II", 1998).save();
        
        part.addCompatibleModel(model1);
        part.addCompatibleModel(model2);
        part.addCompatibleModel(model3);
        part.removeCompatibleModel(model2);
        
        other.addCompatibleModel(model2);
        
        for(VehicleModel m : part.getCompatibleModels()) System.out.println(m);
        System.out.println();
        
        List<VehicleModel> list = part.getCompatibleModels();
        list.add(model2);
        list.remove(model3);
        part.setCompatibleModels(list);

        for(VehicleModel m : part.getCompatibleModels()) System.out.println(m);
        System.out.println();
        
        for(VehicleModel m : model.getVehiclesModels())
            System.out.println(part.isCompatibleWith(m) + " " + other.isCompatibleWith(m));
        
        db.close();
    }
    
}