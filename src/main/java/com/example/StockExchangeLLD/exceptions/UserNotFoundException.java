package com.example.StockExchangeLLD.exceptions;

public class UserNotFoundException extends TradingException {
    public UserNotFoundException(String userId) {
        super("User not found with id: " + userId);
    }
}
