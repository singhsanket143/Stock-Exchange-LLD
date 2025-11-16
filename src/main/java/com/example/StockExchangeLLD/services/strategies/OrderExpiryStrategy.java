package com.example.StockExchangeLLD.services.strategies;

import com.example.StockExchangeLLD.models.Order;

public interface OrderExpiryStrategy {
    
    boolean checkExpiry(Order order);
}
