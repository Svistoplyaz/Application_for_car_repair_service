package me.svistoplyas.graphics;

import me.svistoplyas.Main;

import javax.swing.*;
import java.awt.*;

public class LoginForm {

    private JFrame frame;

    private int defUser = 1;
    private String defPass = "goose";

    public LoginForm() {
        frame = new JFrame("Вход в систему");
        frame.getContentPane().setPreferredSize(new Dimension(330, 118));
        frame.setLayout(null);

//        try {
//            frame.setIconImage(ImageIO.read(MainFrame.class.getResourceAsStream("/book.png")));
//        } catch(Exception e) {
//            throw new RuntimeException(e);
//        }

        JLabel l1 = new JLabel("Пользователь:");
        l1.setBounds(10, 10, 100, 20);
        frame.add(l1);

//        JComboBox box = new JComboBox(Connection.getSession().createQuery("from Staff").list().toArray());
        JComboBox box = new JComboBox(new String[]{"Менеджер", "Владелец"});
        if(box.getItemCount() >= (defUser + 1)) box.setSelectedIndex(defUser);
        box.setBounds(120, 10, 200, 20);
        frame.add(box);

        JLabel l2 = new JLabel("Пароль:");
        l2.setBounds(10, 35, 100, 20);
        frame.add(l2);

        JPasswordField pass = new JPasswordField(defPass);
        pass.setBounds(120, 35, 200, 20);
        frame.add(pass);

        JLabel text = new JLabel("", SwingConstants.CENTER);
        text.setBounds(120, 60, 200, 20);
        frame.add(text);

        JButton bt = new JButton("Войти");
        bt.setBounds(115, 85, 100, 24);
        bt.addActionListener((event) -> {
            String password = new String(pass.getPassword());
//            Staff user = (Staff)box.getSelectedItem();
//
            if(String.valueOf(box.getSelectedItem()).equals("Менеджер") && password.equals("123")) {
                frame.setVisible(false);
                Main.afterLogin(false);
            } else if(String.valueOf(box.getSelectedItem()).equals("Владелец") && password.equals("qwe")){
                frame.setVisible(false);
                Main.afterLogin(true);
            }else {
                text.setText("Неверный логин или пароль!");
            }
        });
        frame.add(bt);
    }

    public void show() {
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}