package me.svistoplyas.teamdev.graphics.editForms;

import javafx.util.Pair;
import me.svistoplyas.teamdev.graphics.utils.ImageLoader;
import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.utils.Converter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractEdit extends JDialog {
    ArrayList<Pair<String, JComponent>> components = new ArrayList<>();
    HashMap<JComponent, Boolean> baddies = new HashMap<>();
    ImageLoader imageLoader = ImageLoader.getInstance();
    HashMap<JComponent, JLabel> marks = new HashMap<>();
    MainFrame mainFrame;
    Object data;
    JButton save;
    JButton exit;

    public AbstractEdit(JFrame frame, boolean isEdit, Object _data) {
        super(frame, isEdit ? "Редактирование записи" : "Добавление записи", true);
        data = _data;
        mainFrame = (MainFrame) frame;
        this.setLayout(null);
        setSize();

        save = new JButton(isEdit ? "Сохранить изменения" : "Добавить запись");
        save.setBounds(10, this.getHeight() - 95, (this.getWidth() - 40) / 2, 60);
        save.addActionListener((e) -> {
            try {
                save.setEnabled(false);
                baddies.clear();
                for (Pair<String, JComponent> component : components) {
                    switch (isEmptyOrBadlyFilled(component)) {
                        case 0:
                            //throw new RuntimeException("Все поля обязательны для заполнения");
                        case 1:
                            //throw new RuntimeException("Не все поля верно заполнены");
                            baddies.put(component.getValue(), true);
                            break;
                        default:
                            baddies.put(component.getValue(), false);
                    }
                }
                redrawValidationMarks();

                if (otherValidation()) {
                    if (noBaddies()) {
                        if (isEdit)
                            performEdit();
                        else
                            performAdd();
                        AbstractEdit.this.setVisible(false);
                    }
                }
                save.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        add(save);

        exit = new JButton("Отмена");
        exit.setBounds(this.getWidth() - (this.getWidth() - 40) / 2 - 15, this.getHeight() - 95, (this.getWidth() - 40) / 2, 60);
        exit.addActionListener((e) -> AbstractEdit.this.setVisible(false));
        add(exit);

        setResizable(false);
        redrawValidationMarks();
//        pack();
        setLocationRelativeTo(frame);
    }

    public abstract void setSize();

    public abstract void fillFields();

    public abstract void performAdd() throws Exception;

    public abstract void performEdit() throws Exception;

    public abstract boolean otherValidation();

    private int isEmptyOrBadlyFilled(Pair<String, JComponent> pair) {
        JComponent c = pair.getValue();
        String type = pair.getKey();
        if (c instanceof JTextField) {
            String str = ((JTextField) c).getText().trim();

            if (str.equals("") || str.length() > 100)
                return 0;

            switch (type) {
                case "Phone": {
                    try {
                        if (str.length() != 5 && str.length() != 6 && str.length() != 7 && str.length() != 11)
                            return 1;

                        Long.parseLong(str);
                        return -1;
                    } catch (Exception e) {
                        return 1;
                    }
                }
                case "Price": {
                    try {
                        int test = Converter.getInstance().convertStrToPrice(str);
                        if (test < 0)
                            throw new Exception();
                        return -1;
                    } catch (Exception ex) {
                        return 1;
                    }
                }
                default:
                    return -1;
            }
        } else if (c instanceof JComboBox)
            if (((JComboBox) c).getSelectedItem() == null)
                return 0;
            else
                return -1;
        else
            throw new RuntimeException("isEmptyOrBadlyFilled - unknown component " + c.getClass().getCanonicalName());

    }

    public void redrawValidationMarks() {
        for (JComponent component : baddies.keySet()) {
            Boolean filledWrong = baddies.get(component);
            String file;
            if (filledWrong)
                file = "/images/letter-x.png";
            else
                file = "/images/correct-symbol.png";

            if (marks.get(component) == null) {
                JLabel label = new JLabel(new ImageIcon(imageLoader.getImage(file)));
                if (component.getName() != null && component.getName().equals("Left"))
                    label.setBounds(component.getX() - 34, component.getY(), 24, 24);
                else
                    label.setBounds(component.getX() + component.getWidth() + 10, component.getY(), 24, 24);
                add(label);
                marks.put(component, label);
            } else {
                marks.get(component).setIcon(new ImageIcon(imageLoader.getImage(file)));
            }
        }

        repaint();
    }

    public boolean noBaddies() {
        for (JComponent component : baddies.keySet()) {
            if (baddies.get(component))
                return false;
        }
        return true;
    }

    //
    public void addMark(JComponent component, String type) {
        components.add(new Pair<>(type, component));
    }

    public void addMark(JComponent component) {
        components.add(new Pair<>("", component));
    }

    public void disableAllComponents() {
        Component[] components = this.getComponents();
        for (Component component : components) {
            component.setEnabled(false);
        }
        this.repaint();
    }

    public void enableAllComponents() {
        Component[] components = this.getComponents();
        for (Component component : components) {
            component.setEnabled(true);
        }
        this.repaint();
    }
}