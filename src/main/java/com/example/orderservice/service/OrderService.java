package com.example.orderservice.service;

import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.OrderRequest;
import com.example.orderservice.model.OrderResponse;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.model.OrderEntity;
import com.example.orderservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Convert request to entity
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(orderRequest.getUserId());
        orderEntity.setProductId(orderRequest.getProductId());
        orderEntity.setQty(orderRequest.getQuantity());
        orderEntity.setTotalAmount(calculateAmount(orderRequest)); // simple calculation
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setPaymentStatus(PaymentStatus.NOT_PAID);
        orderEntity.setShippingStatus(ShippingStatus.NOT_SHIPPED);
        orderEntity.setCreatedAt(LocalDateTime.now());

        // Save to DB
        OrderEntity saved = orderRepository.save(orderEntity);

        // Convert to response
        return mapToResponse(saved);
    }

    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private OrderResponse mapToResponse(OrderEntity entity) {
        return new OrderResponse(
                "OrderCreated",        // eventType
                entity.getOrderId(),
                entity.getUserId(),
                entity.getProductId(),
                entity.getQty(),
                entity.getTotalAmount()
        );
    }


    private Double calculateAmount(OrderRequest request) {
        // For demo purposes, assume product price = 100/unit
        return request.getQuantity() * 100.0;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}
