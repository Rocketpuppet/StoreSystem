public class Order {
    int amount = 0;
    String item = "";
    String tag = "";
    float sale = 0;
    boolean discount = false;
    int length = 0;

    public Order(int quantity, String name, String ID){
        this.amount = quantity;
        this.item = name;
        this.tag = ID;
        this.discount = false;

    }
    public Order(String item, float sale, int length){
        this.item = item;
        this.sale = sale;
        this.discount = true;
        this.length = length;

    }

    public void returnOrder(){
        System.out.println(this.tag + " " + this.item + " " + this.amount);
    }
    public void returnDiscount(){
        System.out.println(this.item + " " + this.sale);
    }
}
