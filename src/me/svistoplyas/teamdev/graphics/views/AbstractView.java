package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.TableModel;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractView extends JPanel {
    JTable table;
    private JButton add, edit, delete;
    MainFrame mainFrame;

    AbstractView(MainFrame _mainFrame) {
        mainFrame = _mainFrame;
        setLayout(null);

        if (canAdd()) {
            add = new JButton("Добавить");
            add.addActionListener((e) -> {
                AbstractEdit b = getEdit(false, null);
                b.setVisible(true);
                updateTable();
            });
            setBtBounds(add, 0);
            add(add);
        }

        if (canEdit()) {
            edit = new JButton("Изменить");
            edit.addActionListener((e) -> {
                int row = table.getSelectedRow();
                if (row != -1)
                    try {
                        AbstractEdit b = getEdit(true, getObject(row));
                        b.setVisible(true);
                        updateTable();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                else
                    JOptionPane.showMessageDialog(this, "Выберите элемент из таблицы для изменения");
            });
            setBtBounds(edit, 1);
            add(edit);
        }

        if (canDelete()) {
            delete = new JButton("Удалить");
            delete.addActionListener((e) -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    if(JOptionPane.showOptionDialog(this,
                            "Вы действительно хотите удалить выбранную запись?",
                            "Подтверждение",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String[]{"Да", "Нет"},
                            "default")!= 0) return;
                    else {
                        try {
                            AbstractView.this.performDelete(row);
                            updateTable();
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(this, "Невозможно удалить элемент",
                                    "Внимание!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(this, "Выберите элемент из таблицы для удаления",
                            "Внимание!", JOptionPane.INFORMATION_MESSAGE);
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

    abstract Object getObject(int row);

    abstract boolean canAdd();

    abstract boolean canEdit();

    abstract boolean canDelete();

    abstract void performDelete(int row) throws Exception;

    private static void setBtBounds(JButton button, int index) {
        button.setBounds(10 + 295 * index, 440, 190, 60);
    }

    public AbstractEdit getEdit(boolean b, Object o) {
        return null;
    }

    public void updateTable(){
        ((TableModel)table.getModel()).setData(getData());
        mainFrame.repaint();
    }
}
