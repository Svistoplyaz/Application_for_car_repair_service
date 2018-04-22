package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.PeriodSelector;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.SparePart;
import net.web_kot.teamdev.db.wrappers.SparePartReservation;

import java.util.List;

public class ReservedView extends AbstractView {
    
    private PeriodSelector period;
    
    public ReservedView(MainFrame _mainFrame) {
        super(_mainFrame ,false);
        table.getColumnModel().getColumn(2).setMaxWidth(42);
        
        period = new PeriodSelector();
        period.addActionListener(e -> updateTable());
        period.setLocation(568, 0);
        this.add(period);
    }

    @Override
    String[] getColumnNames() {
        return new String[] {"Запасная часть", "Получатель", "Заказ", "Дата", "Действие"};
    }

    @Override
    Object[][] getData() {
        if(period == null) return new Object[0][];
        
        Converter c = Converter.getInstance();
        try {
            List<SparePartReservation> reservations = mainFrame.model.getReservation(period.getStart(), period.getFinish());
            Object[][] result = new Object[reservations.size()][];
            
            for(int i = 0; i < result.length; i++) {
                SparePartReservation reservation = reservations.get(i);
                SparePart part = reservation.getSparePart();
                
                result[i] = new Object[] {
                        part.getName() + " (" + c.beautifulQuantity(reservation.getQuantity(), part.getUnit()) + " " + part.getUnit() + ")",
                        reservation.getResponsible().getName(),
                        "№ " + reservation.getOrderNumber(),
                        c.dateToStr(reservation.getDate()),
                        reservation.getAction()
                };
            }
            return result;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new Object[0][];
    }

    @Override
    Object getObject(int row) {
        return null;
    }

    @Override
    boolean canAdd() {
        return false;
    }

    @Override
    boolean canEdit() {
        return false;
    }

    @Override
    boolean canDelete() {
        return false;
    }

    @Override
    void performDelete(int row) {

    }

    @Override
    public String toString() {
        return "Забронированные детали";
    }
}
