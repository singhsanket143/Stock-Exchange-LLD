package com.example.StockExchangeLLD.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.StockExchangeLLD.models.Trade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TradeService {
    
    private final Map<String, Trade> tradeMap = new ConcurrentHashMap<>();

    public Trade saveTrade(Trade trade) {
        tradeMap.put(trade.getTradeId(), trade);
        return trade;
    }

    public Optional<Trade> getTrade(String tradeId) {
        return Optional.ofNullable(tradeMap.get(tradeId));
    }

    public List<Trade> getTradesByStockSymbol(String stockSymbol) {
        return tradeMap.values().stream().filter(trade -> trade.getStockSymbol().equals(stockSymbol)).collect(Collectors.toList());
    }
}
