import java.util.ArrayList;

public class PriorityQueue extends Queue {

    ArrayList<IdPriority> contentID = new ArrayList<>();

    public PriorityQueue() {
        super();
        contentID = new ArrayList<>();
    }

    public void enqueue(Order order) {
        IdPriority id = new IdPriority(order);
        int i = 0;

        if (contentID.size() > 0) {
            while (i<contentID.size()&&id.priority > this.contentID.get(i).priority) {
                i += 1;
            }
            contentID.add(i, id);
        } else {
            contentID.add(id);
        }

    }
    @Override
    public int getSize(){
        return contentID.size();
    }
    public Order dequeue(){
        return contentID.remove(0).rawOrder;
    }
    public Order peek(){return contentID.get(0).rawOrder;}
}
