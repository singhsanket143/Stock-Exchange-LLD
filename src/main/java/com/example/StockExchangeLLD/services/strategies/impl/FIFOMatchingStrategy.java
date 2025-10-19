package com.example.StockExchangeLLD.services.strategies.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.OrderStatus;
import com.example.StockExchangeLLD.models.OrderType;
import com.example.StockExchangeLLD.models.Trade;
import com.example.StockExchangeLLD.services.strategies.OrderMatchingStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FIFOMatchingStrategy implements OrderMatchingStrategy{
    
    @Override
    public String getStrategyName() {
        return "FIFO";
    }

    @Override
    public List<Trade> matchOrders(Order newOrder, List<Order> existingOrders) {
        if(newOrder.getOrderType() == OrderType.BUY) {
            return matchBuyOrder(newOrder, existingOrders);
        } else {
            return matchSellOrder(newOrder, existingOrders);
        }
    }

    private List<Trade> matchBuyOrder(Order buyOrder, List<Order> existingOrders) {
        List<Trade> trades = new ArrayList<>();

        List<Order> matchingSellOrders = existingOrders.stream()
                                        .filter(order -> order.getOrderType() == OrderType.SELL)
                                        .filter(order -> order.getStockId().equals(buyOrder.getStockId()))
                                        .filter(order -> order.getPrice() <= buyOrder.getPrice())
                                        .filter(order -> order.getOrderStatus() == OrderStatus.ACCEPTED)
                                        .sorted(Comparator.comparing(Order::getPrice).thenComparing(Order::getOrderAcceptedTimeStamp))
                                        .collect(Collectors.toList());

        int remainingQuantity = buyOrder.getRemainingQuantity();

        for(Order sellOrder : matchingSellOrders) {
            if(remainingQuantity <= 0) break; // we have matched all the buys

            int tradeQuantity = Math.min(remainingQuantity, sellOrder.getRemainingQuantity());

            Double tradePrice = sellOrder.getPrice();

            Trade trade = Trade.builder()
                          .buyerOrderId(buyOrder.getOrderId())
                          .sellerOrderId(sellOrder.getOrderId())
                          .stockId(buyOrder.getStockId())
                          .quantity(tradeQuantity)
                          .price(tradePrice)
                          .build();

            trades.add(trade);



            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity() - tradeQuantity);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);

            remainingQuantity -= tradeQuantity;

            log.info("Trade: {} - {} - {} - {} - {} - {} - {} - {}", trade.getTradeId(), trade.getBuyerOrderId(), trade.getSellerOrderId(), trade.getStockId(), trade.getQuantity(), trade.getPrice(), buyOrder.getFilledQuantity(), buyOrder.getRemainingQuantity(), sellOrder.getFilledQuantity(), sellOrder.getRemainingQuantity());

        }

        return trades;  
    }

    private List<Trade> matchSellOrder(Order sellOrder, List<Order> existingOrders) {
        return null;
    }
}
