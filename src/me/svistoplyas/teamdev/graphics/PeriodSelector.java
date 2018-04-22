package me.svistoplyas.teamdev.graphics;

import me.svistoplyas.teamdev.graphics.utils.Converter;
import org.apache.commons.lang3.time.DateUtils;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PeriodSelector extends JPanel {
    
    private ArrayList<ActionListener> listeners = new ArrayList<>();
    private JDatePicker from, to;
    
    public PeriodSelector() {
        this.setLayout(null);
        this.setSize(220, 54);
        
        JLabel label = new JLabel("За период:");
        label.setBounds(570 - 568, 2, 80, 24);
        this.add(label);
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1);
        
        from = new JDatePicker(calendar);
        from.addActionListener(this::periodChanged);
        from.setBounds(639 - 568, 4, 150, 24);
        this.add(from);
        
        to = new JDatePicker(new Date());
        to.addActionListener(this::periodChanged);
        to.setBounds(639 - 568, 30, 150, 24);
        this.add(to);
    }
    
    public void addActionListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }
    
    private void periodChanged(ActionEvent e) {
        for(ActionListener listener : listeners)
            listener.actionPerformed(e);
    }
    
    public Date getStart() {
        Converter c = Converter.getInstance();
        
        Date start = c.convertDataPicker(from);
        return DateUtils.addMilliseconds(DateUtils.truncate(start, Calendar.DATE), -1);
    }
    
    public Date getFinish() {
        Converter c = Converter.getInstance();
        
        Date end = c.convertDataPicker(to);
        return DateUtils.ceiling(end, Calendar.DATE);
    }
    
}
