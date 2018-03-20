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
        
        Database db = new Database(f).setDebug(false);
        Model model = db.getModel();
        
        Client vasya = model.createClient("Vasya").setPhone("222222").save();
        
        Mark toyota = model.createMark("Toyota").save();
        VehicleModel corolla = model.createVehicleModel(toyota, "Corolla", 2012).save();
        
        Order o = model.createOrder(vasya, corolla, new Date()).setRegistrationNumber("А222МР777RUS").save();
        
        Service service1 = model.createService("Замена масла").save();
        Service service2 = model.createService("Проверка тормозов").save().setPrice(12);
        
        o.addService(service1);
        o.addService(service2);
        
        List<Service> services = o.getServices();
        services.remove(service1);
        o.setServices(services);
        
        System.out.println(o);
        for(Service s : o.getServices()) System.out.println("> " + s);
        
        db.close();
    }
    
}