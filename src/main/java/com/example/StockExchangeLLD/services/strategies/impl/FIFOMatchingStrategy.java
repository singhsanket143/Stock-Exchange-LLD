package com.example.StockExchangeLLD.services.strategies.impl;

import java.util.List;

import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.Trade;
import com.example.StockExchangeLLD.services.strategies.OrderMatchingStrategy;

public class FIFOMatchingStrategy implements OrderMatchingStrategy{
    
    @Override
    public String getStrategyName() {
        return "FIFO";
    }

    @Override
    public List<Trade> matchOrders(Order newOrder, List<Order> existingOrders) {
        return null;
    }
}
