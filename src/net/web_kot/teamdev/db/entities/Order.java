package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

import java.util.Date;
import java.util.List;

@SuppressWarnings("SqlResolve")
public class Order extends AbstractEntity {
    
    private int idClient, idModel;
    private String number;
    private Long start, finish;
    private int finishCost;
    
    public Order(Model model, Client client, VehicleModel vehicle, Date start) {
        this(model, -1, client.getId(), null, start.getTime(), null, vehicle.getId(), -1);
    }
    
    @SelectConstructor
    public Order(Model model, int id, int idClient, String number, long start, Long finish, int idModel, int cost) {
        super(model, "Order", "PK_Order");
        this.id = id; this.idClient = idClient; this.number = number; 
        this.start = start; this.finish = finish; this.idModel = idModel; this.finishCost = cost;
    }

    public Order save() throws Exception {
        if(id == -1)
            id = model.db().insert(
                    "INSERT INTO `Order` (PK_Clients, Registration_number, Start_date, Finish_date, PK_Model, " +
                            "Finish_cost) VALUES (%d, %s, %d, %s, %d, %d)",
                    idClient, number, start, finish, idModel, finishCost
            );
        else
            model.db().update(
                    "UPDATE `Order` SET Registration_number = %s, Start_date = %d, Finish_date = %s, " +
                            "PK_Model = %d, Finish_cost = %d WHERE PK_Order = %d",
                    number, start, finish, idModel, finishCost, id
            );
        return this;
    }
    
    public Client getClient() throws Exception {
        return model.getClientById(idClient);
    }
    
    public VehicleModel getVehicleModel() throws Exception {
        return model.getVehicleModelById(idModel);
    }
    
    public Order setVehicleModel(VehicleModel vmodel) {
        idModel = vmodel.getId();
        return this;
    }
    
    public String getRegistrationNumber() {
        return number;
    }
    
    public Order setRegistrationNumber(String number) {
        this.number = number;
        return this;
    }
    
    public Date getStartDate() {
        return new Date(start);
    }
    
    public Order setStartDate(Date date) {
        start = date.getTime();
        return this;
    }
    
    public Date getFinishDate() {
        if(finish == null) return null;
        return new Date(finish);
    }
    
    public Order setFinishDate(Date date) {
        if(date == null) 
            finish = null;
        else
            finish = date.getTime();
        return this;
    }
    
    @Override
    public String toString() {
        try {
            return String.format("%d: (%s) [%s, %s]", id, getClient().toString(), getVehicleModel().toString(), number);
        } catch(Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Order)) return false;
        Order other = (Order)o;
        
        return id == other.id;
    }
    
    public void addService(Service service) throws Exception {
        model.db().insert(
                "INSERT INTO Order_Service (PK_Order, PK_Service, Date) VALUES (%d, %d, %d)",
                id, service.getId(), System.currentTimeMillis()
        );
    }
    
    public List<Service> getServices() throws Exception {
        return model.getList(Service.class, model.db().formatQuery(
                "SELECT s.PK_Service, s.Name FROM Service s, Order_Service os WHERE " +
                        "s.PK_Service = os.PK_Service AND os.PK_Order = %d", id)
        );
    }
    
    public void removeService(Service service) throws Exception {
        model.db().exec(
                "DELETE FROM Order_Service WHERE PK_Order = %d AND PK_Service = %d",
                id, service.getId()
        );
    }
    
    public void setServices(List<Service> services) throws Exception {
        List<Service> existing = getServices();
        for(Service s : services) 
            if(!existing.contains(s)) addService(s);
        for(Service s : existing)
            if(!services.contains(s)) removeService(s);
    }
    
}
