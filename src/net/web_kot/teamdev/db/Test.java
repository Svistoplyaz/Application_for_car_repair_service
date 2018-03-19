package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.Client;

import java.io.File;
import java.util.List;

public class Test {
    
    public static void main(String[] args) throws Exception {
        Database db = new Database(new File("data.db"));
        Model model = db.getModel();
        
        List<Client> clients = model.getClients();
        if(clients.size() == 0) {
            model.createClient("Test").setPhone("88001234567").save();
            model.createClient("Ivan").save();
            
            clients = model.getClients();
        }
        for(Client c : clients) System.out.println(c);
        
        db.close();
    }
    
}
