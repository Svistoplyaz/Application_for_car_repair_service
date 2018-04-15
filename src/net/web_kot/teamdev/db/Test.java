package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;
import net.web_kot.teamdev.db.wrappers.OrderSpareParts;

import java.io.File;
import java.util.Date;

public class Test {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        File f = new File("test.db");
        f.delete();
        
        Database db = new Database(f).setDebug(false);
        Model model = db.getModel();
        
        SparePart part = model.createSparePart("Воздушный фильтр", SparePart.Unit.liters, false).save().setPrice(100);
        SparePart other = model.createSparePart("Масло", SparePart.Unit.pieces, true).save().setPrice(200);
        
        Position pos = model.createPosition("Механик").save();
        Staff staff = model.createStaff(pos, "Vasya", "123456", new Date()).save();
        
        Client client = model.createClient("Client").save();
        
        Mark mark = model.createMark("Tesla").save();
        VehicleModel vehicle = model.createVehicleModel(mark, "Model S", 2018).save();
        
        Order order = model.createOrder(client, staff, vehicle, new Date()).save();
        
        /* ----- */

        OrderSpareParts wrapper = order.getSpareParts();
        wrapper.addSparePart(part, 200);
        wrapper.addSparePart(other, 312);
        order.setSpareParts(wrapper);
        
        wrapper = order.getSpareParts();
        wrapper.addSparePart(other, 3000);
        order.setSpareParts(wrapper);
        
        part.setPrice(300);
        Thread.sleep(100);
        
        wrapper = order.getSpareParts();
        wrapper.removeSparePart(part, 20);
        wrapper.addSparePart(part, 100);
        order.setSpareParts(wrapper);
        
        order.getSpareParts().print();
        
        /* ----- */
        
        db.close();
    }
    
}