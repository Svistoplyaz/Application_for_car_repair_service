package net.web_kot.teamdev.db.wrappers;

import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.Model;
import net.web_kot.teamdev.db.entities.Order;
import net.web_kot.teamdev.db.entities.SparePart;

import java.util.ArrayList;
import java.util.List;

public class Profit {
    
    private Object[][] spending, income;
    private int totalSpending = 0, totalIncome = 0, profit;
    
    public Profit(Model model, ArrayList<Object[]> purchases, List<Order> orders) throws Exception {
        spending = new Object[purchases.size()][];
        for(int i = 0; i < spending.length; i++) {
            Object[] data = purchases.get(i);
            SparePart part = model.getSparePartById((int)data[0]);
            
            int price = (int)((long)(int)data[1] * (int)data[2] / 100);
            totalSpending += price;
            
            spending[i] = new Object[] {
                    part.getName(),
                    Converter.getInstance().beautifulQuantity((int)data[1], part.getUnit()),
                    Converter.getInstance().convertPriceToStr(price)
            };
        }
        
        income = new Object[orders.size()][];
        for(int i = 0; i < income.length; i++) {
            Order o = orders.get(i);
            
            int price = o.getPrice() + o.getSpareParts().getPrice();
            totalIncome += price;
            
            income[i] = new Object[] {
                    "№" + o.getId() + " (" + o.getClient().getName() + ")",
                    Converter.getInstance().convertPriceToStr(price)
            };
        }
        
        profit = totalIncome - totalSpending;
    }
    
    public Object[][] getIncomeData() {
        return income;
    }
    
    public Object[][] getSpendingData() {
        return spending;
    }
    
    public int getTotalIncome() {
        return totalIncome;
    }
    
    public int getTotalSpending() {
        return totalSpending;
    }
    
    public int getProfit() {
        return profit;
    }
    
}
