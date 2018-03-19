package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

public abstract class AbstractEntity {
    
    protected final Model model;
    
    public AbstractEntity(Model model) {
        this.model = model;
    }
    
}
