package com.example.StockExchangeLLD.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.StockExchangeLLD.data.IOrderBook;
import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.OrderStatus;
import com.example.StockExchangeLLD.models.OrderType;
import com.example.StockExchangeLLD.models.Trade;
import com.example.StockExchangeLLD.services.strategies.OrderMatchingStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradingService {
    
    private final IOrderBook orderBook;
    private final OrderMatchingStrategy orderMatchingStrategy;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public Order placOrder(Order order) {
        // TODO: validations 

        order.setOrderAcceptedTimeStamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setRemainingQuantity(order.getQuantity());

        orderBook.addOrder(order);

        CompletableFuture.runAsync(() -> {
            try {
                executeOrderMatch(order);
            } catch (Exception e) {
                log.error("Error executing order match", e);
            }
        }, executorService); 
        
        return order;
    }

    private void executeOrderMatch(Order newOrder) {
        String stockSymbol = newOrder.getStockSymbol();

        List<Order> existingOrders = orderBook.getOrders(stockSymbol);

        existingOrders = existingOrders.stream().filter(order -> !order.getOrderId().equals(newOrder.getOrderId())).collect(Collectors.toList());

        List<Trade> executedTrades = orderMatchingStrategy.matchOrders(newOrder, existingOrders);

        if(!executedTrades.isEmpty()) {
            for(Trade trade : executedTrades) {
                // save trades in the db
            }

            orderBook.updateOrder(newOrder);

            for(Trade trade : executedTrades) {
                String otherOrderId = newOrder.getOrderType() == OrderType.BUY ? trade.getSellerOrderId() : trade.getBuyerOrderId();
                orderBook.getOrderByOrderId(otherOrderId).ifPresent(orderBook::updateOrder);
            }

            log.info("Order matched successfully");
        }
    }



}
