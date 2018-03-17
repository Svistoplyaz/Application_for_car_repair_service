package me.svistoplyas.graphics.views;

import javax.swing.*;
import javax.swing.table.TableModel;

public abstract class AbstractView extends JPanel{
    JTable table;

    AbstractView(){
        table = new JTable(new TableModel());
    }

}
