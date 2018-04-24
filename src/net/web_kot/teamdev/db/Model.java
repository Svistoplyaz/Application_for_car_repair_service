package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;
import net.web_kot.teamdev.db.wrappers.Profit;
import net.web_kot.teamdev.db.wrappers.SparePartReservation;
import org.apache.commons.lang3.time.DateUtils;
import org.intellij.lang.annotations.Language;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.*;

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
    public <T> List<T> getList(Class<T> clazz, @Language("SQL")String sql) throws Exception {
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
        else if(clazz == boolean.class)
            value = result.getInt(index) != 0;
        else throw new Exception("Unknown field type " + clazz.getCanonicalName());
        
        if(result.wasNull()) return null;
        return value;
    }
    
    private <T extends AbstractEntity> T getById(Class<T> clazz, @Language("SQL")String sql) throws Exception {
        return getList(clazz, sql).get(0);
    }
    
    /* Users */
    
    public boolean checkPassword(int id, String password) throws Exception {
        ResultSet result = db.select("SELECT Password FROM Users WHERE PK_User = %d", id);
        result.next();
        return password.equals(result.getString(1));
    }
    
    public void setPassword(int id, String password) throws Exception {
        db.update("UPDATE Users SET Password = %s WHERE PK_User = %d", password, id);
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
    
    public Order createOrder(Client client, Staff staff, VehicleModel vehicle, Date start) {
        return new Order(this, client, staff, vehicle, start);
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
                        "SELECT * FROM `Order` WHERE ((%d <= Start_date AND Start_date <= %d) " +
                                "OR (%d <= Finish_date AND Finish_date <= %d)) AND PK_Order NOT IN " +
                                "(SELECT o.PK_Order FROM `Order` o, Status s WHERE s.Type = %d AND s.PK_Order = o.PK_Order)",
                        start, end, start, end, Order.Status.CANCELED.ordinal()
                )
        );
    }
    
    /* Position */
    
    public Position createPosition(String name) {
        return new Position(this, name);
    }
    
    public Position getPositionById(int id) throws Exception {
        return getById(Position.class, db.formatQuery("SELECT * FROM Position WHERE PK_Position = %d", id));
    }
    
    public List<Position> getPositions() throws Exception {
        return getList(Position.class, "SELECT * FROM Position");
    }
    
    /* Staff */
    
    public Staff createStaff(Position position, String name, String phone, Date birthday) {
        return new Staff(this, position, name, phone, birthday);
    }
    
    public Staff getStaffById(int id) throws Exception {
        return getById(Staff.class, db.formatQuery("SELECT * FROM Staff WHERE PK_Staff = %d", id));
    }
    
    public List<Staff> getStaff() throws Exception {
        return getList(Staff.class, "SELECT * FROM Staff");
    }
    
    /* Spare parts */
    
    public SparePart createSparePart(String name, SparePart.Unit unit, boolean universal) {
        return new SparePart(this, name, unit, universal);
    }
    
    public SparePart getSparePartById(int id) throws Exception {
        return getById(SparePart.class, db.formatQuery("SELECT * FROM Spare_part WHERE PK_Spare_part = %d", id));
    }
    
    public List<SparePart> getSpareParts() throws Exception {
        List<SparePart> list = getList(SparePart.class, "SELECT * FROM Spare_part");
        ArrayList<SparePart> result = new ArrayList<>();
        for(SparePart part : list) 
            if(!part.isHidden()) result.add(part);
        return result;
    }
    
    public List<SparePart> getCompatibleSpareParts(VehicleModel model) throws Exception {
        Set<SparePart> set = new HashSet<>();
        set.addAll(getList(SparePart.class, 
                "SELECT * FROM Spare_part WHERE Universal <> 0"
        ));
        set.addAll(getList(SparePart.class, db.formatQuery(
                "SELECT p.PK_Spare_part, p.Name, p.Hidden, p.Unit, p.Universal FROM Spare_part p, Model_Spare_part m" +
                        " WHERE p.PK_Spare_part = m.PK_Spare_part AND m.PK_Model = %d", model.getId()
        )));
        return new ArrayList<>(set);
    }
    
    public List<SparePartReservation> getReservation(Date from, Date to) throws Exception {
        return getList(SparePartReservation.class, db.formatQuery(
                "SELECT * FROM Reservation WHERE %d < Date AND Date < %d",
                from.getTime(), to.getTime()
        ));
    }
    
    /* Statistics */
    
    public Object[][] getServicesStat(Date from, Date to) throws Exception {
        ResultSet result = db.select(
                "SELECT s.Name, COUNT(*) as cnt FROM Service s, Order_Service os WHERE s.PK_Service = os.PK_Service AND " +
                        "%d < os.Date AND os.Date < %d AND os.PK_Order IN (SELECT o.PK_Order FROM " +
                        "`Order` o, Status s WHERE s.Type = %d AND s.PK_Order = o.PK_Order) " +
                        "GROUP BY s.Name ORDER BY cnt DESC",
                from.getTime(), to.getTime(), Order.Status.FINISHED.ordinal()
        );

        ArrayList<Object[]> list = new ArrayList<>();
        while(result.next()) list.add(new Object[] { result.getString(1), result.getInt(2) });
        
        Object[][] ret = new Object[list.size()][];
        for(int i = 0; i < ret.length; i++) ret[i] = list.get(i);
        
        return ret;
    }
    
    public Profit getProfitStats(Date from, Date to) throws Exception {
        ResultSet result = db.select(
                "SELECT * FROM Purchase s WHERE %d < Date AND Date < %d", 
                from.getTime(), to.getTime()
        );
        
        ArrayList<Object[]> data = new ArrayList<>();
        while(result.next()) data.add(new Object[] { result.getInt(2), result.getInt(3), result.getInt(4) });
        
        List<Order> orders = getList(Order.class, db.formatQuery(
                "SELECT * FROM `Order` WHERE PK_Order IN (SELECT o.PK_Order FROM `Order` o, Status s WHERE " +
                        "s.Type = %d AND s.PK_Order = o.PK_Order AND %d < s.Date_Time AND s.Date_Time < %d)",
                Order.Status.FINISHED.ordinal(), from.getTime(), to.getTime()
        ));
        
        return new Profit(this, data, orders);
    }
    
}
