import errors.FreeException;
import errors.OutOfStockException;
import errors.StockFullException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Store {
    ArrayList<Product> products;
    HashMap<String, ArrayList<ShimpmentItems>> stock;
    Queue orders = new Queue();
    ItemImports importHandler;
    ArrayList<String> userIDs;
    ArrayList<Order> sales;

    public Store(){
        products = new ArrayList<>();
        stock = new HashMap<>();
        orders = new Queue();
        userIDs = new ArrayList<>();
        sales = new ArrayList<>();

        importHandler = new ItemImports(this);
    }

    public void readTransactions(File orderList) throws IOException {
        //Parsing the file to be read
        FileInputStream fLn = new FileInputStream(orderList);
        BufferedReader transactions = new BufferedReader(new InputStreamReader(fLn));
        String transactionData = transactions.readLine();
        String[] elements = {null, null, null};
        //Looping through the list of orders
        while (transactionData != null) {
            if (transactionData.length() == 21) {
                //If order reads as discount or sale
                //Data pulled out in substrings and placed into elements array
                elements[0] = transactionData.substring(0, 13).replace(" ", "");
                elements[1] = transactionData.substring(14,17);
                elements[2] = transactionData.substring(19);
                //New order is created and added to order queue with passed elements
                Order discount = new Order(elements[0], Float.parseFloat(elements[1]), Integer.parseInt(elements[2]));
                orders.enqueue(discount);
            } else {
                //Any real order (not a discount/sale)
                //Data pulled out in substrings and added to elements array and saved for new object creation
                elements[0] = transactionData.substring(0, 2);
                elements[1] = transactionData.substring(3, 12).replace(" ", "");
                elements[2] = transactionData.substring(14).replace("0", "").replace(" ", "");
                //Transaction user ID is added to userIDs arrayList
                addUser(elements[0]);
                //New order object is created and added to order queue
                Order transaction = new Order(Integer.parseInt(elements[2]), elements[1], elements[0]);
                orders.enqueue(transaction);
            }
            //Read the next line in the list of orders
            transactionData = transactions.readLine();
        }
    }

    public ArrayList<ShimpmentItems> getClosestBox(ArrayList<ShimpmentItems> itemList, ShimpmentItems targetItem) {
        //Initialize closest pair arrayList with targetBox and first box in the list (index 0)
        ArrayList<ShimpmentItems> shortestPair = new ArrayList<ShimpmentItems>();
        shortestPair.add(targetItem);
        shortestPair.add(itemList.get(0));
        //Loop through passed item list
        for (int i = 0; i < itemList.size() - 1; i++) {
            //Compare target box's distance to the other box in the list
            double dist = targetItem.boxDistance(shortestPair.get(1));
            //Compare distances to find box with the shortest distance to the targetBox
            if (dist == 0) {
                shortestPair.remove(1);
                shortestPair.add(itemList.get(i + 1));
            }
            if (dist > targetItem.boxDistance(itemList.get(i + 1))) {
                shortestPair.remove(1);
                shortestPair.add(itemList.get((i + 1)));
            }
        }
        //Return targetBox and it's closest box
        return shortestPair;
    }

    public ArrayList<ShimpmentItems> getPair(ArrayList<ShimpmentItems> boxList, ShimpmentItems targetBox, int box) {
        //Initialize arrayList to hold the closest pair of boxes (targetBox and the first box in the list are added (index 0))
        ArrayList<ShimpmentItems> pair = new ArrayList<ShimpmentItems>();
        pair.add(targetBox);
        pair.add(boxList.get(0));
        //Loop through box list using recursion.
        for (int i = box; i < boxList.size(); i++) {
            //Get the closest box to targetBox in the box list provided
            pair = this.getClosestBox(boxList, targetBox);
            //Find the next closest pair of boxes recursively by calling the function with the next box in the list
            ArrayList<ShimpmentItems> pair2 = this.getPair(boxList, boxList.get(i), box + 1);
            double dist = pair.get(0).boxDistance(pair.get(1));
            double dist2 = pair2.get(0).boxDistance(pair2.get(1));
            //Compare distance to find and return the shortest pair
            if (dist == 0) {
                return pair2;
            }
            if (dist2 == 0) {
                return pair;
            }
            if (dist < dist2 && dist != 0) {
                return pair;
            }
            if (dist2 < dist && dist2 != 0) {
                return pair2;
            }
            if(dist==dist2){
                return pair;
            }
        }
        return pair;
    }

    public ArrayList<ShimpmentItems> getClosestPairs(ArrayList<ShimpmentItems> items) {
        //Initialize empty arrayLists
        ArrayList<ShimpmentItems> closestPair = new ArrayList<ShimpmentItems>();
        ArrayList<ShimpmentItems> sortedItems = new ArrayList<ShimpmentItems>();
        //Loop through list with recursion
        if (items.size() > 0) {
            //Get the first closest pair of boxes and remove the from the passed item list
            closestPair = this.getPair(items, items.get(0), 0);
            items.remove(closestPair.get(0));
            items.remove(closestPair.get(1));
            //Recursive call to be called until the if statement above has been reached (passed item list is completely empty)
            sortedItems = this.getClosestPairs(items);
            //Add the closest pair of items in order using recursion and the returned item list.
            sortedItems.add(closestPair.get(0));
            sortedItems.add(closestPair.get(1));

        }
        return sortedItems;
    }

    public void sortItem(ShimpmentItems item) throws StockFullException {
        //Adds item to it's stock in the store if theres enough storage.
        if (stock.get(item.itemName).size() < 24) {
            stock.get(item.itemName).add(item);
        } else {
            throw new StockFullException("Item stock of " + item.itemName + " is too full and cannot be added");
        }
    }

    public void sortTruck(ArrayList<ShimpmentItems> shipmentItems) throws StockFullException {
        //Initializes the truck stock by sorting the list of items provided using closestPairs function
        ArrayList<ShimpmentItems> truckStock = this.getClosestPairs(shipmentItems);
        //Loops thorough truck stock sorting each item and adding to it's correct storage and remove it from the truck
        while (truckStock.size() > 0) {
            this.sortItem(truckStock.get(0));
            truckStock.remove(0);
        }
        //Redefine importHandler with all imported items.
        importHandler = new ItemImports(this);
        importHandler.updateImports();
    }

    public ArrayList<ShimpmentItems> getItemList(String item) {
        //Returns ArrayList<ShipmentItems> from HashMap with passed item name
        if (stock.get(item) != null) {
            return stock.get(item);
        } else {
            return null;
        }
    }

    public float getPrice(Order order) throws FreeException {
        //Checks for any sale that can be applied to the order
        Order sale = this.checkSale(order);
        //Loops thorough list of products to get base price.
        for (Product products : products) {
            if (order.item.equals(products.productName)) {
                //Check if there a valid sale and return if true
                if (sale != null) {
                    return (products.price - (products.price * sale.sale));
                } else {
                    //Return products base price
                    return products.price;
                }
            }
        }
        return 0;
    }

    public Order checkSale(Order order) throws FreeException {
        //Check all current store sales
            for (Order sale : sales) {
                //Check sale duration
                if (sale.length > 0) {
                    //Check sale amount to ensure the product is not free
                    if (sale.sale < 100) {
                        if (order.item.equals(sale.item) || sale.item.equals("storewide")) {
                            //Return valid sale
                            sale.length -= 1;
                            return sale;
                        }
                    } else {
                        throw new FreeException("Sale of " + sale.item + " is too large and cannot be applied");
                    }
                } else {
                    VoidSale(sale);
                    return null;
                }
            }
        return null;
    }

    public void applySale(Order discount) {
        this.sales.add(discount);
    }

    public void VoidSale(Order sale) {
        for (int i = 0; i < sales.size(); i++) {
            if (sales.get(i) == sale) {
                sales.remove(i);
            }

        }
    }

    public float purchase(Order order) throws OutOfStockException, FreeException {
        //Find price of the order (any sales applied automatically)
        float price = this.getPrice(order);
        int amountBought = 0;
        //Buys the targeted number of items
        for (int i = 0; i != order.amount; i++) {
            //Check if item is in stock and remove one unit
            if (this.getItemList(order.item).size() > 0) {
                this.getItemList(order.item).remove(0);
                System.out.println("Item " + order.item + " successfully purchased for " + price);
                amountBought+=1;
            } else {
                throw new OutOfStockException("Item " + order.item + " is out of stock");
            }
        }
        //Check if customer was able to purchase all items
        if(amountBought==order.amount) {
            System.out.println("Order successfully completed for total price of " + price * order.amount);
        } else {
            System.out.println("Order completed for total price " + price*amountBought + ". Amount missing: " + (order.amount-amountBought));
        }
        //Update item's import handler to track items revenue
        for(ImportHandler imports : importHandler.storeImports){
            if(order.item.equals(imports.item)){
                imports.revenue = imports.revenue+price*amountBought;
            }
        }

        return price * order.amount;
    }

    public void purchaseOrders() throws OutOfStockException, FreeException {
        //Loop through queue of orders
        while (orders.getSize() > 0) {
            //If order is a sale apply it instead of calling purchase function
            if (orders.peek().discount) {
                this.applySale(orders.dequeue());
            } else {
                //Purchase with passed order
                this.purchase(orders.dequeue());
            }
        }
        //Update importHandler to account for total revenue and total amount sold
        importHandler.updateImports();
    }

    public void addUser(String ID) {
        for (String userID : this.userIDs) {
            if (userID.equals(ID)) {
                return;
            }
        }
        userIDs.add(ID);
    }




}
