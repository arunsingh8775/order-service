package com.example.orderservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload returned after processing an order")
public class OrderResponse {

    @Schema(description = "Type of event generated",
            example = "OrderCreated")
    private String eventType;

    @Schema(description = "Unique ID of the created order",
            example = "101")
    private Long orderId;

    @Schema(description = "ID of the user who placed the order",
            example = "U001")
    private String userId;

    @Schema(description = "ID of the ordered product",
            example = "P001")
    private String productId;

    @Schema(description = "Quantity of the product ordered",
            example = "2")
    private Integer quantity;

    @Schema(description = "Total amount of the order",
            example = "200.0")
    private Double amount;
}
