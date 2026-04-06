package com.badminton.store.mapper;

import com.badminton.store.dto.cart.CartDto;
import com.badminton.store.dto.cartItem.CartItemDto;
import com.badminton.store.model.Cart;
import com.badminton.store.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    @Mapping(source = "cartItems", target = "items")
    CartDto fromEntityToDto(Cart cart);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.title", target = "productTitle")
    @Mapping(expression = "java(item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty() ? item.getProduct().getImages().get(0) : null)", target = "productImage")
    @Mapping(expression = "java(item.getPrice() * item.getQuantity())", target = "subTotal")
    CartItemDto fromEntityToItemDto(CartItem item);

    List<CartItemDto> fromEntityListToItemDtoList(List<CartItem> list);
}