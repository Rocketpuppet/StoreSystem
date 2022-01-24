import errors.FreeException;
import errors.OutOfStockException;
import errors.StockFullException;

import java.beans.beancontext.BeanContextMembershipEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PremiumStore extends Store{

    Product shirt = new Product("shirt", 90);
    Product hoodie = new Product("hoodie",150);
    Product tv = new Product("tv",2000);
    Product sneakers = new Product("sneakers",180);
    Product socks = new Product("socks",70);
    Product laptop = new Product("laptop",1000);
    //PriorityQueue orders = new PriorityQueue();


    public PremiumStore(){
        products.add(shirt);
        products.add(hoodie);
        products.add(tv);
        products.add(sneakers);
        products.add(socks);
        products.add(laptop);

        for(Product product : products){
            stock.put(product.productName,new ArrayList<ShimpmentItems>());
        }
        orders = new PriorityQueue();
    }

    @Override
    public float purchase(Order order) throws OutOfStockException, FreeException {
        if(order.discount){
            this.applySale(order);
            return 0;
        }
        float price = this.getPrice(order);
        int amountBought = 0;
        for(int i=0; i!=order.amount;i++){
            if(this.getItemList(order.item).size()>0){
                this.getItemList(order.item).remove(0);
                System.out.println("Item " + order.item + " successfully purchased for " + price);
                amountBought+=1;
            } else {
                throw new OutOfStockException(order.item + " is out of stock");
            }
        }
        if(amountBought==order.amount){
            System.out.println("Order of " + order.item + " successfully completed for a total of " + price*order.amount);
        } else {
            System.out.println("Order of " + order.item + " completed for a total of " + price*amountBought);
        }
        for(ImportHandler imports : importHandler.storeImports){
            if(imports.item.equals(order.item)){
                imports.revenue += price*amountBought;
            }
        }
        return (price*amountBought);
    }


    public void readTransactions(File orderList) throws IOException {
        super.readTransactions(orderList);
    }

    public ArrayList<ShimpmentItems> getClosestBox(ArrayList<ShimpmentItems> itemList, ShimpmentItems targetItem) {
        return super.getClosestBox(itemList, targetItem);
    }

    @Override
    public ArrayList<ShimpmentItems> getItemList(String item) {
        return super.getItemList(item);
    }

    @Override
    public ArrayList<ShimpmentItems> getClosestPairs(ArrayList<ShimpmentItems> items) {
        return super.getClosestPairs(items);
    }

    @Override
    public ArrayList<ShimpmentItems> getPair(ArrayList<ShimpmentItems> boxList, ShimpmentItems targetBox, int box) {
        return super.getPair(boxList, targetBox, box);
    }
/*
    public float getPrice(Order order) throws FreeException {
        Order sale = this.checkSale(order);
        for(Product products : products){
            if(order.item.equals(products.productName)) {
                if (sale != null) {
                    return (products.price - (products.price * sale.sale));
                } else {
                    return products.price;
                }
            }
        }
        return 0;
    }
 */

    public Order checkSale(Order order) throws FreeException {
        Order sale = null;
        if (sales.size() > 0) {
            for (Order sales : sales) {
                if (sales.item.equals(order.item)||sales.item.equals("storewide")) {
                    sale = sales;
                }
            }
            if (sale != null) {
                if (sale.amount >= 100) {
                    throw new FreeException("Sale on " + sale.item + " is too high and cannot be appiled");
                }
            }
        }
        if(order.tag.contains("A")&&sale!=null){
            sale.sale += 0.1;
        }
        if(sale==null&&order.tag.contains("A")){
            return new Order(order.item, 0.1F,1);
        }
        return sale;
    }

    @Override
    public void addUser(String ID) {
        super.addUser(ID);
    }

    @Override
    public void applySale(Order discount) {
        super.applySale(discount);
    }

    @Override
    public void purchaseOrders() throws OutOfStockException, FreeException {
        super.purchaseOrders();
    }

    @Override
    public void sortItem(ShimpmentItems item) throws StockFullException {
        super.sortItem(item);
    }

    @Override
    public void sortTruck(ArrayList<ShimpmentItems> shipmentItems) throws StockFullException {
        super.sortTruck(shipmentItems);
    }

    @Override
    public void VoidSale(Order sale) {
        super.VoidSale(sale);
    }
}
