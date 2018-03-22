package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;
import org.apache.commons.lang3.tuple.Pair;

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
        
        Service s1 = model.createService("Замена масла").save().setPrice(100);
        Service s2 = model.createService("Проверка тормозов").save().setPrice(50);
        
        o.addService(s1);
        o.addService(s2);
        
        System.out.println(o.getPrice());
        
        Thread.sleep(100);
        s1.setPrice(500);
        
        System.out.println(o.getPrice());
        
        o.removeService(s1);
        o.addService(s1);
        
        System.out.println(o.getPrice());
        
        o.setStatus(Order.Status.CLOSED);
        System.out.println(o.getPrice());
        
        db.close();
    }
    
}