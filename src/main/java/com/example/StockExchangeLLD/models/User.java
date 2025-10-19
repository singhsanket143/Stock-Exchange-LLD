package com.example.StockExchangeLLD.models;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Builder.Default
    private String userId = UUID.randomUUID().toString();

    @NotBlank(message = "User name is required")
    private String userName;

    @Email(message = "Invalid email address")
    private String email;

    private String phoneNumber;
    
}
