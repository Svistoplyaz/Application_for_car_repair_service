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
        
        Position pos = model.createPosition("Механик").save();
        Staff staff = model.createStaff(pos, "Vasya", "123456", new Date()).save();
        
        for(Staff s : model.getStaff()) System.out.println(s);
        System.out.println();
        
        Client client = model.createClient("Client").save();
        
        Mark mark = model.createMark("Tesla").save();
        VehicleModel vehicle = model.createVehicleModel(mark, "Model S", 2018).save();
        
        model.createOrder(client, vehicle, new Date()).setResponsible(staff).save();
        
        for(Order o : model.getOrders()) System.out.println(o + " / " + o.getResponsible());
        
        db.close();
    }
    
}