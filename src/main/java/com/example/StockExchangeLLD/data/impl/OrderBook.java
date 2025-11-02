package com.example.StockExchangeLLD.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;

import com.example.StockExchangeLLD.data.IOrderBook;
import com.example.StockExchangeLLD.models.Order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderBook implements IOrderBook {

    
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

    public boolean updateOrder(Order updatedOrder) {

        String stockSymbol = updatedOrder.getStockSymbol();
        ReadWriteLock lock = getOrCreateLock(stockSymbol);

        lock.writeLock().lock();

        try {
            List<Order> orders = orderBook.get(stockSymbol);
            if(orders != null) {
                for(int i = 0; i < orders.size(); i++) {
                    if(orders.get(i).getOrderId().equals(updatedOrder.getOrderId())) {
                        orders.set(i, updatedOrder);
                        log.info("Order updated in order book");
                        return true;
                    }
                }
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Order> getOrderBySymbol(String symbol) {
        ReadWriteLock lock = getOrCreateLock(symbol);
        lock.readLock().lock();
        try {
            return Optional.ofNullable(orderBook.get(symbol).stream().findFirst().orElse(null));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Order> getOrders(String stockSymbol) {
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.readLock().lock();
        try {
            return orderBook.get(stockSymbol);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Order> getOrderByOrderId(String orderId) {

        for(Map.Entry<String, List<Order>> entry : orderBook.entrySet()) {
            String stockSymbol = entry.getKey();
            ReadWriteLock lock = getOrCreateLock(stockSymbol);
            lock.readLock().lock();

            try {
                List<Order> orders = entry.getValue();
                for(Order order : orders) {
                    if(order.getOrderId().equals(orderId)) {
                        return Optional.of(order);
                    }
                }
            } finally {
                lock.readLock().unlock();
            }
        }
        return Optional.empty();
    }

    private ReadWriteLock getOrCreateLock(String stockSymbol) {
        return symbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());
    }
}
