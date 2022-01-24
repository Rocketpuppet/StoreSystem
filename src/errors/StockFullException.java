package errors;

public class StockFullException extends Exception{
    public StockFullException(String message){
        super(message);
    }
}
