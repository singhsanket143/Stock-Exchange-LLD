package com.example.StockExchangeLLD.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.StockExchangeLLD.dtos.OrderRequest;
import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.services.TradingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Slf4j
@RestController
@RequestMapping("/api/v1/trading")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService; // violated DIP, TODO: fix this

    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = tradingService.placeOrder(orderRequest);

        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/orderBook/{symbol}")
    public ResponseEntity<List<Order>> getOrderBook(@PathVariable String symbol) {
        List<Order> orderBook = tradingService.getOrderBook(symbol);

        return ResponseEntity.ok(orderBook);
    }
    
}
