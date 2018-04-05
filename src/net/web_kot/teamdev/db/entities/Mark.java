package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

import java.util.List;

@SuppressWarnings("SqlResolve")
public class Mark extends AbstractEntity {

    private String name;

    public Mark(Model model, String name) {
        this(model, -1, name);
    }

    @SelectConstructor
    public Mark(Model model, int id, String name) {
        super(model, "Mark", "PK_Mark");
        this.id = id;
        this.name = name;
    }

    public Mark save() throws Exception {
        if (id == -1)
            id = model.db().insert(
                    "INSERT INTO Mark (Name) VALUES (%s)",
                    name
            );
        else
            model.db().update(
                    "UPDATE Mark SET Name = %s WHERE PK_Mark = %d",
                    name, id
            );
        return this;
    }

    public String getName() {
        return name;
    }

    public Mark setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
    
    public List<VehicleModel> getVehiclesModels() throws Exception {
        return model.getList(
                VehicleModel.class,
                model.db().formatQuery("SELECT * FROM Model WHERE PK_Mark = %d", id)
        );
    }

}
