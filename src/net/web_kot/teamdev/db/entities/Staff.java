package net.web_kot.teamdev.db.entities;

import net.web_kot.teamdev.db.Model;

import java.util.Date;

@SuppressWarnings("SqlResolve")
public class Staff extends AbstractEntity {
    
    private int idPosition;
    private String name, phone;
    private long birthday;
    
    public Staff(Model model, Position position, String name, String phone, Date birthday) {
        this(model, -1, position.getId(), name, phone, birthday.getTime());
    }
    
    @SelectConstructor
    public Staff(Model model, int id, int idPosition, String name, String phone, long birthday) {
        super(model, "Staff", "PK_Staff");
        this.id = id; this.idPosition = idPosition; this.name = name; this.phone = phone; this.birthday = birthday;
    }

    public Staff save() throws Exception {
        if(id == -1)
            id = model.db().insert(
                    "INSERT INTO Staff (PK_Position, Name, Phone, Birth_day) VALUES (%d, %s, %s, %d)",
                    idPosition, name, phone, birthday
            );
        else
            model.db().update(
                    "UPDATE Staff SET PK_Position = %d, Name = %s, Phone = %s, Birth_day = %d WHERE PK_Staff = %d",
                    idPosition, name, phone, birthday, id
            );
        return this;
    }
    
    public Position getPosition() throws Exception {
        return model.getPositionById(idPosition);
    }
    
    public Staff setPosition(Position position) {
        idPosition = position.id;
        return this;
    }
    
    public String getName() {
        return name;
    }
    
    public Staff setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public Staff setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public Date getBirthday() {
        return new Date(birthday);
    }
    
    public Staff setBirthday(Date date) {
        birthday = date.getTime();
        return this;
    }

    @Override
    public String toString() {
        try {
            return String.format("%s (%s)", name, getPosition().getName());
        } catch(Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Staff)) return false;
        Staff other = (Staff)o;
        
        return id == other.id;
    }
    
}
