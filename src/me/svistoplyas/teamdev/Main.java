package me.svistoplyas.teamdev;

import me.svistoplyas.teamdev.graphics.LoginForm;
import me.svistoplyas.teamdev.graphics.MainFrame;
import net.web_kot.teamdev.db.Database;
import net.web_kot.teamdev.db.Model;
import net.web_kot.teamdev.db.entities.Client;
import net.web_kot.teamdev.db.entities.Mark;
import net.web_kot.teamdev.db.entities.Order;
import net.web_kot.teamdev.db.entities.VehicleModel;

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

        db = new Database(f);
        model = db.getModel();
        
        if(!f.exists()) {
            model.createService("Замена масла").save();
            model.getServices().get(0).setPrice(15304);
            model.createClient("Misha").setPhone("671821").save();

            Client vasya = model.createClient("Vasya").setPhone("222222").save();
            Mark toyota = model.createMark("Toyota").save();
            Mark nissan = model.createMark("Nissan").save();
            model.createVehicleModel(nissan, "Passat", 2008).save();
            model.createVehicleModel(nissan, "Almera", 2018).save();
            VehicleModel corolla = model.createVehicleModel(toyota, "Corolla", 2012).save();
            Order o = model.createOrder(vasya, corolla, new Date()).setRegistrationNumber("А222МР777RUS").
                    setFinishDate(new Date(119, 10, 22)).save();
        }

        (new LoginForm()).show();

    }

    public static void afterLogin(boolean type) {
        mf = new MainFrame(type, model);
        mf.setVisible(true);
    }
}
