package me.svistoplyas.teamdev.graphics.views;

import me.svistoplyas.teamdev.graphics.MainFrame;
import me.svistoplyas.teamdev.graphics.TableModel;
import me.svistoplyas.teamdev.graphics.editForms.AbstractEdit;
import me.svistoplyas.teamdev.graphics.editForms.ClientForm;
import net.web_kot.teamdev.db.entities.Client;

import java.util.List;

public class ClientsView extends AbstractView {

    public ClientsView(MainFrame _mainFrame) {
        super(_mainFrame);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{"Имя", "Номер телефона"};
    }

    @Override
    Object[][] getData() {
        try {
            List<Client> clients = mainFrame.model.getClients();
            Object[][] ans = new Object[clients.size()][];
            int i = 0;
            for (Client client : clients) {
                ans[i] = new Object[]{client.getName(), client.getPhone()};
                i++;
            }

            return ans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    Object getObject(int row) {
        try {
            return mainFrame.model.getClients().get(row);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    boolean canAdd() {
        return true;
    }

    @Override
    boolean canEdit() {
        return true;
    }

    @Override
    boolean canDelete() {
        return true;
    }

    @Override
    void performDelete(int row) throws Exception {
        mainFrame.model.getClients().get(row).delete();
    }

    @Override
    public AbstractEdit getEdit(boolean b, Object o) {
        return new ClientForm(mainFrame, b, o);
    }

    @Override
    public String toString() {
        return "Клиенты";
    }
}
