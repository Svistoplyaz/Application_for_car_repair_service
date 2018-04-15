package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.entities.SparePart;
import net.web_kot.teamdev.db.wrappers.SparePartReservation;
import org.apache.commons.lang3.time.DateUtils;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservedView extends AbstractView {
    
    private JDatePicker from, to;
    
    public ReservedView(MainFrame _mainFrame) {
        super(_mainFrame);
        table.getColumnModel().getColumn(2).setMaxWidth(42);
        
        JLabel label = new JLabel("За период:");
        label.setBounds(570, 2, 80, 24);
        this.add(label);
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1);
        
        from = new JDatePicker(calendar);
        from.addActionListener(e -> updateTable());
        from.setBounds(639, 4, 150, 24);
        this.add(from);

        to = new JDatePicker(new Date());
        to.addActionListener(e -> updateTable());
        to.setBounds(639, 30, 150, 24);
        this.add(to);
    }

    @Override
    String[] getColumnNames() {
        return new String[] {"Запасная часть", "Получатель", "Заказ", "Дата", "Действие"};
    }

    @Override
    Object[][] getData() {
        if(from == null || to == null) return new Object[0][];
        
        Converter c = Converter.getInstance();
        Date start = c.convertDataPicker(from), end = c.convertDataPicker(to);
        
        start = DateUtils.addMilliseconds(DateUtils.truncate(start, Calendar.DATE), -1);
        end = DateUtils.ceiling(end, Calendar.DATE);
        
        try {
            List<SparePartReservation> reservations = mainFrame.model.getReservation(start, end);
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
