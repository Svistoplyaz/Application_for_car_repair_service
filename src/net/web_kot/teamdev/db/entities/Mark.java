package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

@SuppressWarnings("SqlResolve")
public class Mark extends AbstractEntity {
    
    private int id;
    private String name;
    
    public Mark(Model model, String name) {
        this(model, -1, name);
    }
    
    public Mark(Model model, int id, String name) {
        super(model);
        this.id = id; this.name = name;
    }

    public Mark save() throws Exception {
        if(id == -1)
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
    
    public int getId() {
        return id;
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
        return String.format("%d: %s", id, name);
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Mark)) return false;
        Mark other = (Mark)o;
        
        return id == other.id;
    }
    
}
