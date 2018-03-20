package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("SqlResolve")
public abstract class AbstractEntity {
    
    protected final Model model;
    protected int id;
    
    private String tableName, primaryKey;
    
    public AbstractEntity(Model model, String table, String key) {
        this.model = model;
        tableName = table; primaryKey = key;
    }
    
    public int getId() {
        return id;
    }
    
    public void delete() throws Exception {
        model.db().exec("DELETE FROM %s WHERE %s = %d", tableName, primaryKey, id);
    }

    @Target(value=ElementType.CONSTRUCTOR)
    @Retention(value=RetentionPolicy.RUNTIME)
    public @interface SelectConstructor { }
    
}
