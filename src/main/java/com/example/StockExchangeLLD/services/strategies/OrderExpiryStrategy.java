package com.example.StockExchangeLLD.services.strategies;

import com.example.StockExchangeLLD.models.Order;

public interface OrderExpiryStrategy {
    
    void checkExpiry(Order order);
}
