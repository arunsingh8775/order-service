package com.example.orderservice.controller;

import com.example.orderservice.model.OrderRequest;
import com.example.orderservice.model.OrderResponse;
import com.example.orderservice.kafka.OrderEventProducer;
import com.example.orderservice.service.OrderService;
import com.example.ordertrackingcommon.model.OrderEvent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Management", description = "Create and retrieve orders")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderEventProducer orderEventProducer;

    public OrderController(OrderService orderService, OrderEventProducer orderEventProducer) {
        this.orderService = orderService;
        this.orderEventProducer = orderEventProducer;
    }

    @Operation(
            summary = "Create a new order",
            description = "Persists an order and publishes an `OrderCreated` event to Kafka.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = OrderRequest.class),
                            examples = @ExampleObject(
                                    name = "Sample order",
                                    value = """
                    {
                      "userId": "U001",
                      "productId": "P001",
                      "quantity": 2
                    }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class),
                                    examples = @ExampleObject(
                                            name = "Created",
                                            value = """
                        {
                          "eventType": "OrderCreated",
                          "orderId": 1,
                          "userId": "U001",
                          "productId": "P001",
                          "quantity": 2,
                          "amount": 200.0
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        // Save order in DB
        OrderResponse savedOrder = orderService.createOrder(orderRequest);

        // Build and publish event
        OrderEvent event = new OrderEvent(
                "OrderCreated",
                savedOrder.getOrderId(),
                savedOrder.getUserId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getAmount()
        );
        orderEventProducer.publishOrderEvent(event);

        return ResponseEntity.ok(savedOrder);
    }

    @Operation(
            summary = "Get order by ID",
            description = "Fetch a single order by its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order found",
                    content = @Content(
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(
                                    name = "Sample order",
                                    value = """
                {
                  "eventType": "OrderCreated",
                  "orderId": 1,
                  "userId": "U001",
                  "productId": "P001",
                  "quantity": 2,
                  "amount": 200.0
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Order ID", example = "101")
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    @Operation(
            summary = "Get all orders",
            description = "Fetches all orders from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Orders list",
            content = @Content(
                    array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            )
    )
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
