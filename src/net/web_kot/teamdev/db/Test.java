package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        
        Service service1 = model.createService("Замена масла").save();
        Service service2 = model.createService("Проверка тормозов").save().setPrice(12);
        
        ArrayList<Service> add = new ArrayList<>();
        add.add(service1);
        add.add(service2);
        o.setServices(add);
        
        List<Service> services = o.getServices();
        //services.remove(model.getServices().get(0));
        o.setServices(services);
        
        try {
            model.getServices().get(0).delete();
        } catch(Exception e) {
            System.out.println("!! " + e.getMessage());    
        }
        
        System.out.println(o);
        for(Service s : o.getServices()) System.out.println("> " + s);
        
        db.close();
    }
    
}