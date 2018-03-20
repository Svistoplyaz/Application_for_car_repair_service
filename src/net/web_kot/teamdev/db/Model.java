package net.web_kot.teamdev.db;

import net.web_kot.teamdev.db.entities.*;
import org.intellij.lang.annotations.Language;

import java.lang.reflect.Constructor;
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
    
    private <T extends AbstractEntity> List<T> getList(Class<T> clazz, @Language("SQL")String sql, 
                                                       Class<?>... arguments) throws Exception {
        ArrayList<T> list = new ArrayList<>(1);
        Constructor<T> constructor = clazz.getConstructor(prepareArgs(arguments));
        
        ResultSet result = db.select(sql);
        while(result.next()) {
            Object[] values = new Object[arguments.length + 1];
            values[0] = this;
            
            for(int i = 0; i < arguments.length; i++)
                if(arguments[i] == String.class)
                    values[i + 1] = result.getString(i + 1);
                else if(arguments[i] == int.class)
                    values[i + 1] = result.getInt(i + 1);
                
            list.add(constructor.newInstance(values));
        }
        
        return list;
    }
    
    private Class<?>[] prepareArgs(Class<?>[] arguments) {
        Class<?>[] args = new Class<?>[arguments.length + 1];
        System.arraycopy(arguments, 0, args, 1, arguments.length);
        
        args[0] = Model.class;
        return args;
    }
    
    private <T extends AbstractEntity> T getById(Class<T> clazz, @Language("SQL")String sql, 
                                                 Class<?>... arguments) throws Exception {
        return getList(clazz, sql, arguments).get(0);
    }
    
    /* Clients */
    
    public Client createClient(String name) {
        return new Client(this, name);
    }
    
    public List<Client> getClients() throws Exception {
        return getList(
                Client.class,
                "SELECT * FROM Clients",
                int.class, String.class, String.class
        );
    }
    
    /* Mark */
    
    public Mark createMark(String name) {
        return new Mark(this, name);
    }
    
    public Mark getMarkById(int id) throws Exception {
        return getById(
                Mark.class,
                db.formatQuery("SELECT * FROM Mark WHERE PK_Mark = %d", id),
                int.class, String.class
        );
    }
    
    public List<Mark> getMarks() throws Exception {
        return getList(
                Mark.class,
                "SELECT * FROM Mark",
                int.class, String.class
        );
    }
    
    /* Vehicle model */
    
    public VehicleModel createVehicleModel(Mark mark, String name, int year) {
        return new VehicleModel(this, mark, name, year);
    }
    
    public List<VehicleModel> getVehiclesModels() throws Exception {
        return getList(
                VehicleModel.class,
                "SELECT * FROM Model",
                int.class, String.class, int.class, int.class
        );
    }
    
}
