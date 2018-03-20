package me.svistoplyas.teamdev.graphics;

import javax.swing.*;

public class Loading extends JPanel {

    public Loading() {
        setLayout(null);

        JLabel label = new JLabel("Идет загрузка... пожалуйста, подождите...");
        label.setFont(label.getFont().deriveFont(18F));
        label.setBounds(220, 40, 400, 40);
        add(label);
    }

}

