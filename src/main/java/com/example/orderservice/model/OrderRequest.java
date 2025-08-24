package com.example.orderservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating an order")
public class OrderRequest {

    @Schema(description = "ID of the user placing the order", example = "U001")
    private String userId;

    @Schema(description = "ID of the product being ordered", example = "P001")
    private String productId;

    @Schema(description = "Quantity of product ordered", example = "2")
    private Integer quantity;
}
