package me.svistoplyas.teamdev.graphics.editForms;

import javafx.util.Pair;
import me.svistoplyas.teamdev.graphics.ImageLoader;
import me.svistoplyas.teamdev.graphics.MainFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractEdit extends JDialog {
    ArrayList<Pair<String, JComponent>> components = new ArrayList<>();
    HashMap<JComponent, Boolean> baddies = new HashMap<>();
    ImageLoader imageLoader = ImageLoader.getInstance();
    HashMap<JComponent, JLabel> marks = new HashMap<>();
    MainFrame mainFrame;
    Object data;

    public AbstractEdit(JFrame frame, boolean isEdit, Object _data) {
        super(frame, isEdit ? "Редактирование записи" : "Добавление записи", true);
        data = _data;
        mainFrame = (MainFrame) frame;
        this.setLayout(null);
        setSize();

        JButton save = new JButton(isEdit ? "Сохранить изменения" : "Добавить запись");
        save.setBounds(10, this.getHeight() - 95, (this.getWidth() - 40) / 2, 60);
        save.addActionListener((e) -> {
            try {
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
                redraw();
                if (noBaddies()) {
                    if(isEdit)
                        performEdit();
                    else
                        performAdd();
                    AbstractEdit.this.setVisible(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        add(save);

        JButton exit = new JButton("Отмена");
        exit.setBounds(this.getWidth() - (this.getWidth() - 40) / 2 - 15, this.getHeight() - 95, (this.getWidth() - 40) / 2, 60);
        exit.addActionListener((e) -> AbstractEdit.this.setVisible(false));
        add(exit);

        setResizable(false);
        redraw();
//        pack();
        setLocationRelativeTo(frame);
    }

    abstract void setSize();

    public abstract void fillFields();

    public abstract void performAdd() throws Exception;

    public abstract void performEdit() throws Exception;

    private int isEmptyOrBadlyFilled(Pair<String, JComponent> pair) {

        JComponent c = pair.getValue();
        String type = pair.getKey();
        if (c instanceof JTextField) {
            String str = ((JTextField) c).getText().trim();

            if (str.equals(""))
                return 0;

            if (type.equals("Phone"))
                try {
                    if (str.length() != 5 && str.length() != 6 && str.length() != 7 && str.length() != 11)
                        return 1;

                    Long.parseLong(str);
                } catch (Exception e) {
                    return 1;
                }
            else if (type.equals("Price")) {
                if (str.charAt(str.length() - 3) != ',')
                    return 1;
                else {
                    String[] arr = str.split(",");
                    try {
                        Integer.parseInt(arr[0]);
                        Integer.parseInt(arr[1]);
                        return -1;
                    } catch (Exception e) {
                        return 1;
                    }
                }
            }
            else
                return -1;
        }
        if (c instanceof JComboBox)
            if (((JComboBox) c).getSelectedItem() == null)
                return 0;
            else
                return -1;
        else
            return -1;

//        if (c instanceof JCheckBox)
//            return false;

//        throw new RuntimeException("isEmptyOrBadlyFilled - unknown component " + c.getClass().getCanonicalName());
    }

    public void redraw() {
        for (JComponent component : baddies.keySet()) {
            Boolean filledWrong = baddies.get(component);
            String file;
            if (filledWrong)
                file = "/images/letter-x.png";
            else
                file = "/images/correct-symbol.png";

            if (marks.get(component) == null) {
                JLabel label = new JLabel(new ImageIcon(imageLoader.getImage(file)));
                label.setBounds(component.getX() + component.getWidth() + 10, component.getY(), 24, 24);
                add(label);
                marks.put(component, label);
            } else {
                marks.get(component).setIcon(new ImageIcon(imageLoader.getImage(file)));
            }
        }

        repaint();
    }

    public boolean noBaddies(){
        for (JComponent component : baddies.keySet()) {
            if(baddies.get(component))
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

}