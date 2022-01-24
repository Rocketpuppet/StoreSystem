import errors.FreeException;
import errors.OutOfStockException;
import errors.StockFullException;
import java.util.Random;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class main {

    public static void main(String[] args) throws IOException, OutOfStockException, StockFullException, FreeException {
        Random rand = new Random();
        ArrayList<ShimpmentItems> truckStock = new ArrayList<>();
        PremiumStore store = new PremiumStore();
        for(int i=0;i<6;i++){
            truckStock.add(new ShimpmentItems("shirt",new int[]{rand.nextInt(10),rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems("hoodie", new int[]{rand.nextInt(10),rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems("socks",new int[]{rand.nextInt(10),rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems("tv", new int[]{rand.nextInt(10),rand.nextInt(10)}));
        }
        store.sortTruck(truckStock);
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\PremiumStoreOrderList"));
        store.purchaseOrders();
    }
}

