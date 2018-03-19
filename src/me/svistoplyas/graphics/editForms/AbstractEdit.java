package me.svistoplyas.graphics.editForms;

import javafx.util.Pair;
import me.svistoplyas.graphics.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static java.lang.Integer.parseInt;

public abstract class AbstractEdit extends JDialog {
    ArrayList<Pair<String, JComponent>> components = new ArrayList<>();
    HashMap<JComponent, Pair<Integer, Integer>> marks = new HashMap<>();
    HashSet<JComponent> baddies = new HashSet<>();
    ImageLoader imageLoader = ImageLoader.getInstance();

    public AbstractEdit(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit ? "Редактирование записи" : "Добавление записи", true);
        this.setLayout(null);
        setSize();

        JButton save = new JButton(isEdit ? "Сохранить изменения" : "Добавить запись");
        save.setBounds(10, this.getHeight() - 95, (this.getWidth() - 40) / 2, 60);
        save.addActionListener((e) -> {
            performEdit();
            try {
                for (Pair<String, JComponent> component : components) {
                    switch (isEmptyOrBadlyFilled(component)) {
                        case 0:
                            //throw new RuntimeException("Все поля обязательны для заполнения");
                        case 1:
                            //throw new RuntimeException("Не все поля верно заполнены");
                            baddies.add(component.getValue());
                    }
                }
                int i = 1;
                redraw();
//
//                if(nw && view.hasCustomAddAction()) {
//                    Object[] values = new Object[fields.length];
//                    for(int i = 0; i < fields.length; i++)
//                        values[i] = getValue(getters[i].getReturnType().getCanonicalName(), components[i]);
//
//                    view.customAddAction(values);
//                    Connection.getSession().evict(object);
//                } else {
//                    for(int i = 0; i < fields.length; i++)
//                        if(fields[i].isEditable(nw, filter))
//                            setters[i].invoke(object, getValue(getters[i].getReturnType().getCanonicalName(), components[i]));
//
//                    Session s = Connection.getSession();
//                    s.beginTransaction();
//                    try {
//                        s.save(object);
//                        s.getTransaction().commit();
//                        if(nw) s.evict(object);
//                    } catch(Exception ex) {
//                        if(!nw) update = true; // ??? - s.refresh(object);
//                        throw ex;
//                    }
//                }
//
//                update = true;
//                EditFormBuilder.this.setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                //Main.handleDatabaseException(ex);
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

    public boolean changed() {
        return true;
    }

    public abstract void performAdd();

    public abstract void performEdit();

    private int isEmptyOrBadlyFilled(Pair<String, JComponent> pair) {
        baddies.clear();

        JComponent c = pair.getValue();
        String type = pair.getKey();
        if (c instanceof JTextField) {
            String str = ((JTextField) c).getText().trim();

            if (type.equals("Phone"))
                try {
                    if (str.length() != 5 && str.length() != 6 && str.length() != 7 && str.length() != 11)
                        return 1;

                    Integer.parseInt(str);
                } catch (Exception e) {
                    return 1;
                }

            if (((JTextField) c).getText().trim().equals(""))
                return 0;
            else
                return -1;
        }
        if (c instanceof JComboBox)
            if (((JComboBox) c).getSelectedItem() == null)
                return 0;

//        if (c instanceof JCheckBox)
//            return false;

        throw new RuntimeException("isEmptyOrBadlyFilled - unknown component " + c.getClass().getCanonicalName());
    }

    public void redraw() {
        repaint();

        for (JComponent component : marks.keySet()) {
            Pair<Integer, Integer> pair = marks.get(component);
            String file;
            if (baddies.contains(component))
                file = "resources/images/correct-symbol.png";
            else
                file = "resources/images/letter-x.png";

            JButton butn = new JButton(new ImageIcon(imageLoader.getImage(file)));
            butn.setBounds(pair.getKey(), pair.getValue(), 24, 24);
            butn.setOpaque(false);
            butn.setContentAreaFilled(false);
            butn.setBorderPainted(false);
            butn.setEnabled(false);
            butn.setDisabledIcon(new ImageIcon(imageLoader.getImage(file)));
            add(butn);
        }
    }

    public void addMark(JComponent component, int x, int y, String type) {
        components.add(new Pair<>(type, component));
        marks.put(component, new Pair<>(x, y));
    }

    public void addMark(JComponent component, int x, int y) {
        components.add(new Pair<>("", component));
        marks.put(component, new Pair<>(x, y));
    }

}