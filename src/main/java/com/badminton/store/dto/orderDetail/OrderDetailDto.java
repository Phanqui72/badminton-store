package com.badminton.store.dto.orderDetail;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Long productId;
    private String productTitle;
    private Integer quantity;
    private Double price;
}