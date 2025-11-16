package com.example.StockExchangeLLD.services.strategies.impl;

import java.time.LocalDateTime;

import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.OrderStatus;
import com.example.StockExchangeLLD.services.strategies.OrderExpiryStrategy;

public class IntradayOrderExpiryStrategy implements OrderExpiryStrategy {
    
    @Override
    public boolean checkExpiry(Order order) {
        // check if the current time stamp is beyond 3.30PM and order is older than that.

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime orderAcceptedTimeStamp = order.getOrderAcceptedTimeStamp();
        if(now.isAfter(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 15, 30)) && orderAcceptedTimeStamp.isBefore(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 15, 30))) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }
}
