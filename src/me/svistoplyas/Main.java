package me.svistoplyas;

import me.svistoplyas.graphics.LoginForm;
import me.svistoplyas.graphics.MainFrame;

import javax.swing.*;
import java.util.Locale;

public class Main {
    private static MainFrame mf;

    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Locale.setDefault(Locale.ENGLISH);

        (new LoginForm()).show();
    }

    public static void afterLogin(boolean type){
        mf = new MainFrame(type);
        mf.setVisible(true);
    }
}
