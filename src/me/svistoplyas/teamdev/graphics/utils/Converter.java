package me.svistoplyas.teamdev.graphics.utils;


import net.web_kot.teamdev.db.entities.SparePart;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("deprecation")
public class Converter {
    private static Converter instance;
    private DateFormat dfM = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private DateFormat dfY = new SimpleDateFormat("dd/MM/yyyy");

    public static Converter getInstance() {
        if (instance == null)
            instance = new Converter();
        return instance;
    }

    public String convertPriceToStr(int price) {
        String str = "";
        if(price < 0) {
            str = "-";
            price *= -1;
        }
        
        if (price % 100 < 10)
            str += price / 100 + ",0" + price % 100 + "";
        else if (price % 100 % 10 == 0)
            str += price / 100 + "," + price % 100 / 10 + "";
        else
            str += price / 100 + "," + price % 100 + "";
        
        return str;
    }

    public String convertPriceToStrOnlyRubbles(int price) {
        return price / 100 + "";
    }

    public int convertStrToPrice(String price) throws Exception {
        String[] str = price.split(",");
        switch (str.length) {
            case 1:
                return Integer.parseInt(str[0]) * 100;
            case 2:
                if (str[1].length() == 2)
                    return Integer.parseInt(str[0]) * 100 + Integer.parseInt(str[1]);
                else if (str[1].length() == 1)
                    return Integer.parseInt(str[0]) * 100 + Integer.parseInt(str[1]) * 10;
                else
                    throw new Exception("Wrong format");
            default:
                throw new Exception("Wrong format");
        }

    }

    public String dateToStrWithTime(Date date) {
        return dfM.format(date);
    }

    public String dateToStr(Date date) {
        return dfY.format(date);
    }

    public Date convertSpinnerAndDataPicker(JSpinner spinner, JDatePicker datePicker) {
        Date ans = ((SpinnerDateModel) spinner.getModel()).getDate();
        DateModel model = datePicker.getModel();
        ans.setDate(model.getDay());
        ans.setMonth(model.getMonth());
        ans.setYear(model.getYear() - 1900);
        return ans;
    }

    public Date convertDataPicker(JDatePicker datePicker) {
        Date ans = new Date();
        DateModel model = datePicker.getModel();
        ans.setDate(model.getDay());
        ans.setMonth(model.getMonth());
        ans.setYear(model.getYear() - 1900);
        return ans;
    }
    
    public String beautifulQuantity(int quantity, SparePart.Unit unit) {
        if(unit == SparePart.Unit.pieces) return (quantity / 100) + "";
        return (quantity / 100) + "," + (quantity % 100);
    }
    
}
