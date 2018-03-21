package me.svistoplyas.teamdev.graphics.editForms;

import javafx.util.Pair;
import me.svistoplyas.teamdev.graphics.ImageLoader;
import me.svistoplyas.teamdev.graphics.MainFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public abstract class AbstractEdit extends JDialog {
    ArrayList<Pair<String, JComponent>> components = new ArrayList<>();
    //    HashMap<JComponent, Pair<Integer, Integer>> marks = new HashMap<>();
    HashMap<JComponent, Boolean> baddies = new HashMap<>();
    ImageLoader imageLoader = ImageLoader.getInstance();
    HashMap<JComponent, JLabel> marks = new HashMap<>();
    MainFrame mainFrame;

    public AbstractEdit(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit ? "Редактирование записи" : "Добавление записи", true);
        mainFrame = (MainFrame) frame;
        this.setLayout(null);
        setSize();

        JButton save = new JButton(isEdit ? "Сохранить изменения" : "Добавить запись");
        save.setBounds(10, this.getHeight() - 95, (this.getWidth() - 40) / 2, 60);
        save.addActionListener((e) -> {
            performEdit();
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

    public abstract void fillFields(Object data);

    public abstract void performAdd();

    public abstract void performEdit();

    private int isEmptyOrBadlyFilled(Pair<String, JComponent> pair) {

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
        for (JComponent component : baddies.keySet()) {
            Boolean filledWrong = baddies.get(component);
            String file;
            if (filledWrong)
                file = "resources/images/letter-x.png";
            else
                file = "resources/images/correct-symbol.png";

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

    //
    public void addMark(JComponent component, String type) {
        components.add(new Pair<>(type, component));
    }

    public void addMark(JComponent component) {
        components.add(new Pair<>("", component));
    }

}