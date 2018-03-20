package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.Service;

import java.io.File;
import java.util.Date;

public class Test {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        File f = new File("test.db");
        f.delete();
        
        Database db = new Database(f);
        Model model = db.getModel();
        
        Service s = model.createService("Покраска").save().setPrice(123);
        
        Service service = model.createService("Замена масла").save().setPrice(1);
        Thread.sleep(1000);
        service.setPrice(2);
        long time = System.currentTimeMillis();
        Thread.sleep(1000);
        service.setPrice(3);
        
        System.out.println(service.getPrice());
        System.out.println(service.getPrice(new Date(time + 100)));
        
        for(Service ss : model.getServices()) System.out.println(ss);
        
        db.close();
    }
    
}
