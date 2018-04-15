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
        
        SparePart part = model.createSparePart("Воздушный фильтр", SparePart.Unit.pieces, true).save().setPrice(200);
        SparePart other = model.createSparePart("Другая запчасть", SparePart.Unit.pieces, true).save().setPrice(200);
        
        for(SparePart p : model.getSpareParts()) System.out.println(">> " + p);
        System.out.println();
        
        part.setHidden(true).save();
        for(SparePart p : model.getSpareParts()) System.out.println(">> " + p);
        
        db.close();
    }
    
}