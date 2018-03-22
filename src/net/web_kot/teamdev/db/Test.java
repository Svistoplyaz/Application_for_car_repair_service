package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;

import java.io.File;
import java.util.Date;

public class Test {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        File f = new File("test.db");
        f.delete();
        
        Database db = new Database(f).setDebug(true);
        Model model = db.getModel();
        
        Client vasya = model.createClient("Vasya").setPhone("222222").save();
        
        Mark toyota = model.createMark("Toyota").save();
        VehicleModel corolla = model.createVehicleModel(toyota, "Corolla", 2012).save();
        
        Order o = model.createOrder(vasya, corolla, new Date()).setRegistrationNumber("А222МР777RUS").save();
        
        for(Order order : model.getOrders(new Date())) System.out.println(order);
        
        db.close();
    }
    
}