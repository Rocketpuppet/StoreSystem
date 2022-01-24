package errors;

public class StoreError extends Exception{
    public StoreError(int errorCode, String item) throws OutOfStockException, StockFullException {
        switch (errorCode) {
            case 1 : throw new OutOfStockException("Item " + item + " is no longer in stock");
            case 2 : throw new StockFullException("Item stock " + item + " is full");
        }
    }
}
