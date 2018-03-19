package me.svistoplyas.graphics;

import me.svistoplyas.graphics.views.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MainFrame extends JFrame{
    private final HashMap<String, AbstractView> views = new HashMap<>();
    public static final Loading loading = new Loading();
    public JPanel navigation, content;
    public boolean type;

    public MainFrame(boolean _type){
        super("Реестр имущества студенческого городка");
        type = _type;
        getContentPane().setPreferredSize(new Dimension(970 + 60, 420 + 83 + 30));
        setLayout(null);

        navigation = new JPanel();
        navigation.setLayout(null);
        navigation.setBounds(10, 10, 200, 400 + 83 + 30);
        navigation.setBorder(BorderFactory.createLineBorder(Color.decode("#828790")));
        add(navigation);

        content = new JPanel();
        content.setLayout(null);
        content.setBounds(220, 10, 740 + 60, 400 + 83 + 30);
        content.setBorder(BorderFactory.createLineBorder(Color.decode("#828790")));
        add(content);

        initViews();

        if(type){
            addButton(68, views.get("Services"));
            addButton(322, views.get("Spares"));
            addButton(195, views.get("Stats"));
            addButton(244, views.get("Profit"));
        }
        addButton(10, views.get("Home"));
        addButton(39, views.get("Orders"));
        addButton(117, views.get("Clients"));
        addButton(146, views.get("Staff"));
        addButton(351, views.get("Reserved"));

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        showView(views.get("Home"),true);
    }

    private void addButton(int y, JPanel panel) {
        JButton bt = new JButton(panel.toString());
        bt.setBounds(10, y, 180, 24);
        bt.setFocusable(false);
        bt.addActionListener((e) -> {
            if(panel instanceof AbstractView)
                showView((AbstractView)panel, true);
            else showPanel(panel);
        });
        navigation.add(bt);
    }


    public void showView(AbstractView view, boolean reset) {
        showPanel(loading);
        SwingUtilities.invokeLater(() -> {
//            if(reset) view.resetFilters();
//            view.updateData(-1, true);
            showPanel(view);
        });
    }

    public void showPanel(JPanel view) {
        content.removeAll();
        view.setBounds(1, 1, content.getWidth() - 2, content.getHeight() - 2);
        content.add(view);
        content.revalidate();
        content.repaint();
    }

    public AbstractView getView(String name) {
        return views.get(name);
    }

    private void initViews(){
        views.put("Home", new HomeView(this));
        views.put("Orders", new OrdersView(this));
        views.put("Services", new ServicesView(this));
        views.put("Clients", new ClientsView(this));
        views.put("Staff", new StaffView(this));
        views.put("Stats", new StatsView(this));
        views.put("Profit", new ProfitView(this));
        views.put("Spares", new SparesView(this));
        views.put("Reserved", new ReservedView(this));
    }
}