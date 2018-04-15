package net.web_kot.teamdev.db.wrappers;

import net.web_kot.teamdev.db.Model;
import net.web_kot.teamdev.db.entities.AbstractEntity;
import net.web_kot.teamdev.db.entities.Order;
import net.web_kot.teamdev.db.entities.SparePart;
import net.web_kot.teamdev.db.entities.Staff;

import java.util.Date;

public class SparePartReservation {
    
    private final Model model;
    
    private final int quantity, partId, staffId, orderId, status;
    private final long date;

    @AbstractEntity.SelectConstructor
    public SparePartReservation(Model model, int quantity, int partId, int staffId, int orderId, long date, int status) {
        this.model = model;
        this.quantity = quantity; this.partId = partId; this.staffId = staffId; this.orderId = orderId;
        this.date = date; this.status = status;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public SparePart getSparePart() throws Exception {
        return model.getSparePartById(partId);
    }
    
    public Staff getResponsible() throws Exception {
        return model.getStaffById(staffId);
    }
    
    public int getOrderNumber() throws Exception {
        return orderId;
    }
    
    public String getAction() {
        switch(Order.Status.values()[status]) {
            case PRELIMINARY:
            case CONFIRMED:
                return "Забронирована";
            case INWORK:
                return "Передана";
            case FINISHED:
            case CLOSED:
                return "Установлена";
        }
        return "?";
    }
    
    public Date getDate() {
        return new Date(date);
    }
    
}
