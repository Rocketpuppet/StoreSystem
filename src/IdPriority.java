public class IdPriority {
    String tag = "";
    int priority = 0;
    Order rawOrder = null;
    public IdPriority(Order order) {
        this.tag = order.tag;
        this.rawOrder = order;

        for (String letter : tag.split("")) {
            switch (letter) {
                case "A":
                    priority = 1;
                    return;

                case "B":
                    priority = 2;
                    return;

                case "C":
                    priority = 3;
                    return;
            }
            if(order.discount){
                priority = 0;
                return;
            }
            priority = 4;
        }
    }

}
