package com.example.StockExchangeLLD.models;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

    @Builder.Default
    private String tradeId = UUID.randomUUID().toString();

    @NotBlank(message = "Buyer order ID is required")
    private String buyerOrderId;

    @NotBlank(message = "Seller order ID is required")
    private String sellerOrderId;

    @NotBlank(message = "Stock Symbol is required")
    private String stockSymbol;

    @NotNull(message = "Quantity is required")
    private int quantity;

    @NotNull(message = "Price is required")
    private double price;

    
    
}
