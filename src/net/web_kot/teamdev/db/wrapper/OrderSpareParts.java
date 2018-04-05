package net.web_kot.teamdev.db.wrapper;

import me.svistoplyas.teamdev.graphics.utils.Converter;
import net.web_kot.teamdev.db.Model;
import net.web_kot.teamdev.db.entities.Service;
import net.web_kot.teamdev.db.entities.SparePart;

import java.io.PrintWriter;
import java.util.*;

@SuppressWarnings("SqlResolve")
public class OrderSpareParts {
    
    private final Model model;
    private final boolean blocked;
    private boolean invalid = false;
    
    private HashMap<Integer, TreeMap<Integer, PartPriceWrapper>> data = new HashMap<>();
    
    public OrderSpareParts(Model model) {
        this(model, false);
    }
    
    public OrderSpareParts(Model model, boolean b) {
        this.model = model; blocked = b;
    }
    
    public void addCurrent(int id, int sparePart, long date, int quantity) throws Exception {
        int price = model.getSparePartById(sparePart).getPrice(new Date(date));
        
        TreeMap<Integer, PartPriceWrapper> map = getPartData(sparePart);
        map.put(price, new PartPriceWrapper(id, quantity));
    }
    
    public void removeSparePart(SparePart part, int quantity) throws Exception {
        if(invalid) throw new Exception("This wrapper is no longer valid");
        
        TreeMap<Integer, PartPriceWrapper> map = getPartData(part.getId());
        int original = quantity;
        
        for(Map.Entry<Integer, PartPriceWrapper> entry : map.descendingMap().entrySet()) {
            PartPriceWrapper wrapper = entry.getValue();
            
            int canRemove = wrapper.amount;
            if(blocked) canRemove -= wrapper.reserved;
            
            if(canRemove == 0) break;
            
            if(canRemove >= quantity) {
                wrapper.amount -= quantity;
                quantity = 0;
                break;
            } else {
                wrapper.amount -= canRemove;
                quantity -= canRemove;
            }
        }
        
        if(quantity == original) throw new Exception("Невозможно удалить данную запасную часть!");
        if(quantity != 0) {
            String q = Converter.getInstance().beautifulQuantity(original - quantity, part.getUnit());
            throw new Exception("Было удалено только " + q + " " + part.getUnit() + " данной запасной части!");
        }
    }
    
    public void addSparePart(SparePart part, int quantity) throws Exception {
        if(invalid) throw new Exception("This wrapper is no longer valid");
        
        TreeMap<Integer, PartPriceWrapper> map = getPartData(part.getId());
        int price = part.getPrice();
        
        for(Map.Entry<Integer, PartPriceWrapper> entry : map.entrySet()) {
            PartPriceWrapper wrapper = entry.getValue();
            
            int canAdd = wrapper.reserved - wrapper.amount;
            if(price == entry.getKey()) canAdd = Integer.MAX_VALUE;
            
            if(canAdd >= quantity) {
                wrapper.amount += quantity;
                quantity = 0;
                break;
            } else {
                wrapper.amount = wrapper.reserved;
                quantity -= canAdd;
            }
        }
        
        if(quantity != 0) {
            PartPriceWrapper wrapper = new PartPriceWrapper(quantity);
            map.put(price, wrapper);
        }
    }
    
    private TreeMap<Integer, PartPriceWrapper> getPartData(int identifier) {
        TreeMap<Integer, PartPriceWrapper> map = data.computeIfAbsent(identifier, (e) -> new TreeMap<>());
        data.put(identifier, map);
        return map;
    }
    
    public void saveFor(int idOrder) throws Exception {
        invalid = true;
        
        for(Map.Entry<Integer, TreeMap<Integer, PartPriceWrapper>> entry : data.entrySet()) {
            for(Map.Entry<Integer, PartPriceWrapper> e : entry.getValue().entrySet()) {
                PartPriceWrapper wrapper = e.getValue();
                
                if(wrapper.identifier != -1) {
                    if(wrapper.amount != 0)
                        model.db().update(
                                "UPDATE Spare_part_Order SET Quantity = %d WHERE PK_Spare_part_Order = %d",
                                wrapper.amount, wrapper.identifier
                        );
                    else 
                        model.db().exec(
                                "DELETE FROM Spare_part_Order WHERE PK_Spare_part_Order = %d", 
                                wrapper.identifier
                        );
                } else {
                    if(wrapper.amount != 0) 
                        model.db().insert(
                                "INSERT INTO Spare_part_Order (PK_Spare_part, PK_Order, `Date`, Quantity) " + 
                                        "VALUES (%d, %d, %d, %d)", 
                                entry.getKey(), idOrder, System.currentTimeMillis(), wrapper.amount
                        );
                }
            }
        }
    }
    
    public void print() throws Exception {
        for(Map.Entry<Integer, TreeMap<Integer, PartPriceWrapper>> entry : data.entrySet()) {
            System.out.println(model.getSparePartById(entry.getKey()));

            for(Map.Entry<Integer, PartPriceWrapper> e : entry.getValue().entrySet())
                System.out.println("-- " + e.getKey() + " x " + e.getValue().amount);
        }
    }
    
    public Object[][] getData() throws Exception {
        ArrayList<Object[]> result = new ArrayList<>(data.size());
        for(Map.Entry<Integer, TreeMap<Integer, PartPriceWrapper>> entry : data.entrySet()) {
            int count = 0;
            long price = 0;
            for(Map.Entry<Integer, PartPriceWrapper> e : entry.getValue().entrySet()) {
                PartPriceWrapper wrapper = e.getValue();
                
                count += wrapper.amount;
                price += wrapper.amount * e.getKey();
            }
            if(count == 0) continue;
            
            SparePart part = model.getSparePartById(entry.getKey());
            result.add(new Object[] {
                    part.getName(), 
                    Converter.getInstance().beautifulQuantity(count, part.getUnit()) + " " + part.getUnit(),
                    Converter.getInstance().convertPriceToStr((int)(price / 100)),
                    part,
                    count
            });
        }
        
        Object[][] ret = new Object[result.size()][];
        for(int i = 0; i < result.size(); i++) ret[i] = result.get(i);
        return ret;
    }
    
    public int getPrice() {
        long price = 0;
        for(Map.Entry<Integer, TreeMap<Integer, PartPriceWrapper>> entry : data.entrySet())
            for(Map.Entry<Integer, PartPriceWrapper> e : entry.getValue().entrySet()) {
                PartPriceWrapper wrapper = e.getValue();
                price += wrapper.amount * e.getKey();
            }
        return (int)(price / 100);
    }
    
    public int getAmount(SparePart part) {
        if(!data.containsKey(part.getId())) return 0;
        
        int amount = 0;
        for(Map.Entry<Integer, PartPriceWrapper> e : data.get(part.getId()).entrySet())
            amount += e.getValue().amount;
        
        return amount;
    }
    
    public int writeToDocument(PrintWriter out) throws Exception {
        int i = 0;
        long total = 0;
        for(Map.Entry<Integer, TreeMap<Integer, PartPriceWrapper>> entry : data.entrySet()) {
            SparePart part = model.getSparePartById(entry.getKey());

            out.print(String.format("%3s", (i + 1) + ""));
            out.print(". ");
            out.print(String.format("%-40s", part.getName()));
            
            int j = 0;
            for(Map.Entry<Integer, PartPriceWrapper> e : entry.getValue().entrySet()) {
                if(j != 0) out.print(String.format("%-45s", ""));
                
                out.print(String.format("%8s", Converter.getInstance().beautifulQuantity(e.getValue().amount, part.getUnit())));
                out.print(" x ");
                out.print(String.format("%-14s", (e.getKey() / 100D) + " p."));
                
                out.println(((e.getValue().amount * e.getKey() / 100) / 100D) + " р.");
                
                total += e.getValue().amount * e.getKey();
                j++;
            }
            i++;
        }
        return (int)(total / 100);
    }
    
    private static class PartPriceWrapper {
        
        public final int identifier;
        public final int reserved;
        public int amount;
        
        public PartPriceWrapper(int id, int cnt) {
            identifier = id; reserved = amount = cnt;
        }
        
        public PartPriceWrapper(int cnt) {
            identifier = -1; reserved = 0; amount = cnt;
        }
        
    }
    
}
