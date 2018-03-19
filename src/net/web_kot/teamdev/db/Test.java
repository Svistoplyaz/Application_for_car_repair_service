package net.web_kot.teamdev.db;

import java.io.File;

public class Test {
    
    public static void main(String[] args) throws Exception {
        Database db = new Database(new File("data.db"));
        db.close();
    }
    
}
