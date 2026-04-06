package com.badminton.store.dto.order;

import com.badminton.store.dto.orderDetail.OrderDetailDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Double totalPrice;
    private String shippingAddress;
    private String receiverName;
    private String receiverPhone;
    private int status;
    private String couponCode;
    private List<OrderDetailDto> items;
    private String createdDate; // Sẽ format qua DateUtils
}
