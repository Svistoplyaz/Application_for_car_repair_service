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
        
        Database db = new Database(f).setDebug(false);
        Model model = db.getModel();
        
        Client vasya = model.createClient("Vasya").setPhone("222222").save();
        
        Mark toyota = model.createMark("Toyota").save();
        VehicleModel corolla = model.createVehicleModel(toyota, "Corolla", 2012).save();
        
        Order o = model.createOrder(vasya, corolla, new Date()).setRegistrationNumber("А222МР777RUS").save();
        
        System.out.println(o.getCurrentStatus());
        
        Thread.sleep(100);
        o.setStatus(Order.Status.CONFIRMED);
        
        Thread.sleep(100);
        o.setStatus(Order.Status.CLOSED);
        
        System.out.println(o.getCurrentStatus());
        
        System.out.println();
        for(Pair<String, String> p : o.getHistory()) System.out.println(p);
        
        db.close();
    }
    
}