package com.example.StockExchangeLLD.data;

import java.util.List;
import java.util.Optional;

import com.example.StockExchangeLLD.models.Order;

public interface IOrderBook {

    void addOrder(Order order);

    boolean removeOrder(String orderId, String stockSymbol);

    boolean updateOrder(Order updatedOrder);

    List<Order> getOrders(String stockSymbol);

    Optional<Order> getOrderById(String orderId);
    
    
} 