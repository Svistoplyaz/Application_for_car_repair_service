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
        
        SparePart other = model.createSparePart("Воздушный фильтр", SparePart.Unit.pieces, true).save().setPrice(200);
        other.purchase(100, 127);
        
        db.close();
    }
    
}