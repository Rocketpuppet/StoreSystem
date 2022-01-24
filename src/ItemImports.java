import java.util.ArrayList;

public class ItemImports {
    int totalImportedItems;
    int totalItemsSold;
    float totalRevenue;
    ArrayList<ImportHandler> storeImports = new ArrayList<ImportHandler>();
    NormalStore store;
    Store Store;

    public ItemImports(NormalStore store) {
        this.store = store;

        for(ArrayList<ShimpmentItems> stock : store.stock.values()){
            storeImports.add(new ImportHandler(stock));
        }
    }

    public ItemImports(Store store){
        this.Store = store;

        for(ArrayList<ShimpmentItems> stock : Store.stock.values()){
            storeImports.add(new ImportHandler(stock));
        }
        for(ImportHandler imports : storeImports){
            totalImportedItems = totalImportedItems + imports.importAmount;
        }

    }

    public void updateImports() {
        for (ImportHandler imports : storeImports) {
            imports.updateHandler();
                totalItemsSold = totalItemsSold + imports.amountSold;
                totalRevenue = totalRevenue + imports.revenue;
        }
    }
}


