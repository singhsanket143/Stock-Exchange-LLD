package com.example.StockExchangeLLD.exceptions;

public class InvalidOrderException extends TradingException {
    
    public InvalidOrderException(String message) {
        super("Invalid order: " + message);
    }
    
    
}
