package me.svistoplyas.graphics.editForms;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.HashMap;

public abstract class AbstractEdit extends JDialog {

    public AbstractEdit(JFrame frame, boolean isEdit, Object data) {
        super(frame, isEdit ? "Редактирование записи" : "Добавление записи", true);
        this.setLayout(null);
        setSize();

        JButton save = new JButton(isEdit ? "Сохранить изменения" : "Добавить запись");
        save.setBounds(10, this.getHeight() - 95, (this.getWidth()-40)/2, 60);
        save.addActionListener((e) -> {
//            try {
//                for(int i = 0; i < fields.length; i++)
//                    if(isEmpty(components[i])) throw new RuntimeException("Все поля обязательны для заполнения");
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
//            } catch(Exception ex) {
//                Main.handleDatabaseException(ex);
//            }
        });
        add(save);

        JButton exit = new JButton("Отмена");
        exit.setBounds(this.getWidth() - (this.getWidth()-40)/2 - 15, this.getHeight() - 95, (this.getWidth()-40)/2, 60);
        exit.addActionListener((e) -> AbstractEdit.this.setVisible(false));
        add(exit);

        setResizable(false);
//        pack();
        setLocationRelativeTo(frame);
    }

    abstract void setSize();


    public static class PlaceholderTextField extends JTextField {

        private String placeholder;

        public PlaceholderTextField() {
        }

        public PlaceholderTextField(
                final Document pDoc,
                final String pText,
                final int pColumns)
        {
            super(pDoc, pText, pColumns);
        }

        public PlaceholderTextField(final int pColumns) {
            super(pColumns);
        }

        public PlaceholderTextField(final String pText) {
            super(pText);
        }

        public PlaceholderTextField(final String pText, final int pColumns) {
            super(pText, pColumns);
        }

        public String getPlaceholder() {
            return placeholder;
        }

        @Override
        protected void paintComponent(final Graphics pG) {
            super.paintComponent(pG);

            if (placeholder.length() == 0 || getText().length() > 0) {
                return;
            }

            final Graphics2D g = (Graphics2D) pG;
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getDisabledTextColor());
            g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
                    .getMaxAscent() + getInsets().top);
        }

        public void setPlaceholder(final String s) {
            placeholder = s;
        }

    }
}