package com.example.orderservice.controller;

import com.example.orderservice.model.OrderRequest;
import com.example.orderservice.model.OrderResponse;
import com.example.orderservice.kafka.OrderEventProducer;
import com.example.orderservice.service.OrderService;
import com.example.ordertrackingcommon.model.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderEventProducer orderEventProducer;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        // Save order in DB
        OrderResponse savedOrder = orderService.createOrder(orderRequest);

        // Build event
        OrderEvent event = new OrderEvent(
                "OrderCreated",
                savedOrder.getOrderId(),
                savedOrder.getUserId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getAmount()
        );

        // Send event to Kafka
        orderEventProducer.publishOrderEvent(event);

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
