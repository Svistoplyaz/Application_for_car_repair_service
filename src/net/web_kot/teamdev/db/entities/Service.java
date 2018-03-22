package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@SuppressWarnings("SqlResolve")
public class Service extends AbstractEntity {
    
    private String name;
    
    public Service(Model model, String name) {
        this(model, -1, name);
    }
    
    @SelectConstructor
    public Service(Model model, int id, String name) {
        super(model, "Service", "PK_Service");
        this.id = id; this.name = name;
    }

    public Service save() throws Exception {
        if(id == -1)
            id = model.db().insert(
                    "INSERT INTO Service (Name) VALUES (%s)",
                    name
            );
        else
            model.db().update(
                    "UPDATE Service SET Name = %s WHERE PK_Service = %d",
                    name, id
            );
        return this;
    }
    
    public String getName() {
        return name;
    }
    
    public Service setName(String name) {
        this.name = name;
        return this;
    }
    
    public int getPrice() throws SQLException {
        return getPrice(new Date());
    }
    
    public int getPrice(Date date) throws SQLException {
        ResultSet result = model.db().select(
                "SELECT Price FROM Service_price WHERE PK_Service = %d AND `Date` < %d ORDER BY `Date` DESC",
                id, date.getTime()
        );
        if(!result.next()) return -1;
        return result.getInt(1);
    }
    
    public int getPriceForOrder(Order order) throws Exception {
        if(order == null) return getPrice();
        ResultSet result = model.db().select(
                "SELECT Date FROM Order_Service WHERE PK_Order = %d AND PK_Service = %d",
                order.getId(), id
        );
        
        if(!result.next()) return getPrice();
        return getPrice(new Date(result.getLong(1)));
    }
    
    public Service setPrice(int price) throws Exception {
        model.db().insert(
                "INSERT INTO Service_price(`Date`, Price, PK_Service) VALUES (%d, %d, %d)",
                System.currentTimeMillis(), price, id
        );
        return this;
    }
    
    @Override
    public String toString() {
        try {
            return String.format("%d: %s (%d)", id, name, getPrice());
        } catch(Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Service)) return false;
        Service other = (Service)o;
        
        return id == other.id;
    }
    
}
