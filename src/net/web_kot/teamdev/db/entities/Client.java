package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

@SuppressWarnings("SqlResolve")
public class Client extends AbstractEntity {
    
    private int id = -1;
    private String name, phone = null;
    
    public Client(Model model, String name) {
        super(model);
        this.name = name;
    }
    
    public Client(Model model, int id, String name, String phone) {
        super(model);
        this.id = id; this.name = name; this.phone = phone;
    }
    
    public Client save() throws Exception {
        if(id == -1)
            id = model.db().insert(
                    "INSERT INTO Clients (Name, Phone) VALUES (%s, %s)", 
                    name, phone
            );
        else
            model.db().update(
                    "UPDATE Clients SET Name = %s, Phone = %s WHERE PK_Clients = %d", 
                    name, phone, id
            );
        return this;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Client setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public Client setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("%d: %s (%s)", id, name, phone);
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Client)) return false;
        Client other = (Client)o;
        
        return id == other.id; 
    }
    
}
