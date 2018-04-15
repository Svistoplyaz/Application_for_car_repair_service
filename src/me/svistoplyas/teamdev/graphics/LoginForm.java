package me.svistoplyas.teamdev.graphics;

import me.svistoplyas.teamdev.Main;
import net.web_kot.teamdev.db.Model;

import javax.swing.*;
import java.awt.*;

public class LoginForm {

    private JFrame frame;

    private static final int defUser = 1;
    private static final String defPass = "qwe";

    public LoginForm(Model model) {
        frame = new JFrame("Вход в систему");
        frame.getContentPane().setPreferredSize(new Dimension(330, 118));
        frame.setLayout(null);

        JLabel l1 = new JLabel("Пользователь:");
        l1.setBounds(10, 10, 100, 20);
        frame.add(l1);
        
        JComboBox<String> box = new JComboBox<>(new String[]{"Менеджер", "Владелец"});
        if (box.getItemCount() >= (defUser + 1)) box.setSelectedIndex(defUser);
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
            int user = box.getSelectedIndex();
            String password = new String(pass.getPassword());
            
            try {
                if(model.checkPassword(user, password)) {
                    frame.setVisible(false);
                    Main.afterLogin(user == 1);
                } else {
                    text.setText("Неверный логин или пароль!");
                }
            } catch(Exception e) {
                e.printStackTrace();
                text.setText("При авторизации произошла ошибка!");
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