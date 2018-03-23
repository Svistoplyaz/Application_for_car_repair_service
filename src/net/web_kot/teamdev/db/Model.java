package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;
import org.apache.commons.lang3.time.DateUtils;
import org.intellij.lang.annotations.Language;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> List<T> getList(Class<T> clazz, @Language("SQL")String sql) throws Exception {
        Constructor<T> constructor = null;
        for(Constructor<?> c : clazz.getConstructors())
            if(c.isAnnotationPresent(AbstractEntity.SelectConstructor.class) && c.getDeclaringClass() == clazz) {
                constructor = (Constructor<T>) c;
                break;
            }
            
        if(constructor == null) 
            throw new Exception("Class " + clazz.getCanonicalName() + " doesn't contains @SelectConstructor");    
        
        ArrayList<T> list = new ArrayList<>(1);
        
        ResultSet result = db.select(sql);
        while(result.next()) {
            Object[] values = new Object[constructor.getParameterCount()];
            values[0] = this;
            
            Class<?>[] parameters = constructor.getParameterTypes();
            for(int i = 1; i < parameters.length; i++) values[i] = getParameter(result, parameters[i], i);
            
            list.add(constructor.newInstance(values));
        }
        
        return list;
    }
    
    private Object getParameter(ResultSet result, Class<?> clazz, int index) throws Exception {
        Object value;
        if(clazz == String.class)
            value = result.getString(index);
        else if(clazz == int.class || clazz == Integer.class)
            value = result.getInt(index);
        else if(clazz == long.class || clazz == Long.class)
            value = result.getLong(index);
        else throw new Exception("Unknown field type " + clazz.getCanonicalName());
        
        if(result.wasNull()) return null;
        return value;
    }
    
    private <T extends AbstractEntity> T getById(Class<T> clazz, @Language("SQL")String sql) throws Exception {
        return getList(clazz, sql).get(0);
    }
    
    /* Clients */
    
    public Client createClient(String name) {
        return new Client(this, name);
    }
    
    public Client getClientById(int id) throws Exception {
        return getById(Client.class, db.formatQuery("SELECT * FROM Clients WHERE PK_Clients = %d", id));
    }
    
    public List<Client> getClients() throws Exception {
        return getList(Client.class, "SELECT * FROM Clients");
    }
    
    /* Mark */
    
    public Mark createMark(String name) {
        return new Mark(this, name);
    }
    
    public Mark getMarkById(int id) throws Exception {
        return getById(Mark.class, db.formatQuery("SELECT * FROM Mark WHERE PK_Mark = %d", id));
    }
    
    public List<Mark> getMarks() throws Exception {
        return getList(Mark.class, "SELECT * FROM Mark");
    }
    
    /* Vehicle model */
    
    public VehicleModel createVehicleModel(Mark mark, String name, int year) {
        return new VehicleModel(this, mark, name, year);
    }

    public VehicleModel getVehicleModelById(int id) throws Exception {
        return getById(VehicleModel.class, db.formatQuery("SELECT * FROM Model WHERE PK_Model = %d", id));
    }
    
    public List<VehicleModel> getVehiclesModels() throws Exception {
        return getList(VehicleModel.class, "SELECT * FROM Model");
    }
    
    /* Service */
    
    public Service createService(String name) {
        return new Service(this, name);
    }
    
    public Service getServiceById(int id) throws Exception {
        return getById(Service.class, db.formatQuery("SELECT * FROM Service WHERE PK_Service = %d", id));
    }
    
    public List<Service> getServices() throws Exception {
        return getList(Service.class, "SELECT * FROM Service");
    }
    
    /* Order */
    
    public Order createOrder(Client client, VehicleModel vehicle, Date start) {
        return new Order(this, client, vehicle, start);
    }
    
    public List<Order> getOrders() throws Exception {
        return getList(Order.class, "SELECT * FROM `Order`");
    }
    
    public List<Order> getOrders(Date day) throws Exception {
        long start = DateUtils.truncate(day, Calendar.DATE).getTime();
        long end = DateUtils.addMilliseconds(DateUtils.ceiling(day, Calendar.DATE), -1).getTime();
        
        return getList(
                Order.class, 
                db.formatQuery(
                        "SELECT * FROM `Order` WHERE (%d <= Start_date AND Start_date <= %d) " +
                                "OR (%d <= Finish_date AND Finish_date <= %d)",
                        start, end, start, end
                )
        );
    }
    
}
