package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class AbstractEntity {
    
    protected final Model model;
    
    public AbstractEntity(Model model) {
        this.model = model;
    }

    @Target(value=ElementType.CONSTRUCTOR)
    @Retention(value=RetentionPolicy.RUNTIME)
    public @interface SelectConstructor { }
    
}
