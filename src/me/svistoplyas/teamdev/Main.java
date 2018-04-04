package me.svistoplyas.teamdev;

import me.svistoplyas.teamdev.graphics.LoginForm;
import me.svistoplyas.teamdev.graphics.MainFrame;
import net.web_kot.teamdev.db.Database;
import net.web_kot.teamdev.db.Model;
import net.web_kot.teamdev.db.entities.*;

import javax.swing.*;
import java.io.File;
import java.util.Date;
import java.util.Locale;

public class Main {
    private static MainFrame mf;
    private static Database db;
    private static Model model;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Locale.setDefault(Locale.ENGLISH);

        File f = new File("myfile.db");
        f.delete();
        boolean exists = f.exists();
        
        db = new Database(f);
        model = db.getModel();
        
        if(!exists) {
            /* Vehicles marks and models */
            Position pos = model.createPosition("Механик").save();
            Staff staff = model.createStaff(pos, "Vasya", "123456", new Date()).save();
            
            Mark toyota = model.createMark("Toyota").save();
            model.createVehicleModel(toyota, "Corolla", 2012).save();
            model.createVehicleModel(toyota, "Mark II", 1994).save();
            model.createVehicleModel(toyota, "Highlander", 2014).save();
            model.createVehicleModel(toyota, "Camry", 2014).save();
            model.createVehicleModel(toyota, "Camry", 2018).save();
            
            Mark nissan = model.createMark("Nissan").save();
            model.createVehicleModel(nissan, "Passat", 2008).save();
            model.createVehicleModel(nissan, "Almera", 2018).save();
            model.createVehicleModel(nissan, "X-Trail", 2007).save();
            
            Mark kia = model.createMark("Kia").save();
            model.createVehicleModel(kia, "Rio", 2013).save();
            model.createVehicleModel(kia, "Cee'd", 2016).save();
            model.createVehicleModel(kia, "Rio", 2010).save();
            
            /* Services */
            
            model.createService("Замена воздушного фильтра").save().setPrice(20000);
            model.createService("Замена головки блока цилиндров").save().setPrice(800000);
            model.createService("Замена подшипника ступицы").save().setPrice(80000);
            model.createService("Ремонт радиатора системы охлаждения").save().setPrice(100000);
            model.createService("Промывка инжектора").save().setPrice(100000);
            model.createService("Промывка инжектора").save().setPrice(100000);
            model.createService("Регулировка троса ручного тормоза").save().setPrice(30000);
            model.createService("Диагностика двигателя").save().setPrice(80000);
            model.createService("Установка противотуманных фар").save().setPrice(150000);
            
            /* Clients */
            
            model.createClient("Лещёв Архип Эдуардович").setPhone("59756906919 ").save();
            model.createClient("Салтыкова Ирина Германовна").setPhone("0797932581").save();
            model.createClient("Мосин Игорь Евстафиевич").save();
            model.createClient("Бореева Оксана Потаповна").setPhone("48984769479").save();
            model.createClient("Лебедков Георгий Макарович").setPhone("50282926645").save();
            model.createClient("Антонов Валерий Епифанович ").setPhone("319342911").save();
        }

        (new LoginForm()).show();

    }

    public static void afterLogin(boolean type) {
        mf = new MainFrame(type, model);
        mf.setVisible(true);
    }
}
