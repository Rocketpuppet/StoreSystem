public class ShimpmentItems {
    String itemName = null;
    int[] cords = {0, 0};
    int price = 0;

    public ShimpmentItems(String name, int[] cords){
        this.itemName = name;
        this.cords = cords;
    }
    public ShimpmentItems(Product product, int[] cords){
        this.itemName = product.productName;
        this.price = product.price;
    }

    public String name(){
        return itemName;
    }
    public int[] cords(){
        return cords;
    }
    public int x(){
        return cords[0];
    }
    public int y(){
        return cords[1];
    }

    public double distance(int x1, int y1, int x2, int y2){
        double distance = Math.sqrt((Math.pow(x2-x1,2))+(Math.pow(y2-y1,2)));
        return distance;
    }

    public double boxDistance(ShimpmentItems box2){
        return this.distance(this.x(),this.y(),box2.x(),box2.y());
    }
}
