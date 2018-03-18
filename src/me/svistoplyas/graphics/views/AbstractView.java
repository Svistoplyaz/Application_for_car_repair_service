package me.svistoplyas.graphics.views;

import me.svistoplyas.graphics.MainFrame;
import me.svistoplyas.graphics.TableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractView extends JPanel{
    private JTable table;
    private JButton add, edit, delete;
    MainFrame mainFrame;

    AbstractView(MainFrame _mainFrame){
        mainFrame = _mainFrame;
        setLayout(null);

        if(canAdd()) {
            add = new JButton("Добавить");
            add.addActionListener((e) -> {
//            boolean changed = false;
//            for(int i = 0; i < filters.length; i++)
//                if(filters[i].getFilterField() == null && filteredValues[i] != null) {
//                    filtersBoxes[i].setSelectedItem(null);
//                    changed = true;
//                }
//            if(changed) updateFilters();
//
//            try {
//                Object instance = newInstance();
//                for(int i = 0; i < filters.length; i++)
//                    if(filteredValues[i] != null) {
//                        Method m = instance.getClass().getMethod("set" + filters[i].getFilterField(), filteredValues[i].getClass());
//                        m.invoke(instance, filteredValues[i]);
//                    }
//
//                EditFormBuilder b = new EditFormBuilder(true, globalFilter, frame, getEditableFieldsDescriptions(), instance, this);
//                b.setVisible(true);
//                if(b.isNeedUpdate()) updateData(table.getRowCount(), false);
//            } catch(Exception ex) {
//                Main.handleDatabaseException(ex);
//            }
            });
            setBtBounds(add, 0);
            add(add);
        }

        if(canEdit()) {
            edit = new JButton("Изменить");
            edit.addActionListener((e) -> {
//            int row = table.getSelectedRow();
//            if(hasFakeRow()) row--;
//            try {
//                EditFormBuilder b = new EditFormBuilder(false, globalFilter, frame, getEditableFieldsDescriptionsEdit(), data.get(row), this);
//                b.setVisible(true);
//                if(b.isNeedUpdate()) updateData(row, false);
//            } catch(Exception ex) {
//                Main.handleDatabaseException(ex);
//            }
            });
            setBtBounds(edit, 1);
            add(edit);
        }

        if(canDelete()) {
            delete = new JButton("Удалить");
            delete.addActionListener((e) -> {
//            String text = "Вы действительно хотите удалить выбранную запись?";
//            if(JOptionPane.showConfirmDialog(this, text, "Подтверждение", JOptionPane.YES_NO_OPTION) != 0) return;
//
//            int row = table.getSelectedRow();
//            if(hasFakeRow()) row--;
//            try {
//                Session s = Connection.getSession();
//                s.beginTransaction();
//                s.delete(data.get(row));
//                s.getTransaction().commit();
//
//                data.remove(row);
//
//                ((TableModel)table.getModel()).fireTableDataChanged();
//                if(row == data.size()) row--;
//                if(row != -1) table.setRowSelectionInterval(row, row);
//            } catch(Exception ex) {
//                Main.handleDatabaseException(ex);
//            }
            });
            setBtBounds(delete, 2);
            add(delete);
        }

        table = new JTable(new TableModel(getColumnNames(), getData()));
//        table.setBounds(140, 140, 200, 200);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//        table.getModel().addTableModelListener((e) -> TableAutoResizer.sizeColumnsToFit(table, 718 + 60));
        table.getSelectionModel().addListSelectionListener((e) -> {
//            boolean dis = table.getSelectedRow() == -1 || (hasFakeRow() && table.getSelectedRow() == 0);
//            if(Connection.isUserBuh()) {
//                edit.setEnabled(!dis);
//                delete.setEnabled(!dis);
//            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
//                int r = table.rowAtPoint(e.getPoint());
//                if(r >= 0 && r < table.getRowCount()) table.setRowSelectionInterval(r, r); else table.clearSelection();
//
//                int rowindex = table.getSelectedRow();
//                if (rowindex < 0) return;
//                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
//                    JPopupMenu popup = createPopUp();
//                    popup.show(e.getComponent(), e.getX(), e.getY());
//                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 54, 718 + 60, 378);
        add(scrollPane);
    }

    abstract String[] getColumnNames();

    abstract Object[][] getData();

    abstract boolean canAdd();

    abstract boolean canEdit();

    abstract boolean canDelete();

    private static void setBtBounds(JButton button, int index) {
        button.setBounds(10 + 295 * index, 440, 190, 60);
    }
}
