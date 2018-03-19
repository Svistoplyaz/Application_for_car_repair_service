package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SqlResolve")
public class Model {
    
    private final Database db;
    
    public Model(Database database) {
        db = database;
    }
    
    public Database db() {
        return db;
    }
    
    /* Clients */
    
    public Client createClient(String name) {
        return new Client(this, name);
    }
    
    public List<Client> getClients() throws SQLException {
        ArrayList<Client> list = new ArrayList<>();
        
        ResultSet result = db.select("SELECT * FROM Clients");
        while(result.next()) list.add(new Client(this, result.getInt(1), result.getString(2), result.getString(3)));
        
        return list;
    }
    
}
