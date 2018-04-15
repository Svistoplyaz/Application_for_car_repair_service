package me.svistoplyas.teamdev.graphics.otherFrames;

import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;

import javax.swing.*;

public class ChangePassword extends AbstractEdit {
    
    private JPasswordField current, nw, repeat;
    
    public ChangePassword(JFrame frame) {
        super(frame, true, null);
        this.setTitle("Изменение пароля");
        
        current = addField(20, "Текущий пароль:", false);
        nw = addField(50, "Новый пароль:", true);
        repeat = addField(80, "Повторите пароль:", false);
    }
    
    private JPasswordField addField(int y, String text, boolean checkEmpty) {
        JLabel quantLabel = new JLabel(text);
        quantLabel.setBounds(10, y, 140, 24);
        add(quantLabel);

        JPasswordField field = new JPasswordField();
        field.setBounds(215, y, 160, 24);
        add(field);
        if(checkEmpty) addMark(field, "Password");
        
        return field;
    }

    @Override
    public void setSize() {
        this.setSize(385 + 35, 150 + 60);
    }

    @Override
    public void fillFields() {

    }

    @Override
    public void performAdd() throws Exception {

    }

    @Override
    public void performEdit() throws Exception {
        mainFrame.model.setPassword(mainFrame.getUserId(), new String(nw.getPassword()));
    }

    @Override
    public boolean otherValidation() {
        try {
            if(!mainFrame.model.checkPassword(mainFrame.getUserId(), new String(current.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Текущий пароль введен неверно!");
                return false;
            }
            
            if(!(new String(nw.getPassword())).equals(new String(repeat.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Введенные пароли различаются!");
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
}
