import errors.FreeException;
import errors.OutOfStockException;
import errors.StockFullException;

import java.io.*;
import java.util.*;

public class NormalStore extends Store{
    Product carpet = new Product("carpets", 30);
    Product curtain = new Product("curtains", 35);
    Product towel = new Product("towels", 10);
    Product table = new Product("tables", 85);

    ArrayList<ShimpmentItems> carpets = new ArrayList<>();
    ArrayList<ShimpmentItems> curtains = new ArrayList<>();
    ArrayList<ShimpmentItems> towels = new ArrayList<>();
    ArrayList<ShimpmentItems> tables = new ArrayList<>();

    public NormalStore() {

        stock.put("carpets", carpets);
        stock.put("curtains", curtains);
        stock.put("towels", towels);
        stock.put("tables", tables);

        products.add(carpet);
        products.add(curtain);
        products.add(towel);
        products.add(table);

    }


    public ShimpmentItems[] getClosestPair(ShimpmentItems[] items) {
        ShimpmentItems[] pair = {null, null};
        double dist = 0;
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items.length; j++) {
                double dist1 = items[i].boxDistance(items[j]);
                if (dist == 0) {
                    pair[0] = items[i];
                    pair[1] = items[j];
                }
                if (dist1 < dist && dist1 != 0) {
                    pair[0] = items[i];
                    pair[1] = items[j];
                }
            }
            dist = pair[0].boxDistance(pair[1]);
        }
        return pair;
    }

    public double getDist(ShimpmentItems targetBox, ShimpmentItems[] boxList, int box) {
        double ShortestDist = 0;
        ShimpmentItems[] pair;
        for (int i = box; i < boxList.length; i++) {
            double dist = targetBox.boxDistance(boxList[i]);
            double dist2 = this.getDist(boxList[i], boxList, box + 1);
            if (dist < dist2 && dist != 0) {
                return dist;
            } else {
                return dist2;
            }
        }
        return targetBox.boxDistance(boxList[0]);
    }

    public ArrayList<ShimpmentItems> getPair(ArrayList<ShimpmentItems> boxList, ShimpmentItems targetBox, int box) {
        return super.getPair(boxList,targetBox,box);
    }

    public ArrayList<ShimpmentItems> getClosestBox(ArrayList<ShimpmentItems> itemList, ShimpmentItems targetItem) {
        return super.getClosestBox(itemList,targetItem);
    }

    public ArrayList<ShimpmentItems> getClosestPairs(ArrayList<ShimpmentItems> items) {
        return super.getClosestPairs(items);
    }

    public void sortTruck(ArrayList<ShimpmentItems> shipmentItems) throws StockFullException {
        super.sortTruck(shipmentItems);
    }

    public void sortItem(ShimpmentItems item) throws StockFullException {
        super.sortItem(item);
    }

    public float purchase(Order order) throws OutOfStockException, FreeException {
        return super.purchase(order);
    }

    public ArrayList<ShimpmentItems> getItemList(String item) {
        return super.getItemList(item);
    }

    public float getPrice(Order order) throws FreeException {
        return super.getPrice(order);
    }

    public void readTransactions(File orderList) throws IOException {
        super.readTransactions(orderList);
    }

    public void purchaseOrders() throws OutOfStockException, FreeException {
        super.purchaseOrders();
    }

    public void applySale(Order discount) {
        super.applySale(discount);
    }

    public Order checkSale(Order order) throws FreeException {
        return super.checkSale(order);
    }

    public void VoidSale(Order sale) {
       super.VoidSale(sale);
    }

    public void addUser(String ID) {
        super.addUser(ID);
    }
}



