package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String userId;

    private String productId;

    private Integer qty;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // Enum: PENDING, COMPLETED, CANCELLED

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // Enum: NOT_PAID, PAID

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus; // Enum: NOT_SHIPPED, SHIPPED

    private LocalDateTime shippingDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
