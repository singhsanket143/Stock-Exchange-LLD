package com.example.StockExchangeLLD.data.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;

import com.example.StockExchangeLLD.models.Order;

public class OrderBook implements IOrderBook{

    
    private final ConcurrentMap<String, List<Order>> orderBook = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ReadWriteLock> symbolLocks = new ConcurrentHashMap<>();
    
}
