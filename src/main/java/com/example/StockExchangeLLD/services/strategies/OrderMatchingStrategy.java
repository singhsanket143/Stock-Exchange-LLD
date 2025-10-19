package com.example.StockExchangeLLD.services.strategies;

import java.util.List;

import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.Trade;

public interface OrderMatchingStrategy {
    
    String getStrategyName();

    List<Trade> matchOrders(Order newOrder, List<Order> existingOrders);
}
