import java.util.ArrayList;

public class Queue {
    ArrayList<Order> content;
    public Queue(){
        content = new ArrayList<Order>();
    }

    public void enqueue(Order item){
        content.add(item);
    }
    public Order dequeue(){
        return content.remove(0);
    }
    public int getSize() {
        return content.size();
    }
    public void getItems() {
        for (Order order : content) {
            if (order.discount) {
                order.returnDiscount();
            } else {
                order.returnOrder();
            }
        }
    }
    public Order peek(){
        return content.get(0);
    }
}
