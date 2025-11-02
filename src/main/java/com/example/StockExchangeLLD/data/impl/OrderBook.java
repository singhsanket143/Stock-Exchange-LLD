package com.example.StockExchangeLLD.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.example.StockExchangeLLD.models.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderBook implements IOrderBook{

    
    private final ConcurrentMap<String, List<Order>> orderBook = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ReadWriteLock> symbolLocks = new ConcurrentHashMap<>();


    public void addOrder(Order order) {
        // 1. Figure out the stock symbol

        String stockSymbol = order.getStockSymbol();

        ReadWriteLock lock = getOrCreateLock(stockSymbol);

        lock.writeLock().lock();

        try {
            orderBook.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(order);
            log.info("Order added to order book: {} - {} - {} - {} - {} - {}", order.getOrderId(), order.getUserId(), order.getStockSymbol(), order.getQuantity(), order.getPrice(), order.getOrderType());
        } finally {
            lock.writeLock().unlock();
        }


    }

    public boolean removeOrder(String orderId, String stockSymbol) {
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();
        try {
            List<Order> orders = orderBook.get(stockSymbol);

            if(orders != null) {
                boolean removed = orders.removeIf(order -> order.getOrderId().equals(orderId));
                if(removed) {
                    log.info("Order removed from order book");
                } else {
                    log.info("Order not found in order book");

                }
                return removed;
            }

            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private ReadWriteLock getOrCreateLock(String stockSymbol) {
        return symbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());
    }
}
