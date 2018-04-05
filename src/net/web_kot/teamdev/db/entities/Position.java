package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

import java.awt.*;

@SuppressWarnings("SqlResolve")
public class Position extends AbstractEntity {

    private String name;

    public Position(Model model, String name) {
        this(model, -1, name);
    }

    @SelectConstructor
    public Position(Model model, int id, String name) {
        super(model, "Position", "PK_Position");
        this.id = id;
        this.name = name;
    }

    public Position save() throws Exception {
        if (id == -1)
            id = model.db().insert(
                    "INSERT INTO Position (Name) VALUES (%s)",
                    name
            );
        else
            model.db().update(
                    "UPDATE Position SET Name = %s WHERE PK_Position = %d",
                    name, id
            );
        return this;
    }

    public String getName() {
        return name;
    }

    public Position setName(String n) {
        name = n;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }

}
