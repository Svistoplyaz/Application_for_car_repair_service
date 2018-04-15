package net.web_kot.teamdev.db.entities;

import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SqlResolve")
public class SparePart extends AbstractEntity {
    
    public enum Unit {
        pieces("шт"), liters("л"), kilograms("кг"), meters("м"), squareMeters("м²"), cubicMeters("м³");
        
        private String value;
        Unit(String s) { value = s; }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    private String name;
    private int quantity;
    private Unit unit;
    private boolean universal;
    
    public SparePart(Model model, String name, Unit unit, boolean universal) {
        this(model, -1, name, 0, unit.ordinal(), universal);
    }
    
    @SelectConstructor
    public SparePart(Model model, int id, String name, int quantity, int unit, boolean universal) {
        super(model, "Spare_part", "PK_Spare_part");
        this.id = id; this.name = name; this.quantity = quantity; 
        this.unit = Unit.values()[unit]; this.universal = universal;
    }

    public SparePart save() throws Exception {
        if(id == -1)
            id = model.db().insert(
                    "INSERT INTO Spare_part (Name, Quantity, Unit, Universal) VALUES (%s, %d, %d, %d)",
                    name, quantity, unit.ordinal(), universal
            );
        else
            model.db().update(
                    "UPDATE Spare_part SET Name = %s, Quantity = %d, Unit = %d, Universal = %d WHERE PK_Spare_part = %d",
                    name, quantity, unit.ordinal(), universal, id
            );
        return this;
    }
    
    public String getName() {
        return name;
    }
    
    public SparePart setName(String name) {
        this.name = name;
        return this;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public int getRealQuantity() throws Exception {
        return getRealQuantity(-1);
    }
    
    public int getRealQuantity(int ignored) throws Exception {
        ResultSet result = model.db().select(
                "SELECT SUM(Quantity) FROM Spare_part_Order WHERE PK_Spare_part = %d " +
                        "AND PK_Order <> %d AND PK_Order NOT IN " +
                        "(SELECT o.PK_Order FROM `Order` o, Status s WHERE s.Type = %d AND s.PK_Order = o.PK_Order)",
                id, ignored, Order.Status.CANCELED.ordinal()
        );
        result.next();
        return quantity - result.getInt(1);
    }
    
    public String getBeautifulQuantity() {
        return Converter.getInstance().beautifulQuantity(quantity, getUnit());
    }
    
    @Deprecated
    public SparePart setQuantity(int q) {
        this.quantity = q; 
        return this;
    }
    
    public void purchase(int quantity, int price) throws Exception {
        model.db().insert(
                "INSERT INTO Purchase (PK_Spare_part, Quantity, Price, Date) VALUES (%d, %d, %d, %d)",
                id, quantity, price, System.currentTimeMillis()
        );
    }
    
    public Unit getUnit() {
        return unit;
    }
    
    public SparePart setUnit(Unit unit) {
        this.unit = unit;
        return this;
    }
    
    public boolean isUniversal() {
        return universal;
    }
    
    public SparePart setUniversal(boolean value) {
        this.universal = value;
        return this;
    }

    public int getPrice() throws SQLException {
        return getPrice(new Date());
    }

    public int getPrice(Date date) throws SQLException {
        ResultSet result = model.db().select(
                "SELECT Price FROM Spare_part_price WHERE PK_SparePart = %d AND `Date` < %d ORDER BY `Date` DESC",
                id, date.getTime()
        );
        if(!result.next()) return -1;
        return result.getInt(1);
    }
    
    public SparePart setPrice(int price) throws Exception {
        model.db().insert(
                "INSERT INTO Spare_part_price(`Date`, Price, PK_SparePart) VALUES (%d, %d, %d)",
                System.currentTimeMillis(), price, id
        );
        return this;
    }
    
    public void addCompatibleModel(VehicleModel vehicleModel) throws Exception {
        model.db().insert(
                "INSERT INTO Model_Spare_part(PK_Spare_part, PK_Model) VALUES (%d, %d)",
                id, vehicleModel.getId()
        );
    }
    
    public void removeCompatibleModel(VehicleModel vehicleModel) throws Exception {
        model.db().exec(
                "DELETE FROM Model_Spare_part WHERE PK_Spare_part = %d AND PK_Model = %d",
                id, vehicleModel.getId()
        );
    }
    
    public List<VehicleModel> getCompatibleModels() throws Exception {
        return model.getList(VehicleModel.class, model.db().formatQuery(
                "SELECT m.PK_Model, m.Name, m.Year, m.PK_Mark FROM Model m, Model_Spare_part l " +
                        "WHERE m.PK_Model = l.PK_Model AND l.PK_Spare_part = %d", id
        ));
    }
    
    public void setCompatibleModels(List<VehicleModel> models) throws Exception {
        List<VehicleModel> existing = getCompatibleModels();
        for(VehicleModel m : models)
            if(!existing.contains(m)) addCompatibleModel(m);
        for(VehicleModel m : existing)
            if(!models.contains(m)) removeCompatibleModel(m);
    }
    
    public boolean isCompatibleWith(VehicleModel model) throws Exception {
        return universal || getCompatibleModels().contains(model);
    }
    
    @Override
    public String toString() {
        return name + " (" + unit + ", " + universal + ")";
    }
    
}
