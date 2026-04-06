package com.badminton.store.mapper;

import com.badminton.store.dto.order.OrderDto;
import com.badminton.store.dto.orderDetail.OrderDetailDto;
import com.badminton.store.model.Order;
import com.badminton.store.model.OrderDetail;
import com.badminton.store.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DateUtils.class})
public interface OrderMapper {
    @Mapping(source = "coupon.code", target = "couponCode")
    @Mapping(source = "orderDetails", target = "items")
    @Mapping(target = "createdDate", expression = "java(com.badminton.store.utils.DateUtils.formatDate(order.getCreatedDate()))")
    OrderDto fromEntityToDto(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.title", target = "productTitle")
    OrderDetailDto fromDetailToDto(OrderDetail detail);

    List<OrderDto> fromEntityListToDtoList(List<Order> list);
}