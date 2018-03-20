package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.Mark;
import net.web_kot.teamdev.db.entities.VehicleModel;

import java.io.File;

public class Test {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        File f = new File("test.db");
        f.delete();
        
        Database db = new Database(f);
        Model model = db.getModel();
        
        Mark toyota = model.createMark("Toyota").save();
        Mark lada = model.createMark("Lada").save();
        
        model.createVehicleModel(toyota, "Corolla", 2012).save();
        model.createVehicleModel(toyota, "Land Cruiser", 2010).save();
        model.createVehicleModel(lada, "Priora", 2014).save();
        
        for(Mark m : model.getMarks()) System.out.println(m);
        System.out.println();
        for(VehicleModel m : model.getVehiclesModels()) System.out.println(m);
        
        db.close();
    }
    
}
