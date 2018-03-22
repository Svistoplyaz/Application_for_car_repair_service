package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SqlResolve")
public class Order extends AbstractEntity {
    
    public enum Status {
        PRELIMINARY("Предварительный"), CONFIRMED("Подтвержденный"), CANCELED("Отмененный"),
        INWORK("В работе"), FINISHED("Завершенный"), CLOSED("Закрытый");
        
        private final String name;
        public String getName() { return name; }
        
        @Override
        public String toString() { return name; }
        
        Status(String n) { name = n; }
    }
    
    private static final DateFormat FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    
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
        if(id == -1) {
            id = model.db().insert(
                    "INSERT INTO `Order` (PK_Clients, Registration_number, Start_date, Finish_date, PK_Model, " +
                            "Finish_cost) VALUES (%d, %s, %d, %s, %d, %d)",
                    idClient, number, start, finish, idModel, finishCost
            );
            setStatus(Status.PRELIMINARY);
        } else
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
    
    public void setStatus(Status status) throws Exception {
        model.db().insert(
                "INSERT INTO Status (Type, Date_Time, PK_Order) VALUES (%d, %d, %d)",
                status.ordinal(), System.currentTimeMillis(), id
        );
    }
    
    public ArrayList<Pair<String, String>> getHistory() throws Exception {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        
        ResultSet result = model.db().select("SELECT * FROM Status WHERE PK_Order = %d ORDER BY Date_Time ASC", id);
        while(result.next()) {
            Pair<Status, Date> pair = parseStatus(result);
            list.add(Pair.of(pair.getLeft().getName(), FORMAT.format(pair.getRight())));
        }
        
        return list;
    }
    
    public Status getCurrentStatus() throws Exception {
        ResultSet result = model.db().select("SELECT * FROM Status WHERE PK_Order = %d ORDER BY Date_Time DESC", id);
        result.next();
        return parseStatus(result).getLeft();
    }
    
    private Pair<Status, Date> parseStatus(ResultSet result) throws Exception {
        return Pair.of(Status.values()[result.getInt(2)], new Date(result.getLong(3)));
    }
    
    public Status[] getPossibleStatuses() throws Exception {
        switch(getCurrentStatus()) {
            case PRELIMINARY:
                return new Status[] { Status.CONFIRMED, Status.CANCELED };
            case CONFIRMED:
                return new Status[] { Status.INWORK };
            case INWORK:
                return new Status[] { Status.FINISHED };
            case FINISHED:
                return new Status[] { Status.CLOSED };
            case CLOSED:
            case CANCELED:
                return new Status[0];
        }
        throw new RuntimeException();
    }
    
}
