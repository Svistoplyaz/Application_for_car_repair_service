package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

@SuppressWarnings("SqlResolve")
public class VehicleModel extends AbstractEntity {
    
    private int markId;
    private String name;
    private int year;
    
    private String cachedMarkName = null;
    
    public VehicleModel(Model model, Mark mark, String name, int year) {
        this(model, -1, name, year, mark.getId());
    }

    @SelectConstructor
    public VehicleModel(Model model, int id, String name, int year, int markId) {
        super(model, "Model", "PK_Model");
        this.id = id; this.markId = markId; this.name = name; this.year = year;
    }

    public VehicleModel save() throws Exception {
        if(id == -1)
            id = model.db().insert(
                    "INSERT INTO Model (Name, Year, PK_Mark) VALUES (%s, %d, %d)", 
                    name, year, markId
            );
        else
            model.db().update(
                    "UPDATE Model SET Name = %s, Year = %d, PK_Mark = %d WHERE PK_Model = %d", 
                    name, year, markId, id
            );
        return this;
    }
    
    public Mark getMark() throws Exception {
        return model.getMarkById(markId);
    }
    
    public String getName() {
        return name;
    }
    
    public VehicleModel setName(String name) {
        this.name = name;
        return this;
    }
    
    public int getYear() {
        return year;
    }
    
    public VehicleModel setYear(int year) {
        this.year = year;
        return this;
    }
    
    @Override
    public String toString() {
        if(cachedMarkName == null)
            try {
                cachedMarkName = model.getMarkById(markId).getName();
            } catch(Exception e) {
                e.printStackTrace();
            }
        return String.format("%s %s %d", cachedMarkName, name, year);
    }
    
}
