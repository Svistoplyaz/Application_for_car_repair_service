package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

@SuppressWarnings("SqlResolve")
public class Client extends AbstractEntity {

    private String name, phone = null;

    public Client(Model model, String name) {
        this(model, -1, name, null);
    }

    @SelectConstructor
    public Client(Model model, int id, String name, String phone) {
        super(model, "Clients", "PK_Clients");
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Client save() throws Exception {
        if (id == -1)
            id = model.db().insert(
                    "INSERT INTO Clients (Name, Phone) VALUES (%s, %s)",
                    name, phone
            );
        else
            model.db().update(
                    "UPDATE Clients SET Name = %s, Phone = %s WHERE PK_Clients = %d",
                    name, phone, id
            );
        return this;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Client setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, phone);
    }

}
