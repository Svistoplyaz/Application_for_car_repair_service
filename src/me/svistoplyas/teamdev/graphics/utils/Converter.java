package me.svistoplyas.teamdev.graphics.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        return price / 100 + "," + price % 100 + "";
//        return price / 100 + " руб. " + price % 100 + " коп.";
    }

    public String convertPriceToStrOnlyRubbles(int price) {
        return price / 100 + "";
    }

    public String dateToStrWithTime(Date date){
        return dfM.format(date);
    }

    public String dateToStr(Date date){
        return dfY.format(date);
    }

}
