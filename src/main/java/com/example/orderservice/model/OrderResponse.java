package com.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String eventType;   // "OrderCreated", "PaymentCompleted", etc.
    private Long orderId;
    private String userId;
    private String productId;
    private Integer quantity;
    private Double amount;
}