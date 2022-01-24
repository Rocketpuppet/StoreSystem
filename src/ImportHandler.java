import java.util.ArrayList;

public class ImportHandler {
    ArrayList<ShimpmentItems> targetStock;
    String item = " ";
    int importAmount;
    int amountSold;
    float revenue;

    public ImportHandler(ArrayList<ShimpmentItems> item){
        this.targetStock = item;
        if(item.size()>0){
            this.item = item.get(0).itemName;
        }
        this.importAmount = item.size();
        this.amountSold = 0;
    }

    public void updateHandler(){
        this.amountSold = this.importAmount - this.targetStock.size();
    }
}
