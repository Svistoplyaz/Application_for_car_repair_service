package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;

import java.io.File;
import java.util.Date;

public class Test {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        File f = new File("test.db");
        f.delete();
        
        Database db = new Database(f);
        Model model = db.getModel();
        
        Client vasya = model.createClient("Vasya").setPhone("222222").save();
        
        Mark toyota = model.createMark("Toyota").save();
        VehicleModel corolla = model.createVehicleModel(toyota, "Corolla", 2012).save();
        VehicleModel markII = model.createVehicleModel(toyota, "MarkII", 1993).save();
        
        model.createOrder(vasya, corolla, new Date()).setRegistrationNumber("А222МР777RUS").save();
        model.createOrder(vasya, markII, new Date()).setFinishDate(new Date()).save();
        
        for(Order o : model.getOrders()) System.out.println(o);
        
        db.close();
    }
    
}