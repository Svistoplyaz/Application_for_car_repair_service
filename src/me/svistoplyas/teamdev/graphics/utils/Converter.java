package me.svistoplyas.teamdev.graphics.utils;


public class Converter {
    private static Converter instance;

    public static Converter getInstance() {
        if (instance == null)
            instance = new Converter();
        return instance;
    }

    public String convertPriceToStr(int price){
        return price / 100 + "," + price % 100 + "";
//        return price / 100 + " руб. " + price % 100 + " коп.";
    }

    public String convertPriceToStrColon(int price){
        return price / 100 + "," + price % 100 + "";
    }

}
