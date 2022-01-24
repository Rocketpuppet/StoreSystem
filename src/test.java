import errors.FreeException;
import errors.OutOfStockException;
import errors.StockFullException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;


public class test {

    Random rand = new Random();
    NormalStore store = new NormalStore();
    ArrayList<ShimpmentItems> truckStock = new ArrayList<ShimpmentItems>();
    FileInputStream fLn;
    BufferedReader reader;

    Product curtain = new Product("curtains", 35);
    Product carpet = new Product("carpets", 30);
    Product towel = new Product("towels", 10);
    Product tables = new Product("tables", 85);

    public test() throws FileNotFoundException {
        fLn = new FileInputStream("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions");
        reader = new BufferedReader(new InputStreamReader(fLn));
    }


    @Test
    public void checkStock() throws StockFullException {
        for (int i = 0; i < 3; i++) {
            truckStock.add(new ShimpmentItems("tables", new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems("towels", new int[]{rand.nextInt(10), rand.nextInt(10)}));
        }
        store.sortTruck(truckStock);

        assertEquals(3, store.stock.get("tables").size());
        assertEquals(3, store.stock.get("towels").size());
    }

    @Test
    public void checkOrders() throws IOException {
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions"));
        assertEquals(5, store.orders.getSize());
    }

    @Test
    public void checkTotalImports() throws StockFullException, IOException, OutOfStockException, FreeException {
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions"));
        for (int i = 0; i < 3; i++) {
            truckStock.add(new ShimpmentItems("tables", new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems("towels", new int[]{rand.nextInt(10), rand.nextInt(10)}));
        }
        store.sortTruck(truckStock);
        store.purchaseOrders();

        assertEquals(6, store.importHandler.totalImportedItems);
    }

    @Test
    public void checkTotalSales() throws IOException, OutOfStockException, StockFullException, FreeException {
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions2"));
        for (int i = 0; i < 3; i++) {
            truckStock.add(new ShimpmentItems(curtain, new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(carpet, new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(tables, new int[]{rand.nextInt(10), rand.nextInt(10)}));
        }

        store.sortTruck(truckStock);
        store.purchaseOrders();
        assertEquals(3, store.importHandler.storeImports.get(3).amountSold);
    }

    @Test
    public void checkTotalRevenue() throws IOException, OutOfStockException, StockFullException, FreeException {
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions2"));
        for (int i = 0; i < 3; i++) {
            truckStock.add(new ShimpmentItems(curtain, new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(carpet, new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(tables, new int[]{rand.nextInt(10), rand.nextInt(10)}));
        }
        store.sortTruck(truckStock);
        store.purchaseOrders();

        assertEquals(250, store.importHandler.totalRevenue);
    }

    @Test
    public void checkUserIDs() throws IOException {
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions2"));
        for (int i = 0; i < 3; i++) {
            truckStock.add(new ShimpmentItems(carpet, new int[]{rand.nextInt(10), rand.nextInt(10)}));
        }

        assertEquals(5, store.userIDs.size());
    }

    @Test
    public void checkRevenue() throws IOException, OutOfStockException, StockFullException, FreeException {
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions2"));
        for (int i = 0; i < 8; i++) {
            truckStock.add(new ShimpmentItems(carpet, new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(curtain, new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(tables, new int[]{rand.nextInt(10), rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(towel, new int[]{rand.nextInt(10), rand.nextInt(10)}));
        }
        store.sortTruck(truckStock);
        store.purchaseOrders();

        assertEquals(405, store.importHandler.totalRevenue);
    }

    @Test
    public void stockFullError() throws StockFullException {
        for (int i = 0; i < 26; i++) {
            truckStock.add(new ShimpmentItems(carpet, new int[]{rand.nextInt(10), rand.nextInt(10)}));
        }
        store.sortTruck(truckStock);
    }

    @Test
    public void FreeError() throws FreeException, IOException, OutOfStockException {
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions3"));
        store.purchaseOrders();
    }

    @Test
    public void voidSale() throws IOException, OutOfStockException, FreeException, StockFullException {
        for(int i=0;i<10;i++){
            truckStock.add(new ShimpmentItems(curtain, new int[]{rand.nextInt(10),rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(carpet, new int[]{rand.nextInt(10),rand.nextInt(10)}));
            truckStock.add(new ShimpmentItems(tables, new int[]{rand.nextInt(10),rand.nextInt(10)}));
        }
        store.sortTruck(truckStock);
        store.readTransactions(new File("C:\\Users\\user\\Downloads\\CompanyTroubles\\src\\textFiles\\testTransactions"));
        store.purchaseOrders();
        System.out.println(store.importHandler.totalRevenue);
    }
}


